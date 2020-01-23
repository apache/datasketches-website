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
## Compressed Probability Counting (CPC) Sketch<sup>1</sup>
The cpc package contains implementations of Kevin J. Lang's CPC sketch (footnote).
The stored CPC sketch can consume about 40% less space than an HLL sketch of comparable accuracy.
Nonetheless, the HLL and CPC sketches have been intentially designed to offer different tradeoffs so that, in fact, they complement each other in many ways.

Similar to the HLL sketch, the primary use-case for the CPC sketch is for counting distinct values as a stream, and then merging multiple sketches together for a total distinct count. 

Neither HLL nor CPC sketches provide means for set intersecctions or set differences.  If you anticipate your application might require this capability you are better off using the Theta family of sketches.







________________________


<sup>1</sup><small>
Kevin J Lang. Back to the future: an even more nearly optimal cardinality estimation algorithm. arXiv preprint https://arxiv.org/abs/1708.06839, 2017.
</small>
