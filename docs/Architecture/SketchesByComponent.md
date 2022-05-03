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
# Sketches by [Component Repository](https://github.com/apache?utf8=%E2%9C%93&q=datasketches)
 
The DataSketches Library is organized into the following repository groups:

## Java

### datasketches-java
This repository has the core-java sketching classes, which are leveraged by some of the other repositories.   
This repository has no external dependencies outside of the DataSketches/memory repository, Java and TestNG for unit tests. 
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

<b>High-level Repositories Structure</b>

Sketches-core Packages.        | Package Description
-------------------------------|---------------------
org.apache.datasketches             | Common functions and utilities
org.apache.datasketches.cpc         | New Unique Counting Sketch with better accuracy per size than HLL
org.apache.datasketches.fdt         | Frequent Distinct Tuples Sketch. 
org.apache.datasketches.frequencies | Frequent Item Sketches, for both longs and generics
org.apache.datasketches.hash        | The 128-bit MurmurHash3 and adaptors
org.apache.datasketches.hll         | Unique counting HLL sketches for both heap and off-heap.
org.apache.datasketches.hllmap      | The (HLL) Unique Count Map Sketch
org.apache.datasketches.kll         | Quantiles sketch with better accuracy per size than the standard quantiles sketch. Includes PMF, CDF functions, for floats, doubles. On-heap & off-heap.
org.apache.datasketches.quantiles   | Standard Quantiles sketch, plus PMF and CDF functions, for doubles and generics. On-heap & off-heap.
org.apache.datasketches.req         | Relative Error Quantiles (REQ) sketch, plus PMF and CDF functions for floats, on-heap. Extremely high accuracy for very high ranks (e.g., 99.999%ile), or very low ranks (e.g., .00001%ile.
org.apache.datasketches.sampling    | Weighted and uniform reservoir sampling with generics
org.apache.datasketches.theta       | Unique counting Theta Sketches for both on-heap & off-heap
org.apache.datasketches.tuple       | Tuple sketches for both primitives and generics
org.apache.datasketches.tuple.adouble | A Tuple sketch with a Summary of a single double
org.apache.datasketches.tuple.aninteger | A Tuple sketch with a Summary of a single integer
org.apache.datasketches.tuple.Strings | A Tuple sketch with a Summary of an array of Strings

### datasketches-memory
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Memory Packages                | Package Description
-------------------------------|---------------------
org.apache.datasketches.memory | Low level, high-performance Memory data-structure management primarily for off-heap. 


### datasketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Sketches-hive Packages               | Package Description
-------------------------------------|---------------------
org.apache.datasketches.hive.cpc          | Hive UDF and UDAFs for CPC sketches
org.apache.datasketches.hive.frequencies  | Hive UDF and UDAFs for Frequent Items sketches
org.apache.datasketches.hive.hll          | Hive UDF and UDAFs for HLL sketches
org.apache.datasketches.hive.kll          | Hive UDF and UDAFs for KLL sketches
org.apache.datasketches.hive.quantiles    | Hive UDF and UDAFs for Quantiles sketches
org.apache.datasketches.hive.theta        | Hive UDF and UDAFs for Theta sketches
org.apache.datasketches.hive.tuple        | Hive UDF and UDAFs for Tuple sketches

### datasketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Sketches-pig Packages              | Package Description
-----------------------------------|---------------------
org.apache.datasketches.pig.cpc         | Pig UDFs for CPC sketches
org.apache.datasketches.pig.frequencies | Pig UDFs for Frequent Items sketches
org.apache.datasketches.pig.hash        | Pig UDFs for MurmerHash3
org.apache.datasketches.pig.hll         | Pig UDFs for HLL sketches
org.apache.datasketches.pig.kll         | Pig UDFs for KLL sketches
org.apache.datasketches.pig.quantiles   | Pig UDFs for Quantiles sketches
org.apache.datasketches.pig.sampling.   | Pig UDFs for Sampling sketches
org.apache.datasketches.pig.theta       | Pig UDFs for Theta sketches
org.apache.datasketches.pig.tuple       | Pig UDFs for Tuple sketches


### datasketches-characterization
This relatively new repository is for Java and C++ code that we use to characterize the accuracy and speed performance of the sketches in 
the library and is constantly being updated.  Examples of the job command files used for various tests can be found in the src/main/resources directory. 
Some of these tests can run for hours depending on its configuration. This component is not formally released and code must be obtained from
the [GitHub site](https://github.com/apache/datasketches-characterization).

Characterization Packages                       | Package Description
------------------------------------------------|---------------------
org.apache.datasketches.characterization             | Common functions and utilities
org.apache.datasketches.characterization.concurrent  | Concurrent Theta Sketch
org.apache.datasketches.characterization.cpc         | Compressed Probabilistic Counting Sketch
org.apache.datasketches.characterization.fdt         | Frequent Distinct Tuples Sketch
org.apache.datasketches.characterization.frequencies | Frequent Items Sketches
org.apache.datasketches.characterization.hash        | Hash function performance
org.apache.datasketches.characterization.hll         | HyperLogLog Sketcch
org.apache.datasketches.characterization.memory      | Memory performance
org.apache.datasketches.characterization.quantiles   | Quantiles performance
org.apache.datasketches.characterization.theta       | Theta Sketch
org.apache.datasketches.characterization.uniquecount | Base Profiles for Unique Counting Sketches

### datasketches-server
This is a new repository for our experimental docker/container server that enables easy access to the core sketches in the library via HTTP.
This component is not formally released and code must be obtained from
the [GitHub site](https://github.com/apache/datasketches-server).

#### C++ Characterizations
* CPC
* Frequent Items
* HLL
* KLL
* Theta


### datasketches-vector
This component implements the [Frequent Directions Algorithm](/docs/Community/Research.html) [GLP16].  It is still experimental in that the theoretical work has not yet supplied a suitable measure of error for production work. It can be used as is, but it will not go through a formal Apache Release until we can find a way to provide better error properties.  It has a dependence on the Memory component.
This component is not formally released and code must be obtained from
the [GitHub site](https://github.com/apache/datasketches-vector).


## C++ and Python

### datasketches-cpp
This is the evolving C++ implementations of the same sketches that are available in Java. 
These implementations are *binary compatible* with their counterparts in Java.
In other words, a sketch created and stored in C++ can be opened and read in Java and visa-versa.
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

This site also has our [Python adaptors](https://github.com/apache/datasketches-cpp/tree/master/python) that basically wrap the C++ implementations, making the high performance C++ implementations available from Python.

### datasketches-postgresql
This site provides the postgres-specific adaptors that wrap the C++ implementations making
them available to the PostgreSQL database users. PostgreSQL users should download the PostgreSQL extension from [pgxn.org](https://pgxn.org/dist/datasketches/).  For examples refer to the README on the component site. This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).







