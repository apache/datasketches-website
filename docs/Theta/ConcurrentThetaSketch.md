---
layout: doc_page
---
<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->
# Concurrent Theta Sketch

Concurrent Theta sketch is a manifestation of a generic approach for parallelizing sketches while bounding the error such parallelism introduces<sup>1</sup>.

At its core, a generic concurrent sketch ingests data through multiple sketches that are _local_ to the inserting threads.  
The data in these local sketches, which are bounded in size, is merged into a single _shared_ sketch by utilizing the sketch _mergability_ property. 
Queries are served from a _snapshot_ of the shared sketch.
This snapshot is taken frequently enough to guarantee the result's freshness, and seldom enough to not become the bottleneck of the sketch.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/GenericConcurrentSketch.png" alt="GenericConcurrentSketch" />

Unlike previous solutions, this design enables simultaneous queries and updates to a sketch from an arbitrary number of threads. 
The responsibility for merging the thread-local sketch into the shared sketch is divided into two

1. *Eager propagation*. When the sketch is small any delay in merging the local data into the shared thread--so it is captured by the snapshot--can incur a large error in the query result.
Therefore, data is eagerly propagated to the shared sketch by the inserting thread upon each update. 
2. *Lazy propagation*. When sketches are big enough, the local sketches are used to buffer data that should be added to the shared sketch. 
A _background propagation thread_ continuously merges full local sketches into the shared sketch.

## Implementation and User API

Both the local sketch and the shared sketch are descendants of UpdateSketch and therefore support its API.
However, it is important that the shared sketch is only used to get the estimate, while updates only go through the local sketches.
The shared sketch can be allocated either off-heap or on-heap, while the local sketch is always allocated on-heap.

Like other Theta sketches, `UpdateSketchBuilder` is used to build the shared and local sketches. 
It is imperative that the shared sketch is built first. 
Then, at the context of an application thread(/s) that feeds the data a local sketch is created and connected to the shared sketch.
This is a list of the configuration parameters for the builder:
1. Buffer size of shared sketch
2. Buffer size of local sketches
3. Size of the threads pool to handle propagation of all sketches
4. Flag to indicate if the propagated data is to be sorted prior to propagation
5. Max concurrency error; the point the sketch flips from exact to estimate mode is derived from this parameter
6. Max number of local threads to be used

## Code Example for Building a Concurrent Theta Sketch

    import org.apache.datasketches.memory.WritableDirectHandle;
    import org.apache.datasketches.memory.WritableMemory;
    import org.apache.datasketches.theta.Sketch;
    import org.apache.datasketches.theta.UpdateSketch;
    import org.apache.datasketches.theta.UpdateSketchBuilder;

    class ApplicationWithsketches {
    
        private UpdateSketchBuilder bldr;
        private UpdateSketch sharedSketch;
        private Thread writer;

        private int sharedLgK;
        private int localLgK;
        private boolean ordered;
        private boolean offHeap;
        private int poolThreads;
        private double maxConcurrencyError;
        private int maxNumWriterThreads;
        private WritableDirectHandle wdh;
        private WritableMemory wmem;

    
        //configures builder for both local and shared
        void buildConcSketch() {
            bldr = new UpdateSketchBuilder();

            // All configuration parameters are optional
            bldr.setLogNominalEntries(sharedLgK);     // default 12 (K=4096)
            bldr.setLocalLogNominalEntries(localLgK); // default 4 (B=16)
            bldr.setNumPoolThreads(poolThreads);      // default 3
            bldr.setPropagateOrderedCompact(ordered); // default true
            bldr.setMaxConcurrencyError(maxConcurrencyError);   // default 0
            bldr.setMaxNumLocalThreads(maxNumWriterThreads);   // default 1
            
            // build shared sketch first
            final int maxSharedUpdateBytes = Sketch.getMaxUpdateSketchBytes(1 << sharedLgK);    
            if (offHeap) {
              wdh = WritableMemory.allocateDirect(maxSharedUpdateBytes);
              wmem = wdh.get();
            } else {
              wmem = null; //WritableMemory.allocate(maxSharedUpdateBytes);
            }
            sharedSketch = bldr.buildShared(wmem);
        }
        
        void mainApplicationMethod() {
            // init attributes, e.g, with properties file
            ...
            buildConcSketch();
            writer = new WriterThread(bldr, sharedSketch);
            
            while(#some_application_condition) {
                // get estimate through shared sketch
                doSomethingWithEstimate(sharedSketch.getEstimate());
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        
    // Context of writer thread 
    class WriterThread extends Thread {
    
        private UpdateSketch local;
    
        // build local sketches from bldr and reference to shared sketch
        public WriterThread(final UpdateSketchBuilder bldr, final UpdateSketch shared) {
            local = bldr.buildLocal(shared);
            //init input stream, such as a queue, or a communication buffer, etc.
        }
        
        // updtae concurrent sketch through local sketch - no need for locks or any other synchronization
        public void run() {
            while(true) {
                if(#input_stream_is_not_empty) {
                long data = getDataFromInputStream();
                local.update(data);
            }
        }
    }
        

## Serializing a Concurrent Sketch
A concurrent sketch is not a single unit of computation. It is composed of the shared sketch and the local buffers. 
Only the shared sketch supports serialization as it captures the most up-to-date content of the sketch.
In the current implementation, deserializing a shred sketch yields an `UpdateSketch`.
Therefore when de-serializing a concurrent sketch both the shared sketch and the local buffers need to be re-created again. 

## Code Example for Serializing and Deserializing a Concurrent Theta Sketch

    import org.apache.datasketches.memory.WritableMemory;
    import org.apache.datasketches.theta.Sketch;
    import org.apache.datasketches.theta.UpdateSketch;
    import org.apache.datasketches.theta.UpdateSketchBuilder;

    public class serDeTest {
        
        private UpdateSketchBuilder bldr;
        private UpdateSketch sharedSketch;
        private WritableMemory wmem;
        
        void serDeConcurrentQuickSelectSketch() {
            int k = 512;
            
            // build shared sketch and local buffer as in the example above
            bldr = new UpdateSketchBuilder();
            ...
            sharedSketch = bldr.buildShared(wmem);
            UpdateSketch local = bldr.buildLocal(sharedSketch);
            
            int i=0;
            // update sketch through local buffer
            for (; i<10000; i++) {
              local.update(i);
            }
            
            // serialize shared
            byte[]  serArr = shared.toByteArray();
            Memory srcMem = Memory.wrap(serArr);
            Sketch recovered = Sketches.heapifyUpdateSketch(srcMem);

            //reconstruct to Native/Direct
            final int bytes = Sketch.getMaxUpdateSketchBytes(k);
            wmem = WritableMemory.allocate(bytes);
            shared = bldr.buildSharedFromSketch((UpdateSketch)recovered, wmem);
            UpdateSketch local2 = bldr.buildLocal(shared);
            
            // check estimate ~10K
            System.out.println("Estimate="+sharedSketch.getEstimate();
            
            // continue updating through new local buffer
            for (; i<20000; i++) {
              local2.update(i);
            }            

            // check estimate ~20K
            System.out.println("Estimate2="+sharedSketch.getEstimate();
        }
    
    }
    
    
    
[1] Arik Rinberg, Alexander Spiegelman, Edward Bortnikov, Eshcar Hillel, Idit Keidar, Hadar Serviansky, *Fast Concurrent Data Sketches*, https://arxiv.org/abs/1902.10995
