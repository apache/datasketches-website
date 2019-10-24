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
## Size and Byte Array Structures

All the sketches in the <i>theta</i> package share a common byte array data structure and similar 
strategies for managing use of memory when the sketches are instantiated on the Java heap or 
when instantiated in off-heap native memory.

The byte array structure has two forms <i>Hash Table</i> and <i>Compact</i>.

### Update Sketch Families -- Hash Table Form
The Hash Table form is similar to how the sketch is instantiated on the Java heap. 
Hash tables consume more space depending on how full the table is. 
However, updating the sketch is much faster in this form and is the default for all the Update Sketches.

There are two Update Sketch families, <i>QuickSelect</i>, and <i>Alpha</i>. 
The QuickSelect family has both a Java heap and a direct, off-heap variants while the 
Alpha sketch is designed only for operation on the Java heap. 

The Update Sketches consist of an internal hash table cache and a few other primitives. 
How the cache is sized and/or resized can be controlled by the user specified 
<i>ResizeFactor enum</i> that has the values <i>X1, X2, X4</i>, and <i>X8</i> (default). 
The meaning of these values is as follows:

* X1: A Resize Factor of 1 means the cache will not increase its size. A new sketch will 
immediately allocate space for twice the given Nominal Entries. 
For example, if <i>nomEntries is 4096</i> the internal cache will be allocated at 8192 entries. 
Each entry is 8 bytes.
* X2: A Resize Factor of 2 means the cache may increase the cache size by factors of 2. 
A new sketch will initially allocate a very small cache (typically enough for 16 entries), 
and, when required, will increase the cache size by factors of 2 until the maximum cache 
size is reached, which is twice the Nominal Entries.
* X4: A Resize Factor of 4 is similar to X2, except the resizing factor is 4.
* X8: A Resize Factor of 8 is similar to X2, except the resizing factor is 8. 
This is the default. 

Resizing a cache is costly as a new array needs to be allocated and the hash table rebuilt. 
Depending on the application, the user can select the Resize Factor to trade-off memory size 
and overall update speed performance.

When an Update Sketch is converted to a byte array using <i>toByteArray()</i>, 
the internal structure is a 24-byte preamble followed by a non-contiguous 
hash table array of 8-byte, long data entries.
This enables the sketch to be quickly reconstructed from the byte array so that updating 
of the sketch can be continued.

### Compact Sketch Family -- Compact Form
Once the updating of a sketch is completed the HT is no longer needed, so the sketch can be 
stored in a compact form. 
The size of this compact form is a simple function of the number of retained hash values 
(8 bytes) and a small preamble that varies from 8 to 24 bytes depending on the 
internal state of the sketch.  An empty sketch is represented by only 8 bytes. 
The upper limit of the size of the sketch varies by the type of sketch but is 
in the range of <i>8*k to 16*k</i>. 
<i>k</i> is the configured size of the sketch in nominal entries, 
and also determines the accuracy of the sketch.

Compact Sketches optimize memory usage and sketch merge performance, are inherently <i>read only</i> 
and can only be created from an existing Update Sketch or Set Operation 
(<i>Union, Intersection, or AnotB</i>) object. 
The internal structure of Compact Sketches is an 8, 16, or 24 byte preamble followed 
by a contiguous data array of 8-byte, long data entries. 
This data array can be either <i>ordered</i> or <i>unordered</i>. 
This data structure is the same whether the Compact Sketch is instantiated on the 
Java heap or in off-heap direct memory. 
An empty Compact Sketch consumes only 8 bytes. 

