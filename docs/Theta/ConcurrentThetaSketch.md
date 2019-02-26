---
layout: doc_page
---

# Concurrent Theta Sketch

Concurrent Theta sketch is a manifestation of a generic approach for parallelizing sketchs while bounding the error such parallelism introduces<sup>1</sup>.

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
The user can also manipulate the size of propagation threads pool, and whether the propagated data is sorted prion to propagation.

## Code Example for Building a Concurrent Theta Sketch

    import static com.yahoo.sketches.Util.DEFAULT_UPDATE_SEED;
    import com.yahoo.memory.WritableDirectHandle;
    import com.yahoo.memory.WritableMemory;
    import com.yahoo.sketches.theta.Sketch;
    import com.yahoo.sketches.theta.UpdateSketch;
    import com.yahoo.sketches.theta.UpdateSketchBuilder;

    // Context of application code
    private UpdateSketchBuilder bldr;
    private UpdateSketch sharedSketch;

    // Configuration parameters 
    sharedLgK = Integer.parseInt(prop.mustGet("LgK"));
    localLgK = Integer.parseInt(prop.mustGet("CONCURRENT_THETA_localLgK"));
    ordered = Boolean.parseBoolean(prop.mustGet("CONCURRENT_THETA_ordered"));
    offHeap = Boolean.parseBoolean(prop.mustGet("CONCURRENT_THETA_offHeap"));
    poolThreads = Integer.parseInt(prop.mustGet("CONCURRENT_THETA_poolThreads"));
    
    // configure builder for both local and shared
    {
        final UpdateSketchBuilder bldr = new UpdateSketchBuilder();
        bldr.setNumPoolThreads(poolThreads);
        bldr.setLogNominalEntries(sharedLgK);
        bldr.setLocalLogNominalEntries(localLgK);
        bldr.setSeed(DEFAULT_UPDATE_SEED);
        bldr.setPropagateOrderedCompact(ordered);
    }
    
    
    // build shared sketch first
    {
        final int maxSharedUpdateBytes = Sketch.getMaxUpdateSketchBytes(1 << sharedLgK);    
        if (offHeap) {
          wdh = WritableMemory.allocateDirect(maxSharedUpdateBytes);
          wmem = wdh.get();
        } else {
          wmem = null; //WritableMemory.allocate(maxSharedUpdateBytes);
        }
        sharedSketch = bldr.buildShared(wmem);
    }
    
    // Context of writer thread e.g.,
    // class WriterThread extends Thread
    
    private UpdateSketch local;
    
    // build local sketches from bldr and reference to shared sketch
    {
        local = bldr.buildLocal(sharedSketch);
    }
    
    // updtae concurrent sketch through local sketch - no need for locks or any other synchronization
    {
        local.update(data);
    }
    
    // Context of reader thread e.g.,
    // class ReaderThread extends Thread
    
    // get estimate through reference to shared sketch
    {
        doSomethingWithEstimate(sharedSketch.getEstimate());
    }




[1] TBD, arXiv/PODC