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
[Prev]({{site.docs_dir}}/Theta/KMVfirstEst.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVrejection.html)

## The KMV Sketch, Better Estimator, Size = <i>k</i>
Now lets choose <i>k = 3</i>, which means that we will keep the 3 smallest hash values that the cache has seen.  The fractional distance that these <i>k</i> values consume is simply the value of the k<sup>th</sup> hash value, or <i>V(k<sup>th</sup>)</i>, which in this example is 0.195. This is also known as the <i>k<sup>th</sup> Minimum Value</i> or <i>KMV</i>.  Since these measurements are relative to zero, a sketch constructed like this is also known as a <i>Bottom-k</i> sketch.  (It could well have been a <i>Top-k</i> sketch, but referencing to zero is just simpler.)

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/KMV3.png" alt="KMV3" />

We want not only a more accurate estimate, but one that is also <u><i>unbiased</i></u>.  I'm going to skip a few pages of calculus[1] and reveal that we only need to subtract one in the numerator to achieve that.  Our new, unbiased KMV estimator becomes

<img src="{{site.docs_img_dir}}/theta/Est2Formula.png" alt="Est2Formula" width="600" />

This is much closer to 10, but with such a small sample size we were also lucky. 

Note that with our new estimator based on the <i>k</i> minimum values in the cache we don't have to keep any hash values larger than <i>V(k<sup>th</sup>)</i>.  And, since <i>k</i> is a constant our cache will have a fixed upper bound size independent of how many hash values it has seen.

[1] For those interested in the mathematical proofs, 
<a href="https://www-sop.inria.fr/members/Frederic.Giroire/publis/Gi05.pdf">Giroire</a>
has a straightforward and easy-to-follow development.

[Prev]({{site.docs_dir}}/Theta/KMVfirstEst.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVrejection.html)

