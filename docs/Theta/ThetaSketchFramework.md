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
## Theta Sketch Framework
Theta Sketches are a generalization of the well known <i>K<sup>th</sup> Minimum Value</i> (KMV)<sup>1,2</sup> 
sketches in that KMV sketches are a form of Theta Sketch, but not all Theta Sketches are KMV.


The <a href="{{site.docs_pdf_dir}}/ThetaSketchFramework.pdf">Theta Sketch Framework</a> (TSF) 
is a mathematical framework 
defined in a multi-stream setting that enables set expressions over these streams and encompasses many
different sketching algorithms. A rudimentary introduction to the mathematics of the simpler sketch algorithms is developed in 
<a href="{{site.docs_pdf_dir}}/SketchEquations.pdf">Sketch Equations</a>.

The TSF consists of the following components:

1. A data type <i>(&theta;,S)</i>, known as a <i>Theta Sketch</i>, where 0 &lt; <i>&theta;</i> &lt; 1 is a 
threshold, and <i>S</i>, the number of entries, is the set of all unique hashed stream items 0 &lt; <i>x</i> &lt; 1 
that are less than <i>&theta;</i>. 
2. A universal "combining function" <i>ThetaUnion</i> that takes as input a collection of <i>Theta Sketches</i> 
and returns a single <i>Theta Sketch</i> that is a <i>Union</i> of the input sketches. 
This combining function is extended to set <i>Intersections</i> and <i>Differences</i> as well.
3. A estimator function that takes as input a <i>Theta Sketch</i> and returns an estimate of the unique 
hashed stream items presented to all the input sketches.
  
The TSF enables this sketch library to encompass multiple sketching algorithms including the 
KMV sketch with a common API and greatly simplifies implementation of set 
expressions.

Note that in the KMV sketch the value <i>k</i> is overloaded with multiple roles:

1. <i>k</i> determines the RSE (accuracy) of the sketch
2. <i>k</i> determines the upper-bound size of the sketch
3. <i>k</i> is used as a constant in the estimator and RSE equations
4. <i>k</i> determines the <i>V(k<sup>th</sup>)</i> threshold, used to reject/accept hash values into the cache.

By unloading some of these roles, we will gain degrees of freedom to do some innovative things. 

Instead of having to track <i>V(k<sup>th</sup>)</i>, which is a member of the list of hash values, 
we are going to create a separate threshold variable and call it <i>theta (&theta;)</i>. 
This effectively decouples #3 and #4 above from <i>k</i>. When the sketch is empty <i>&theta;</i> = 1.0. 
After the sketch has filled with <i>k</i> minimum values <i>&theta;</i> is still 1.0. 
When the next incoming unique value must be inserted into the sketch the <i>(k+1)<sup>th</sup></i> 
minimum value, is assigned to <i>&theta;</i> and removed from the cache.<sup>3</sup>

Ultimately, it will be the size of <i>S</i>, <i>|S|</i>, that will determine the stored size of a 
sketch, which decouples #2 above from the value <i>k</i>. 
The <i>Nominal Entries</i> or <i>k</i> is a <i>user specified, configuration parameter</i>, 
which is used by the software to determine the target accuracy of the sketch and the maximum size of the sketch.

The unbiased estimate simplifies to \|S\|/<i>&theta;</i>, which is just the size of <i>S</i> divided by <i>&theta;</i>. 
We will discuss the RSE in a later section.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/ThetaSketch1.png" alt="ThetaSketch1" />

[1] Z. Bar-Yossef, T. Jayram, R. Kumar, D. Sivakumar, and L. Trevisan. Counting distinct elements in a data stream. In <i>Randomization and Approximation Techniques in Computer Science</i>, pages 1–10. Springer, 2002.

[2] See <a href="{{site.docs_dir}}/Theta/KMVempty.html">KMV Tutorial</a> and 
<a href="https://research.neustar.biz/2012/07/09/sketch-of-the-day-k-minimum-values/">Sketch of the Day: K-Minimum Values</a> 
for a brief tutorials on KMV Sketches.

[3] This is a limited "KMV perspective" on how <i>&theta;</i> gets assigned.  The attached paper 
<a href="{{site.docs_pdf_dir}}/ThetaSketchFramework.pdf">Theta Sketch Framework</a> 
presents multiple ways that <i>&theta;</i> can be assigned using the <i>Theta Choosing Function (TCF)</i>. 
Different sketch algorithms have different TCFs.  
