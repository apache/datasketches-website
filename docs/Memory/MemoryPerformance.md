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
## Memory Performance

These performance tests are very simple: the write test stores longs of 0 to k-1 into arrays of size k where k varied from 2^5 to 2^26.
The read test then reads those longs from those arrays. A trial consisted of the tight loop that filled or read the entire array.
Each test of size k was repeated up to millions of times to reduce noise in the measurements.

The inner timing loop for writing into a java array:

      startTime_nS = System.nanoTime();
      for (int i=0; i<arrLongs; i++) { array[i] = i; }
      stopTime_nS = System.nanoTime();

The inner timing loop for reading from that array:

      startTime_nS = System.nanoTime();
      for (int i=0; i<arrLongs; i++) { trialSum += array[i]; }
      stopTime_nS = System.nanoTime();

The trialSum is used as a simple checksum and to make sure the compiler optimizers didn't eliminate the loop.

---
<img class="doc-img-full" src="{{site.docs_img_dir}}/memory/Read_C_Java_Unsafe.png" alt="Read_C_Java_Unsafe.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/memory/Write_C_Java_Unsafe.png" alt="Write_C_Java_Unsafe.png" />

The first 2 figures compares a simple C program with 3 different compiler optimization levels, a java loop with a simple heap array as above, 
and then a java loop using Unsafe accessing on off-heap (Direct) array.

The first myth to be dispelled is that "Unsafe is as fast as Java on-heap access". Nope!
Unsafe is nowhere near as fast as the Java on-heap array access. 
It is 50% slower than Java on-heap, especially for the small array sizes, and 13% slower for the biggest array size. 
This has a lot to due with how efficiently the code can utilize the L1, L2 and L3 CPU caches.
Even if the Unsafe method calls are being replaced with hand-written assembly instructions, 
they are not as effective as the instructions that the JIT compiler emits.

Only in the case of writes to large arrays (> L3 cache) is the Unsafe comparable to the java heap array.

Now look at what the potential is from C. Using just O2 optimization.
The Java code is 3X slower for reads of small arrays and 65% slower for writes.
Oddly, the C code is slower for the larger arrays, but I'm sure there are tuning options to fix that.

---
<img class="doc-img-full" src="{{site.docs_img_dir}}/memory/BB_LB.png" alt="BB_LB.png" />

Next I looked at the ByteBuffer and LongBuffer to see how well they would do.

Reading and writing longs with a Heap ByteBuffer is unbelievably horrible. 
Even though the Endianness is set correctly to native, the code emitted is not taking proper advantage of
Unsafe calls and is likely disassembling the longs into bytes on the java side! 
I consider this a java performance bug.  Meanwhile using a Direct BB is much better but not quite as
good as using the Heap LongBuffer. And in this test, anyway, using the Direct LongBuffer is not quite as 
fast as using the Direct BB. 

What is really good is that the LongBuffer Heap Reads are very comparable to the Java array reads,
but slower on the writes.
For direct access the Unsafe reads and writes are faster than the LongBuffer direct.

Unless you are R/W to Heap, there is little incentive to using the LongBuffer. The LB performance is 
slower and the API is much weaker than the BB API.

---
<img class="doc-img-full" src="{{site.docs_img_dir}}/memory/Speed_0.12.0.png" alt="Speed_0.12.0.png" />

I then looked at the Memory package to see how it would perform.

The lines in this chart are from three different test-types:

- R/W into an heap array. Labeled HeapR and HeapW. This is the fastest access you can do in java, just putting and getting values from a java long array. Only C can do it faster, especially for small arrays that can live in the CPU caches.
- R/W into an off-heap array using unsafe calls directly in the loop code. No intervening classes or objects.
- R/W into an off-heap array using our Memory package. 

As you can see from the graph the fastest traces are the dashed lines for the on-heap arrays, with the exception of writing to an on-heap long array over 2^24 in size.  I am not sure exactly what is going on here.

Next in performance is the Memory Direct Read and the Unsafe Read which are right on top of each other. The good news is that the Memory package does not add ANY overhead and is as fast as a user using Unsafe calls directly in their code! 

The slowest of these three is the Memory Write and Unsafe Write which also track right on top of each other.  Nonetheless, 0.6 nanoseconds per operation is not bad!

---
My test system was my laptop which is an older Apple MacBook Pro:

- Model: MacBookPro 11,3
- CPU: 2.5 GHz Intel Core i7
- Memory: 16 GB 1600 MHz DDR3
- Cores: 4
- L2: 256KB
- L3: 6 MB 




