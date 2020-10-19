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
# ReqSketch Accuracy with Adversarial Streams

This set of tests characterize the accuracy (or more precisely the rank error) of the ReqSketch using specifically selected adversarial streams.  The goal of this suite of tests is to understand how the rank error of the sketch behaves across all ranks with these specific stream patterns.  All of these tests are run with the same configuration except for the choice of the adversarial stream pattern.

The design of these tests is quite different from the tests for the *Random Shuffled Streams*.  Here, each test has one pattern and running multiple trials on the same pattern will not produce a nice distribution of error that we can easily analyze. We would like to capture the ranks where the pattern creates the largest error. These aberrant ranks could occur anywhere in the stream.  Instead of choosing 100 plot points where the error is exclusively measured, we want to measure the error at every value of the stream. However, a stream length of 2^20 presents a practical plotting problem of plotting and visualizing 2^20 error values. 

In this case we collect the statistics of all the errors in 100 contiguous intervals of the stream. For a stream length of 2^20, each interval consists of about ten thousand values.  The errors from these 10K values are fed into a standard quantile sketch as before, and we extract 3 statical quantile points, -3SD, median and +3SD, and plot those 3 values at each of the 100 plot points. 

As you can see, some of these patterns challenge our current a priori calculation of the error bounds, which means we may need to adjust them somewhat. If we do, these plots will be regenerated. 

For those that are interested in the actual code that run these tests can examine the following links.
 
* [Code](https://github.com/apache/incubator-datasketches-characterization/blob/master/src/test/java/org/apache/datasketches/characterization/quantiles/ReqSketchAccuracyProfile2.java): The code used to generate these characterization studies.
* [Config](https://github.com/apache/incubator-datasketches-characterization/blob/master/src/main/resources/quantiles/ReqSketchAccuracy2Job.conf): The human readable and editable configuration file that instructs the above code with the specific properties used to run the test. These configuration properties are different for each of the following plots and summarized below with each plot.

## Test Design
* Stream Length (SL): 2^20
* Stream Values: Natural numbers, &#x2115;<sub>1</sub>, from 1 to SL, expressed as 32-bit floats.
* Y-axis: The absolute error of the sketch *getRank(value)* method.
* X-axis: The normalized rank [0.0, 1.0]
* Plot Points (PP): 100.  Equally spaced points along the X-axis starting at *1.0/SL* and ending at 1.0. 
* Trial:
	* The stream is generated according to the chosen adversarial pattern.
	* At each Plot Point, we compute the rank errors of all ~10K points from the preceding PP to the current PP.
	* These 10K error values are fed into an error quantile sketch associated with the current PP.
	* 3 quantile values (-3sd, Median, +3sd) are extracted from each error sketch. These error quantiles correspond to the standard normal distribuion at the median, and +/- 3SD, where SD stands for Standard Deviation from the mean.
* Plotting:
	* Each of the error quantiles are connected by lines to form contours of the error distribution where the area between the +/- 3SD contours is the 99.7% confidence interval.
	* In addition to the error contours. 6 dashed contours (with colors corresponding to the error contours) represent the a priori estimates of the error at each of the +/- standard deviations computed from the sketch's *getRankLowerBound(double, int)* and *getRankUpperBound(double, int)* methods.

## Specific Configurations
### Common Configuration for the following plots
* K=50: the sketch sizing & accuracy parameter
* HRA: High Rank Accuracy
* Crit=LT: Comparison criterion: LT = Less-Than
* SL=2^20: StreamLength
* Eq Spaced: Equally spaced Plot Points (PP)
* PP=100: Number of plot points on the x-axis


### Plot 1 Adversarial Pattern: Sorted

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/SortedPattern.png" alt="/req/SortedPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_Sorted.png" alt="/req/ReqErrEqHraK50SL20T0_LT_Sorted.png" />

### Plot 2 Adversarial Pattern: Reversed

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/ReversedPattern.png" alt="/req/ReversedPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_Reversed.png" alt="/req/ReqErrEqHraK50SL20T0_LT_Reversed.png" />

### Plot 3 Adversarial Pattern: Random

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/RandomPattern.png" alt="/req/RandomPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_Random.png" alt="/req/ReqErrEqHraK50SL20T0_LT_Random.png" />

### Plot 4 Adversarial Pattern: Zoomin

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/ZoominPattern.png" alt="/req/ZoominPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_Zoomin.png" alt="/req/ReqErrEqHraK50SL20T0_LT_Zoomin.png" />

### Plot 5 Adversarial Pattern: Zoomout

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/ZoomoutPattern.png" alt="/req/ZoomoutPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_Zoomout.png" alt="/req/ReqErrEqHraK50SL20T0_LT_Zoomout.png" />

### Plot 6 Adversarial Pattern: Sqrt

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/SqrtPattern.png" alt="/req/SqrtPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_Sqrt.png" alt="/req/ReqErrEqHraK50SL20T0_LT_Sqrt.png" />

### Plot 6 Adversarial Pattern: FlipFlop

<img class="doc-img-qtr" src="{{site.docs_img_dir}}/req/FlipFlopPattern.png" alt="/req/FlipFlopPattern.png" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T0_LT_FlipFlop.png" alt="/req/ReqErrEqHraK50SL20T0_LT_FlipFlop.png" />