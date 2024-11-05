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
## Theta Sketch Resize Factor
For Theta Sketches, the *Resize Factor* is a dynamic, speed performance vs. memory size tradeoff. 

Sketches that are created on-heap and configured with a Resize Factor of > X1 start out with an internal hash table size that is the smallest submultiple of the the target Nominal Entries, and larger than the minimum required hash table size, which is 16.

When the sketch needs to be resized larger the *Resize Factor* is used as a multiplier of the current sketch cache array size.

**X1** means no resizing is allowed and the sketch will be intialized at full target size.

**X2** means the internal cache will start very small and double in size until the target size is reached.

Similarly, **X4** is a factor of 4 and **X8** is a factor of 8.

For example, suppose you configure a sketch with a *lgK* = 10 (*k* =1024) and a resize factor as follows:

* **X8** (the default):<br>
Internally the sketch will start out with an internal hash array of size 16 (which corresponds to the smallest allowed value of k).  Its memory size is 128+24 bytes. 
    * When the hash array has filled it will grow the hash array by a factor of 8, to 1024+24 bytes.  
    * When it needs to grow again it will grow by a factor of 8 again, to 819+24 bytes, which is the max size.  
    * So the growth sequence of the effective internal k is 16, 128, 1024 for a total of 2 resize operations.

* **Similarly with X4:**<br>
The growth sequence is 16, 64, 256, 1024 for a total of 3 resize operations.

* **Similarly with X2:**<br> 
The growth sequence is 16, 32, 64, 128, 256, 512, 1024 for a total of 6 resize operations.

* **With X1:**<br>
The growth multiplier is one, so it must start the sketch at full size of 1024 with no resizing operations.

Resizing the internal hash array is an expensive operation, relatively speaking, so if real-time performance is critical you might set the resize factor to **X1**, but you pay in memory usage up front with a sketch that is always full size.

If you want to minimize memory usage then you want the sketch to stay as small as possible and let it grow only as required, so you would use **X2**, but the real-time performance will be a little slower because of the resizing operations.

**X4** and **X8** are in between **X1** and **X2**.