The <i>ordered</i> form is ideal for systems environments where the building of the sketches 
from data occur in an offline system (like a Hadoop grid), then compacted, ordered and 
uploaded to a real-time query engine (like Druid) where the compact sketches can be quickly 
merged to satisfy end-user queries. 
The ordered form enables <i>early stop</i> enhancements to the merge algorithm 
that makes the merging extreemly fast (~ 10's of millions of sketches per second).

The <i>unordered</i> form is more desirable for systems environments where the 
building of the sketches from data occur in real-time and queried in real-time. 
In this environment there is no need to pay the cost of the sort.

Thus, the choice of <i>ordered</i> or <i>unordered</i> is a tradeoff between 
real-time sketch build & getEstimate() performance and offline sketch-build 
and real-time merge performance.

The Compact Sketch is created four different ways selected by the ordering preference 
(<i>dstOrdered</i>) and whether the Compact Sketch will reside on-heap or off-heap (<i>dstMemory</i>). 

* Unordered, On-heap 
  * Update Sketch: <i>compact(false, null)</i>
  * Set Operation: <i>getResult(false, null)</i>
* Ordered, On-heap
  * Update Sketch: <i>compact(true, null)</i>
  * Set Operation: <i>getResult(true, null)</i>
* Unordered, Off-heap
  * Update Sketch: <i>compact(false, Memory mem)</i>
  * Set Operation: <i>getResult(false, Memory mem)</i>
* Ordered, Off-heap
  * Update Sketch: <i>compact(true, Memory mem)</i>
  * Set Operation: <i>getResult(true, Memory mem)</i>

#### Compact Sketch Sizing
These tables compute the size of a sketch after it has been converted into Compact Form. 
The size of a sketch during the build phase is explained above as the sketch starts small and 
resizes by the configurable <i>Resize Factor</i> up to the in-memory size of <i>2k*8</i> bytes plus
a few primitives.

Note: a sketch entry = 8 bytes.

##### Quick Select Sketch (Default)
The number of valid entries in the Quick Select Sketch after it enters estimation mode
statistically varies from <i>k</i> to <i>15k/8</i> with an average of about <i>3k/2</i>. 
It is a user option to force a rebuild() prior to compacting the sketch in which case the 
number of valid entries is never larger than <i>k</i>.


&nbsp;  | Empty | After Rebuild() | Estimating Avg | Estimating Max
Nominal Entries (k) : Formula -> | 8 | k*8 +24 | k*12 + 24 | k*15 + 24
----------------|-------------|-------------|------------|--------------
16 | 8 | 152 | 216 | 264
32 | 8 | 280 | 408 | 504
64 | 8 | 536 | 792 | 984
128 | 8 | 1,048 | 1,560 | 1,944
256 | 8 | 2,072 | 3,096 | 3,864
512 | 8 | 4,120 | 6,168 | 7,704
1,024 | 8 | 8,216 | 12,312 | 15,384
2,048 | 8 | 16,408 | 24,600 | 30,744
4,096 | 8 | 32,792 | 49,176 | 61,464
8,192 | 8 | 65,560 | 98,328 | 122,904
16,384 | 8 | 131,096 | 196,632 | 245,784
32,768 | 8 | 262,168 | 393,240 | 491,544
65,536 | 8 | 524,312 | 786,456 | 983,064
131,072 | 8 | 1,048,600 | 1,572,888 | 1,966,104
262,144 | 8 | 2,097,176 | 3,145,752 | 3,932,184
524,288 | 8 | 4,194,328 | 6,291,480 | 7,864,344
1,048,576 | 8 | 8,388,632 | 12,582,936 | 15,728,664

##### Alpha Sketch
The number of valid entries in the Alpha Sketch after it enters estimation mode 
is a random variable that has a probability distribution with a mean of <i>k</i>
and a standard deviation of <i>sqrt(k)</i>. 
The last column computes the maximum size with a confidence of 99.997% representing
plus 4 standard deviations.


&nbsp;  | Empty | Estimating Avg | Std Dev | Max @ 99.997% 
Nominal Entries (k) : Formula -> | 8 | k*8 + 24 | sqrt(k) | (k+4SD)*8 +24
----------------|-------------|-------------|------------|----------
512 | 8 | 4,120 | 23 | 4,844
1,024 | 9 | 8,216 | 32 | 9,240
2,048 | 10 | 16,408 | 45 | 17,856
4,096 | 11 | 32,792 | 64 | 34,840
8,192 | 12 | 65,560 | 91 | 68,456
16,384 | 13 | 131,096 | 128 | 135,192
32,768 | 14 | 262,168 | 181 | 267,961
65,536 | 15 | 524,312 | 256 | 532,504
131,072 | 16 | 1,048,600 | 362 | 1,060,185
262,144 | 17 | 2,097,176 | 512 | 2,113,560
524,288 | 18 | 4,194,328 | 724 | 4,217,498
1,048,576 | 19 | 8,388,632 | 1,024 | 8,421,400


### Set Operation Family

#### Union
The <i>Union</i> operation has both a Java heap and a direct, off-heap variant. 
When a Union operation is converted to a byte array using <i>toByteArray()</i>, 
the internal structure is a 32-byte preamble followed by a non-contiguous hash 
table array of 8-byte, long data entries. 
This enables the Union to be quickly reconstructed from the byte array 
so that updating of the Union can be continued.

#### Intersection
The <i>Intersection</i> operation has both a Java heap and a direct, off-heap variant. 
When an Intersection operation is converted to a byte array using <i>toByteArray()</i>, 
the internal structure is a 24-byte preamble followed by a non-contiguous hash 
table array of 8-byte, long data entries. 
This enables the Intersection to be quickly reconstructed from the byte array 
so that updating of the Intersection can be continued.

#### A not B
The <i>A not B</i> operation is asymmetric and stateless. 
Both the <i>A</i> and <i>B</i> arguments are presented and the difference 
is computed and returned. 
There is no need for a byte array form.

