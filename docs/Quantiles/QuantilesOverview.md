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
# Introduction to the 3 Quantiles Sketches

## Quantile-type sketches in the library


This is an overview of the three types of quantiles sketches in the library. Each of these quantile types may have one or more specific implementtaions. 

The mathematical error bounds of all the quantile sketches is specified with respect to rank and not with respect to quantiles.  In other words, the difference between the rank upper bound and the rank lower bound is the confidence interval and can be expressed as a percent of the overall rank distribution (which is 1.0) and is the mathematically derived error for a specific configuration of the sketch.  

Although the quantile upper bound and quantile lower bounds can be approximately computed from the rank upper bound and rank lower bound, and the difference between the quantile bounds is also an approximate confidence interval, the size of the quantile confidence interval may not be meaningful and is not constrained by the defined error of the sketch.

These sketches have many parallel methods. Please refer to the individual Javadocs for more information. 

### The REQ Sketch

* Java 
	* Release 2.0.0, 12 Feb 2021
	* Repo: <https://github.com/apache/datasketches-java>
	* Package: org.apache.datasketches.req
* C++, Python
	* Release 2.2.0, Soon
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: req
* Key Features (both Java & C++)
	* Accuracy %: a function of *K* and **relative** with respect to rank. The user can select either High Rank Accuracy (HRA) or Low Rank Accuracy (LRA). This enables extremely high accuracy for the ends of the rank domain. E.g., 99.999%ile quantiles.
	* User selectable comparison QuantileSearchCriteria: 
		* Exclusive, which is compatible with the KLL and older Quantiles Sketch
		* Inclusive, a common definition in some of the theoretical literature.
	* Java: Dedicated *float* implementation.
	* C++: Template implementation for arbitrary comparible types.
	* Python: Dedicated *float* and *integer* implementations.

### The KLL Sketch

* Java
	* Release 0.11.0, 15 Mar 2018
	* Repo: <https://github.com/apache/datasketches-java>:
	* Package: org.apache.datasketches.kll
* C++, Python
	* Release 1.0.0, 17 Sep 2019
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: kll
* Key Features (both Java & C++)
	* User selectable comparison QuantileSearchCriteria: 
		* Exclusive, which is compatible with the KLL and older Quantiles Sketch
		* Inclusive, a common definition in some of the theoretical literature.
	* Accuracy %: a function of *K* and independent of rank. 
	* Near optimal accuracy per sketch size compared to other constant accuracy quantiles sketches. 
	* Java: Dedicated *float* and *double* implementations.
	* C++: Template implementation for arbitrary comparible types.
	* Python: Dedicated *float* and *integer* implementations


### The Quantiles Sketch

* Java
	* Release 0.3.0, 25 Jan 2016
	* Repo: <https://github.com/apache/datasketches-java>
	* Package: org.apache.datasketches.quantiles
* C++, Python
	* Release 1.0.0, 17 Sep 2019
	* Repo: <https://github.com/apache/datasketches-cpp>
	* Directory: 
* Key Features (both Java & C++)
    * User selectable comparison QuantileSearchCriteria: 
		* Exclusive, which is compatible with the KLL and older Quantiles Sketch
		* Inclusive, a common definition in some of the theoretical literature. 
	* Accuracy %: a function of *K* and independent of rank. 
	* Dedicated *double* implentation.
	* java generic implementation for arbitrary comparible types.
	* The dedicated *double* implementation can be configured for off-heap operation.
	





