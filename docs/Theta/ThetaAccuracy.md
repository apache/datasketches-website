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
## Basic Theta Sketch Accuracy

Sketch accuracy is usually measured in terms of Relative Error (<i>RE = Measured/Truth -1</i>).
Sketches are stochastic processes and the estimates produced are random variables that have a 
probability distribution that is close to the familiar Gaussian, which looks like the following.

<img class="doc-img-half" src="{{site.docs_img_dir}}/theta/Normal2.png" alt="Normal2" />

The sketch estimator algorithm examines the internal state of the sketch and returns an estimate 
of the mean of the probability distribution that includes the actual value. 
When the sketch contains more than a hundred or so values, we can assume that the shape is 
pretty close to Gaussian due to the Central Limit Theorem. 
It is important to understand that the sketch has no idea what the true value is; 
it only knows the internal state of the sketch. 

From the mathematical theory of these sketches 
(see <a href="{{site.docs_pdf_dir}}/SketchEquations.pdf">Sketch Equations</a> and 
<a href="{{site.docs_pdf_dir}}/ThetaSketchFramework.pdf">Theta Sketch Framework</a>) we know:

* The estimate is unbiased.  If you were to feed the same data into the sketch using 
<i>T</i> different hash functions, the average of all <i>T</i> trials will converge on the true answer.
* The variance of the estimate across all <i>T</i> trials is &lt; <i>est<sup>2</sup>/(k-1)</i>, 
where <i>k</i> is the configured size of the sketch. 

Dividing the variance by <i>est<sup>2</sup></i> and taking the square root normalizes the error to 
&lt; <i><span style="white-space: nowrap">1/&radic;<span style="text-decoration:overline;">&nbsp;k - 1&nbsp;</span></span></i>, 
which is called the <i>Relative Standard Error</i> or RSE.  This corresponds to one standard 
deviation stated as a fraction between zero and one, which can be translated to a percent error.
Because <i>k</i> is a constant, the bounds of <i>Relative Error</i> of a sketch is constant. 

The area under the curve of the Standard Normal (Gaussian) Distribution is defined to be 1.0.
The fractional area between two points on the X-axis is referred to as the <i>confidence level</i>. 
Thus, the confidence level (the fractional area) between +1 RSE (+1 SD) and -1 RSE (-1 SD) is 68.27%. 
Similarly, the confidence level between +2 RSE (+2 SD) and -2 RSE (-2 SD) is 95.4%. 

