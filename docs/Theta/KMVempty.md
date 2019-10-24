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
[Prev]({{site.docs_dir}}/Theta/InverseEstimate.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVfirstEst.html)

## The KMV Empty Sketch
To explain how a simple sketch works, let us start with the well-known <i>k Minimum Value</i> or <i>KMV</i> sketch in its empty state. 

Our objectives are as follows:

* Estimate the number of unique identifiers in the entire Big Data in a single pass through all the data.
* Retain no more than <i>k</i> values in the sketch at any one time.

In the diagram on the left we have a source of data that could be stored data or a live stream of data.  We can imagine that this data consists of many millions of events or rows of data, where each event could consist of many columns of information about the events:  dimensional information, various counters or metrics, and identifiers that uniquely identify a device or user that was the source of the event. There are possibly millions of different identifiers and the same identifiers can appear many times throughout the data. For our purposes here, we will only focus on the identifiers.    

In the middle of the diagram we have a hash function whose job is to transform each identifier encountered into a pseudo-random fractional value between zero and one.

On the right we have a cache that maintains an ordered list of the hash values retained by the sketch. To the right of this cache we will list the rules that the sketch must follow to achieve our objectives.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/KMV1.png" alt="KMV1" />

[Prev]({{site.docs_dir}}/Theta/InverseEstimate.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVfirstEst.html)
