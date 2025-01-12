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
## Contents
<!-- TOC -->
* [CPC Sketches](#cpc-sketches)
* [CPC Sketch Performance](https://datasketches.apache.org/docs/CPC/CpcPerformance.html)
* CPC Examples
    * [CPC Sketch Java Example](https://datasketches.apache.org/docs/CPC/CpcJavaExample.html)
    * [CPC Sketch C++ Example](https://datasketches.apache.org/docs/CPC/CpcCppExample.html)
    * [CPC Sketch Pig UDFs](https://datasketches.apache.org/docs/CPC/CpcPigExample.html)
    * [CPC Sketch Hive UDFs](https://datasketches.apache.org/docs/CPC/CpcHiveExample.html) 
<!-- TOC -->

<a id="cpc-sketches"></a>
## Compressed Probability Counting (CPC) Sketches[^1]
The cpc package contains implementations of Kevin J. Lang's CPC sketch[^1].
The stored CPC sketch can consume about 40% less space than an HLL sketch of comparable accuracy.
Nonetheless, the HLL and CPC sketches have been intentially designed to offer different tradeoffs so that, in fact, they complement each other in many ways.

* The CPC sketch has better accuracy for a given stored size then HLL
* HLL has faster serialization and deserialization times than CPC.

Similar to the HLL sketch, the primary use-case for the CPC sketch is for counting distinct values as a stream, and then merging multiple sketches together for a total distinct count. 

Neither HLL nor CPC sketches provide means for set intersections or set differences.  If you anticipate your application might require this capability you are better off using the Theta family of sketches.







________________________


[^1]: Kevin J Lang. Back to the future: an even more nearly optimal cardinality estimation algorithm. arXiv preprint https://arxiv.org/abs/1708.06839, 2017.
