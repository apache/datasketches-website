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
[Prev]({{site.docs_dir}}/Theta/KMVempty.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVbetterEst.html)

## The KMV Sketch, First Estimator, Size = 1
For this step we are going to cheat a little so that we can learn about estimation. We are going to cheat in that we are going to predetermine that our data source only has <span class="doc-math">n</span> = 10 unique values (so we don't really need a sketch to estimate what we already know).  We have loaded all 10 values into our ordered list.  As one can see, the values are roughly evenly distributed between zero and one so our hash transform is doing its job.

How many hash values do we have to retain to compute the estimate of <span class="doc-math">n</span>, 
<span class="doc-math">n&#770;</span>, accurately? As you might expect, the more samples we retain, the more accurate will be our estimate.

Suppose we kept only one value, so <i>k = 1</i>, and we chose the smallest hash value out of all the hash values in the set, which, in this case, is 0.008.  We could assume that since the hash values are random-uniform distributed that the separation between the hash values are roughly the same.  Let's label that separation between values as <i>d</i>.  Our first estimator could be just dividing 1.0 by <i>d</i>. Unfortunately, 1/0.008 is about 125, which is way larger than 10.  And as one can see, there is a lot of variation in the separation of each of the hash values.  If the hash values had been ordered differently, the smallest hash value could have been the separation between the 3rd and 4th values. In this case our first estimator, 1/0.191 would be about 5, which is too small.  

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/KMV2.png" alt="KMV2" />

Clearly, our first estimator, <i>1/d</i>, with a sample size of one is too noisy to be useful. However, what we do have is a simple formula for an estimate:

<img src="{{site.docs_img_dir}}/theta/Est1Formula.png" alt="Est1" width="450" />



[Prev]({{site.docs_dir}}/Theta/KMVempty.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVbetterEst.html)

