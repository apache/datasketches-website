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
# Sketches by [Component Repository](https://datasketches.apache.org/docs/Architecture/Components.html)
 
The DataSketches Library is organized into the following repository groups:

## Java Sketches

### datasketches-java
This repository has the core-java sketching classes, which are leveraged by some of the other repositories.   
This repository has no external dependencies outside of the DataSketches/memory repository, Java and TestNG for unit tests. 
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

<b>High-level Repositories Structure</b>

Packages (org.apache.datasketches.*)    | Description
----------------------------------------|---------------------
common          | Common functions and utilities
cpc             | New Unique Counting Sketch with better accuracy per size than HLL
fdt             | Frequent Distinct Tuples Sketch.
filters         | Bloomfilter, Quotientfilter, etc.
frequencies     | Frequent Item Sketches, for both longs and generics
hash            | The 128-bit MurmurHash3 and adaptors
hll             | Unique counting HLL sketches for both heap and off-heap.
hllmap          | The (HLL) Unique Count Map Sketch
kll             | Quantiles sketch with better accuracy per size than the standard quantiles sketch. Includes PMF, CDF functions, for floats, doubles. On-heap & off-heap.
partitions      | Special tools to enable large-scale partitioning using the quantiles sketches.
quantiles       | Standard Quantiles sketch, plus PMF and CDF functions, for doubles and generics. On-heap & off-heap.
quantilescommon | Common functions used by all the quantiles sketches.
req             | Relative Error Quantiles (REQ) sketch, plus PMF and CDF functions for floats, on-heap. Extremely high accuracy for very high ranks (e.g., 99.999%ile), or very low ranks (e.g., .00001%ile.
sampling        | Weighted and uniform reservoir sampling with generics
theta           | Unique counting Theta Sketches for both on-heap & off-heap
thetacommon     | Common functions used by all the Theta and Tuple sketches
tuple           | Tuple sketches for both primitives and generics
tuple.adouble   | A Tuple sketch with a Summary of a single double
tuple.arrayofdoubles | Dedicated implementation of a Tuple sketch with an array of doubles Summary.
tuple.aninteger | A Tuple sketch with a Summary of a single integer
tuple.Strings   | A Tuple sketch with a Summary of an array of Strings

## Java Platform Adaptors

### datasketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Packages (org.apache.datasketches.*) | Description
-------------------------------------|---------------------
common            | Common functions
hive.cpc          | Hive UDF and UDAFs for CPC sketches
hive.frequencies  | Hive UDF and UDAFs for Frequent Items sketches
hive.hll          | Hive UDF and UDAFs for HLL sketches
hive.kll          | Hive UDF and UDAFs for KLL sketches
hive.quantiles    | Hive UDF and UDAFs for Quantiles sketches
hive.theta        | Hive UDF and UDAFs for Theta sketches
hive.tuple        | Hive UDF and UDAFs for Tuple sketches

### datasketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Packages (org.apache.datasketches.*) | Description
-------------------------------------|---------------------
pig.cpc         | Pig UDFs for CPC sketches
pig.frequencies | Pig UDFs for Frequent Items sketches
pig.hash        | Pig UDFs for MurmerHash3
pig.hll         | Pig UDFs for HLL sketches
pig.kll         | Pig UDFs for KLL sketches
pig.quantiles   | Pig UDFs for Quantiles sketches
pig.sampling.   | Pig UDFs for Sampling sketches
pig.theta       | Pig UDFs for Theta sketches
pig.tuple       | Pig UDFs for Tuple sketches

## C++ Sketches

### datasketches-cpp
This is the evolving C++ implementations of the same sketches that are available in Java. 
These implementations are *binary compatible* with their counterparts in Java.
In other words, a sketch created and serialized in C++ can be opened and read in Java and visa-versa.
This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Directory         | Description
------------------|---------------------
common            | Common functions
count             | Count-Min Sketch
cpc               | CPC Sketch
density           | Density Sketch
fi                | Frequent Items Sketch
hll               | HLL Sketch
kll               | KLL Sketch
quantiles         | Classic Quantiles Sketch
req               | REQ Sketch
sampling          | Sampling sketches
tdigest           | t-Digest Sketch
theta             | Theta sketches
tuple             | Tuple sketches

## C++ Platform Adaptors

This site also has our [Python adaptors](https://github.com/apache/datasketches-cpp/tree/master/python) that basically wrap the C++ implementations, making the high performance C++ implementations available from Python.

### datasketches-postgresql
This site provides the postgres-specific adaptors that wrap the C++ implementations making
them available to the PostgreSQL database users. PostgreSQL users should download the PostgreSQL extension from [pgxn.org](https://pgxn.org/dist/datasketches/).  For examples refer to the README on the component site. This code is versioned and the latest release can be obtained from
[Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).

Files (src/*)      | Description
-----------------------|---------------------
aod_sketch_c_adapter.h              | Tuple Array-Of-Doubles Sketch
cpc_sketch_c_adapter.h              | CPC Sketch
frequent_strings_sketch_c_adapter.h | Frequent Strings Sketch
hll_sketch_c_adapter.h              | HLL Sketch
kll_double_sketch_c_adapter.h       | KLL Doubles Sketch
kll_float_sketch_c_adapter.h        | KLL Floats Sketch
quantiles_double_sketch_c_adapter.h | Classic Doubles Quantiles Sketch
req_float_sketch_c_adapter.h        | REQ Floats Sketch
theta_sketch_c_adapter.h            | Theta Sketch

## Python Sketches

### datasketches-python
Files (src/*)      | Description
-----------------------|---------------------
count_wrapper.cpp      | Count-Min Sketch
cpc_wrapper.cpp        | CPC Sketch
density_wrapper.cpp    | Density Sketch
ebpps_wrapper.cpp      | EB-PPS Sampling Sketch
fi_wrapper.cpp         | Frequent Items Sketch
hll_wrapper.cpp        | HLL Sketch
kll_wrapper.cpp        | KLL Sketch
quantiles_wrapper.cpp  | Classic Quantiles Sketch
req_wrapper.cpp        | REQ Sketch
theta_wrapper.cpp      | Theta sketches
tuple_wrapper.cpp      | Tuple sketches
vector_of_kll.cpp      | KLL Vector
vo_wrapper.cpp         | VarOpt Sketch

## Other

### datasketches-server
This is a new experimental repository for our experimental docker/container server that enables easy access to the core sketches in the library via HTTP.
This component is not formally released and code must be obtained from
the [GitHub site](https://github.com/apache/datasketches-server).


### datasketches-vector
This experimental component implements the [Frequent Directions Algorithm](/docs/Community/Research.html) [GLP16].  It is still experimental in that the theoretical work has not yet supplied a suitable measure of error for production work. It can be used as is, but it will not go through a formal Apache Release until we can find a way to provide better error properties.  It has a dependence on the Memory component.
This component is not formally released and code must be obtained from
the [GitHub site](https://github.com/apache/datasketches-vector).
