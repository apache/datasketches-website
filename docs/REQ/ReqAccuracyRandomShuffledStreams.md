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

# Accuracy with Random Shuffled Streams
This set of tests characterize the accuracy (or more precisely the rank error) of the ReqSketch using random shuffled streams.  The goal of this suite of tests is to understand how the rank error of the sketch behaves across all ranks.  All of these tests are run with the same stream length. The two major parameters that are varied are the sketch's *K*, which affects size and accuracy of the sketch, and the *HRA / LRA* parameter, which selects the region of highest accuracy is the high ranks close to 1.0 *High Ranks Accuracy (HRA)*, or the low ranks close to zero *Low Ranks Accuracy (LRA)*. 

These tests also confirm that the a priori prediction of the error bounds are reasonable and relatively conservative.  The computation of these bounds are based on empirical measurements derived from tests such as these and are subject to some tuning as we understand the sketch's error behavior over a wider selection of streams.

For those that are interested in the actual code that run these tests can examine the following links.
 
* [Code](https://github.com/apache/datasketches-characterization/blob/master/src/test/java/org/apache/datasketches/characterization/quantiles/ReqSketchAccuracyProfile.java): The code used to generate these characterization studies.
* [Config](https://github.com/apache/datasketches-characterization/blob/master/src/main/resources/quantiles/ReqSketchAccuracyJob.conf): The human readable and editable configuration file that instructs the above code with the specific properties used to run the test. These configuration properties are different for each of the following plots and summarized below with each plot.


## Test Design
* Stream Length (SL): 2^20
* Stream Values: Natural numbers, &#x2115;<sub>1</sub>, from 1 to SL, expressed as 32-bit floats.
* Y-axis: The absolute error of the sketch *getRank(value)* method.
* X-axis: The normalized rank [0.0, 1.0]
* Plot Points (PP): 100.  Equally spaced points along the X-axis starting at 0.0 and ending at 1.0. 
* Trial: 
	* Randomly Shuffles the input stream and presents it to the sketch. 
	* Execute *estRanks[] = getRanks(trueValues[])* from the sketch where the array of *trueValues[]* are the 100 equally spaced values along the x-axis that exactly correspond to the above Plot Points. The *estRanks[]* is the array of the sketch's estimates of the ranks of the given *trueValues*.
	* Computes the absolute error: *estRanks[i] - trueRanks[i]* for each Plot Point and submits this error to a corresponding array of standard qantile sketches, with one quantile sketch assigned to each Plot Point. 
* Trials: 
	* 2^12 trials are executed. This produces a distribution of error at each Plot Point held by the error quantile skeches. 
	* **Note:** With these tests randomization is due to two sources, the shuffling of the stream and the internal random process of the sketch itself.
	* Extract 7 quantiles from the error quantile sketches at each Plot Point.  These error quantiles correspond to the standard normal distribuion at the median, +/- 1SD, +/- 2SD and +/- 3SD, where SD stands for Standard Deviation from the mean.
* Plotting: 
	* Each of the error quantiles are connected by lines to form contours of the error distribution where the area between the +/- 1SD contours is the size of the 68% confidence interval; between the +/- 2SD coutours is the 95.4% confidence interval; and between the +/- 3SD contours is the 99.7% confidence interval.
	* In addition to the error contours. 6 dashed contours (with colors corresponding to the error contours) represent the a priori estimates of the error at each of the +/- standard deviations computed from the sketch's *getRankLowerBound(double, int)* and *getRankUpperBound(double, int)* methods.  


### Common Configuration for the following plots
* SL=2^20: StreamLength
* Eq Spaced: Equally spaced Plot Points (PP)
* PP=100: Number of plot points on the x-axis
* LgT=12: Number of trials = 2^12
* Shuffled: Random shuffle of the input stream for each trial

# Results

## Plot 1: K=12, HRA, LT 
### Specific Configuration
* K=12: the sketch sizing & accuracy parameter
* HRA: High Rank Accuracy
* Crit=LT: Comparison criterion: LT = Less-Than

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK12SL20T12_LT_Sh.png" alt="/req/ReqErrEqHraK50SL20T12_LT_Sh.png" />

* Retained Items: 2015
* Serialization Bytes: 8676
* Run Time: 14:01
* Unique Error Value Count Per Plot Point: Min=459, Max=3055


## Plot 2: K=12, LRA, LE
### Specific Configuration
* K=12: the sketch sizing & accuracy parameter
* LRA: Low Rank Accuracy
* Crit=LE: Comparison criterion: LE = Less-Than or Equal

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqLraK12SL20T12_LE_Sh.png" alt="/req/ReqErrEqLraK50SL20T12_LE_Sh.png" />

* Retained Items: 2015
* Serialization Bytes: 8676
* Run Time: 14:52
* Unique Error Value Count Per Plot Point: Min=447, Max=3106

## Plot 1 and 2, Final Compactor Profile & Size
Because these two tests are run with the same exact sketch configuration and the same input stream length they both have the same final compactor profile, retained items and Serialization Bytes.

| Lg Wt | Items | Nominal Size | Section Size | Num Sections | Num Compactions |
|:-----:|:-----:|:------------:|:------------:|:------------:|:---------------:|
|  0 | 192 | 192 |  4 | 24 | 85812 |
|  1 | 188 | 192 |  4 | 24 | 36251 |
|  2 | 184 | 192 |  4 | 24 | 16596 |
|  3 | 205 | 192 |  4 | 24 |  7509 |
|  4 | 188 | 192 |  4 | 24 |  3185 |
|  5 | 140 | 144 |  6 | 12 |  1340 |
|  6 | 173 | 144 |  6 | 12 |   590 |
|  7 | 138 | 144 |  6 | 12 |   261 |
|  8 | 187 | 144 |  6 | 12 |   108 |
|  9 | 138 | 144 |  6 | 12 |    35 |
| 10 |  94 |  96 |  8 |  6 |    13 |
| 11 |  36 |  96 |  8 |  6 |     4 |
| 12 | 128 |  72 | 12 |  3 |     1 |
| 13 |  24 |  72 | 12 |  3 |     0 |

* Retained Items: 2015
* Serialization Bytes: 8676

## Plot 3: K=50, HRA, LT
### Specific Configuration
* K=50: the sketch sizing & accuracy parameter
* HRA: High Rank Accuracy
* Crit=LT: Comparison criterion: LT = Less-Than

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqHraK50SL20T12_LT_Sh.png" alt="/req/ReqErrEqHraK50SL20T12_LT_Sh.png" />

* Retained Items: 6903
* Serialization Bytes: 28144
* Run Time: 8:38
* Unique Error Value Count Per Plot Point: Min=156, Max=2465

## Plot 4: K=50, LRA, LE
### Specific Configuration
* K=50: the sketch sizing & accuracy parameter
* LRA: Low Rank Accuracy
* Crit=LE: Comparison criterion: LE = Less-Than or Equal

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqLraK50SL20T12_LE_Sh.png" alt="/req/ReqErrEqLraK50SL20T12_LE_Sh.png" />

* Retained Items: 6903
* Serialization Bytes: 28144
* Run Time: 27:20
* Unique Error Value Count Per Plot Point: Min=167, Max=2475

## Plot 4B: K=50, LRA, LE, Using Theoretical Bounds
* K=50: the sketch sizing & accuracy parameter
* LRA: Low Rank Accuracy
* Crit=LE: Comparison criterion: LE = Less-Than or Equal

<img class="doc-img-full" src="{{site.docs_img_dir}}/req/ReqErrEqLraLeK50SL20T12_PyRSE.png" alt="/req/ReqErrEqLraLeK50SL20T12_PyRSE.png" />

## Plot 3 and 4, Final Compactor Profile & Size
Because these two tests are run with the same exact sketch configuration and the same input stream length they both have the same final compactor profile, retained items and Serialization Bytes.

| Lg Wt | Items | Nominal Size | Section Size | Num Sections | Num Compactions |
|:-----:|:-----:|:------------:|:------------:|:------------:|:---------------:|
|  0 | 786 | 864 | 18 | 24 | 16288 |
|  1 | 919 | 864 | 18 | 24 |  6755 |
|  2 | 846 | 864 | 18 | 24 |  2979 |
|  3 | 531 | 576 | 24 | 12 |  1308 |
|  4 | 613 | 576 | 24 | 12 |   578 |
|  5 | 529 | 576 | 24 | 12 |   246 |
|  6 | 528 | 576 | 24 | 12 |   106 |
|  7 | 781 | 576 | 24 | 12 |    33 |
|  8 | 621 | 432 | 36 | 6  |    13 |
|  9 | 364 | 432 | 36 | 6  |     4 |
| 10 | 250 | 300 | 50 | 3  |     1 |
| 11 | 135 | 300 | 50 | 3  |     0 |

* Retained Items: 6903
* Serialization Bytes: 28144
