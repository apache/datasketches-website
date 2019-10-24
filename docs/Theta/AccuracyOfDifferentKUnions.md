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
## Unioning Sketches with Different values of \(k\)

<p>One of the benefits of the Theta Sketch algorithms is that they support the union of sketches that have been created with different values of \(k\) or <i>Nominal Entries</i>. More specifically, it is possible to create a Union operation with a \(k_U\) and then update the union with sketches created with different \(k_i\) that can be either larger or smaller than \(k_U\).</p>

<p>The interesting case, of course, is where \(k_U &gt; k_i\), and, it turns out that it is possible that the Relative Standard Error, \(\color{black}{RSE = 1/{\sqrt{k}}}\), of the resulting Union, can be improved, 
i.e., \(RSE_U &lt; min(RSE_i)\).</p>

<p>This is in contrast to the HLL algorithm, where unioning is only possible with the same \(k\) or with values that are powers-of-2 smaller. 
In this case the \(RSE_U = min(RSE_i)\).</p>

