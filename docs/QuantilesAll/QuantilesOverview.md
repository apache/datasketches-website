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
# Introduction to the Quantile Sketches

This is an overview of the quantiles sketches in the library. Each of these quantile types may have one or more specific implementaions and some variation in API depending on the language. Three of the quantile sketches have mathematically provable error bounds while the fourth is an empirical algorithm.

The three sketches with mathematically provable error bounds are:

* The Classic quantile sketch family
* The KLL quantile sketch family
* The REQ quantile sketch

The one empirical quantile sketch is the T-Digest sketch.

The mathematical error bounds of the Classic, KLL and REQ sketches are specified with respect to rank and not with respect to quantiles.

The T-Digest is empirical and has no mathematical basis for estimating its error and its results are dependent on the input data. However, for many common data distributions, it can produce excellent results. Please refer to the spcific documentation about the T-Digest sketch.

For the Classic and KLL sketches, the difference between the rank upper bound and the rank lower bound is a 99% confidence interval and is an additive constant for all normalized ranks between 0.0 and 1.0. The specific error is a function of the parameter <i>K</i> of the sketch and can be derived from the sketch.  For example, if the rank error for a given K is 1%, then the error of a result rank of .01 is +/- .01 with a 99% confidence; the error of a result rank of .99 is +/- .01 with a 99% confidence.

The REQ sketch is special in that its error is also relative to the actual result rank (thus its name: Relative Error Quantiles). It was designed to provide very high rank accuacy for either the high end of the range of ranks (close to 1.0) or, based on the user's choice, the low end of ranks (close to 0.0). Please refer to the spcific documentation about the REQ sketch.

Although upper and lower quantile bounds can be approximately computed from the upper and lower rank bounds, and the difference between the quantile bounds is also an approximate confidence interval, the size of the quantile confidence interval may not be meaningful and is not constrained by the defined rank error of the sketch.

For example, a 1% rank error at a result rank of .50 corresponds to a rank range of .49 to .51. However, in the quantile domain, there could be step function in the values such that the difference in the quantiles corresponding to ranks .49 and .51 are as large as the the entire dynamic range of all quantiles. It is possible to retrieve the upper and lower quantile bounds at a specific rank, but the difference between these quantile bounds may not be meaningful.

These sketches have many parallel methods. Please refer to the sketch API documentation by language for more information.

### Classic Quantiles Sketch
* Java
	* Repo: <https://github.com/apache/datasketches-java>
	* Package: [org.apache.datasketches.quantiles](https://github.com/apache/datasketches-java/tree/master/src/main/java/org/apache/datasketches/quantiles)
	* Dedicated *double* and generic *item* implentations for arbitrary comparable types.
		* The *double* implementation can be configured for off-heap operation.
* C++
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: [quantiles](https://github.com/apache/datasketches-cpp/tree/master/quantiles)
	* Template implementation for arbitrary comparable types.
* Python
    * Repo: <https://github.com/apache/datasketches-python>
	* Dedicated *float*, *double*, *integer* and arbitrary Python object implementations
* Key Features (Java, C++ and Python)
	* Accuracy %: a function of *K* and independent of rank.
    * User selectable search criteria QuantileSearchCriteria:
		* Inclusive, a common definition in some of the theoretical literature (default).
		* Exclusive, which is compatible with older Quantiles Sketch.

### KLL Sketch
* Java
	* Repo: <https://github.com/apache/datasketches-java>
	* Package: [org.apache.datasketches.kll](https://github.com/apache/datasketches-java/tree/master/src/main/java/org/apache/datasketches/kll)
	* Dedicated *float*, *double*, *long*, and generic *item* implementations.
		* The *float*, *double*, and *long* implementations can be configured for off-heap operation.
* C++
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: [kll](https://github.com/apache/datasketches-cpp/tree/master/kll)
	* Template implementation for arbitrary comparable types.
* Python
    * Repo: <https://github.com/apache/datasketches-python>
	* Dedicated *float*, *double*, *integer* and arbitrary Python object implementations
* Key Features (Java, C++ and Python)
	* Accuracy %: a function of *K* and independent of rank.
	* User selectable comparison QuantileSearchCriteria: 
		* Inclusive, a common definition in some of the theoretical literature (default).
		* Exclusive, which is compatible with older Quantiles Sketch.
	* Near optimal accuracy per sketch size compared to other constant accuracy quantiles sketches.

### REQ Sketch
* Java 
	* Repo: <https://github.com/apache/datasketches-java>
	* Package: [org.apache.datasketches.req](https://github.com/apache/datasketches-java/tree/master/src/main/java/org/apache/datasketches/req)
	* Dedicated *float* implementation.
* C++
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: [req](https://github.com/apache/datasketches-cpp/tree/master/req)
	* Template implementation for arbitrary comparable types.
* Python
    * Repo: <https://github.com/apache/datasketches-python>
	* Dedicated *float*, *integer* and arbitrary Python object implementations
* Key Features (Java, C++ and Python)
	* Accuracy %: a function of *K* and **relative** with respect to rank. The user can select either High Rank Accuracy (HRA) or Low Rank Accuracy (LRA). This enables extremely high accuracy for the ends of the rank domain. E.g., 99.999%ile quantiles.
	* User selectable comparison QuantileSearchCriteria:
		* Inclusive, a common definition in some of the theoretical literature (default).
		* Exclusive, which is compatible with older Quantiles Sketch.

### T-Digest
* Java 
	* Repo: <https://github.com/apache/datasketches-java>
	* Package: [org.apache.datasketches.req](https://github.com/apache/datasketches-java/tree/master/src/main/java/org/apache/datasketches/tdigest)
	* Dedicated *double* implementation.
* C++
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: [req](https://github.com/apache/datasketches-cpp/tree/master/tdigest)
	* Template implementation for *float* and *double* types.
* Python
    * Repo: <https://github.com/apache/datasketches-python>
	* Dedicated *float* and *double* implementations
* Key Features (Java, C++ and Python)
	* Works on numeric (floating point) types only.
	* Accuracy: a function of *K* and **relative** with respect to rank. Prioritizes both high rank accuracy and low rank accuracy with lower accuracy in the middle.
