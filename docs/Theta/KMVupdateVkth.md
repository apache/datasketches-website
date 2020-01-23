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
[Prev]({{site.docs_dir}}/Theta/KMVrejection.html)<br>


## The KMV Sketch, Update <i>V(k<sup>th</sup>)</i> Rule
Our final rules: 

If the cache is presented with a hash value that is less than <i>V(k<sup>th</sup>)</i> and <i>not</i> a duplicate, we insert the new value in order, remove what was the <i>V(k<sup>th</sup>)</i> value and replace it with whatever was next in order, which becomes the new <i>V(k<sup>th</sup>)</i> value.

If the cache has been presented with less than <i>k</i> values,
the estimate equals the current count of values in the cache. This is also known as <i>Exact Mode</i> (as opposed to <i>Estimation Mode</i>).

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/KMV5.png" alt="KMV5" />

We now have a complete <i>KMV</i> sketch, with some amazing properties:

* The sketch has a fixed upper bound size of <i>k</i> values.
* De-duplication is part of the algorithm.
* If the sketch has retained less than <i>k</i> values, the resulting estimate is exact and is just the count of the values in the cache.  Otherwise, the unbiased estimate of the unique values seen by the sketch is <i>(k-1)/V(k<sup>th</sup>)</i>[1].
* The <i>Relative Standard Error</i> or <i>RSE</i> of the estimate, for this simple KMV Sketch, is &le; <i>1/sqrt(k-2)</i>[1],
which is a constant and independent of <i>n</i>, the number of uniques presented to the sketch (See [Accuracy]({{site.docs_dir}}/Theta/ThetaAccuracy.html)).
For large enough values of <i>k</i> the error distribution is roughly Gaussian, which makes preditions of confidence intervals relatively straightforward.

To figure out what value of <i>k</i> is required, you must first determine what level of accuracy is required for your application.  The graph below can serve as a guide.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/RSEvsK.png" alt="RSEvsK" />

The RSE corresponds to +/- one Standard Deviation of the Gaussian distribution, which is equivalent to a confidence of 68%.  To obtain the Relative Error (RE, no longer "Standard") at 95% confidence and the same value of <i>k</i> you multiply the RSE value by two.  For example, reading from the chart, choosing <i>k</i> = 4096 corresponds to an RSE of +/- 1.6% with 68% confidence.  That same size sketch will have a Relative Error of +/- 3.2% with 95% confidence.

The values of <i>k</i> are shown as powers of 2 for a reason.  From an implementation point-of-view, choosing internal cache sizes that are powers of 2 improves performance and is simpler to implement.

Note that the sketch algorithms implemented in the DataSketches library are more sophisticated than the simple KMV sketch outlined here and the corresponding estimation equations are slightly different and the error bounds equations are much more complex. Don't worry, all the complexity is handled for you in the library. This will be discussed more extensively in other sections.


[1] For those interested in the mathematical proofs, 
<a href="https://www-sop.inria.fr/members/Frederic.Giroire/publis/Gi05.pdf">Giroire</a>
has a straightforward and easy-to-follow development.


[Prev]({{site.docs_dir}}/Theta/KMVrejection.html)<br>

