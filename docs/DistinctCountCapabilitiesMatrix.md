---
sectionid: docs
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

## Capabilities Matrix for Distinct Count Sketches

Category | Sub-Category | Theta | Tuple | HLL | CPC |
:-------:|:------------:|:-----:|:-----:|:---:|:---:|
Space Accuracy Config | Min Log Precision (Min LgK) | 4 | 4 | 4 | 4 |
 | Max Log Precision (Max LgK) | 26 | 26 | 21 | 26 |
 | Min Log Sparse Precision | 64 | 64 | 26 | 26 |
 | Max Log Sparce Precision | 64 | 64 | 26 | 26 |
Space Accuracy Factors | Entropy bits / slot = b | 64 | 64 | 4 | 4.8 |
 | Error Coefficient = C | 1 | 1 | 1.04 | 0.69 |
 | HIP Error Coefficient = C |  |  | 0.83 | 0.59 |
Space Accuracy Merit<sup>1</sup> | Merging | 64 |  | 4.33 | 2.31 |
 | Not Merging (HIP) |  |  | 2.78 | 1.66 |
Input Types | int | Y | Y | Y | Y |
 | long | Y | Y | Y | Y |
 | double | Y | Y | Y | Y |
 | String | Y | Y | Y | Y |
 | byte[] | Y | Y | Y | Y |
 | char[] | Y |  | Y | Y |
 | int[] | Y | Y | Y | Y |
 | long[] | Y | Y | Y | Y |
Sketch results | Estimate () | double | double | double | double |
 | Upper Bound ( Std Dev ) | double | double | double | double |
 | Lower Bound ( Std Dev ) | double | double | double | double |
 | Estimate (numSubSetRows) |  | double |  |  |
 | Upper Bound (Std Dev, numSubset Rows) |  | double |  |  |
 | Lower Bound (Std Dev, numSubset Rows) |  | double |  |  |
 | Iterator() | Y | Y | Y | Y |
Set Operations | Union | Y | Y | Y | Y |
 | Intersection | Y | Y |  |  |
 | Difference | Y | Y |  |  |
 |    Enables full set expressions | Y | Y |  |  |
 | Set Op Result Type | Sketch | Sketch | Sketch | Sketch |
 | Merge different LgK | Y | Y | Y | Y |
Serialize Operations | To Byte Array | Y | Y | Y | Y |
 | To ProtoBuf |  |  |  |  |
Deserialize Operations | Heapify() | Y | Y | Y | Y |
 | Wrap() | Y | Y | Y |  |
 | WritableWrap | Y | Y | Y |  |
Languages | Java | Y | Y | Y | Y |
 | C++ | Y | Y | Y | Y |
 | Python | Y | Y | Y | Y |
 | Binary compatibility across Languages | Y | Y | Y | Y |
Other Operations and Modes | Jaccard Index | Y |  |  |  |
 | Off-Heap | Y | Y | Y |
 | Associative Columns |  | Y |  |  |
 | Generic Extensions  |  | Y |  |  |
 
 ____
 1. Space Accuracy Merit (Lower is better) = b * C^2 = b * K * RSE^2 
