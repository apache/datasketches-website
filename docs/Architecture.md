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
# Architecture

The DataSketches Library is organized into the following repository groups:

## Java

### incubator-datasketches-java
This repository has the core-java sketching classes, which are leveraged by some of the other repositories.   
This repository has no external dependencies outside of the DataSketches/memory repository, Java and TestNG for unit tests. 
This code is versioned and the latest release can be obtained from
<a href="https://www.apache.org/dyn/closer.cgi?path=/incubator/datasketches/java">incubator-datasketches-java<a/>.

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
org.apache.datasketches.kll         | New quantiles sketch with better accuracy per size than the standard quantiles sketch.
org.apache.datasketches.quantiles   | Sketches for quantiles, PMF and CDF functions, both doubles and generics and for heap and off-heap.
org.apache.datasketches.sampling    | Weighted and uniform reservoir sampling with generics
org.apache.datasketches.theta       | Unique counting Theta Sketches for both heap and off-heap
org.apache.datasketches.tuple       | Tuple sketches for both primitives and generics
org.apache.datasketches.tuple.adouble | A Tuple sketch with a Summary of a single double
org.apache.datasketches.tuple.aninteger | A Tuple sketch with a Summary of a single integer
org.apache.datasketches.tuple.Strings | A Tuple sketch with a Summary of an array of Strings

### incubator-datasketches-memory
This code is versioned and the latest release can be obtained from
<a href="https://www.apache.org/dyn/closer.cgi?path=/incubator/datasketches/memory">incubator-datasketches-memory<a/>.

Memory Packages                | Package Description
-------------------------------|---------------------
org.apache.datasketches.memory               | Low level, high-performance Memory data-structure management primarily for off-heap. 


### incubator-datasketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://www.apache.org/dyn/closer.cgi?path=/incubator/datasketches/hive">incubator-datasketches-hive<a/>.

Sketches-hive Packages               | Package Description
-------------------------------------|---------------------
org.apache.datasketches.hive.cpc          | Hive UDF and UDAFs for CPC sketches
org.apache.datasketches.hive.frequencies  | Hive UDF and UDAFs for Frequent Items sketches
org.apache.datasketches.hive.hll          | Hive UDF and UDAFs for HLL sketches
org.apache.datasketches.hive.kll          | Hive UDF and UDAFs for KLL sketches
org.apache.datasketches.hive.quantiles    | Hive UDF and UDAFs for Quantiles sketches
org.apache.datasketches.hive.theta        | Hive UDF and UDAFs for Theta sketches
org.apache.datasketches.hive.tuple        | Hive UDF and UDAFs for Tuple sketches

### incubator-datasketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://www.apache.org/dyn/closer.cgi?path=/incubator/datasketches/pig">incubator-datasketches-pig<a/>.

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



### incubator-datasketches-characterization
This relatively new repository is for code that we use to characterize the accuracy and speed performance of the sketches in 
the library and is constantly being updated.  Examples of the job command files used for various tests can be found in the src/main/resources directory.
Some of these tests can run for hours depending on its configuration.

Characterization Packages                       | Package Description
------------------------------------------------|---------------------
org.apache.datasketches.characterization             | Common functions and utilities
org.apache.datasketches.characterization.hash        | Hash function performance
org.apache.datasketches.characterization.memory      | Memory performance
org.apache.datasketches.characterization.quantiles.  | Quantiles performance
org.apache.datasketches.characterization.uniquecount | Performance of Theta and HLL sketches

### incubator-datasketches-vector
This is a new repository dedicated to sketches for vector and matrix operations. It is still somewhat experimental.


## C++ and Python

### incubator-datasketches-cpp
This is the evolving C++ implementations of the same sketches that are available in Java. 
These implementations are *binary compatible* with their counterparts in Java.
In other words, a sketch created and stored in C++ can be opened and read in Java and visa-versa.

This site also has our Python adaptors that basically wrap the C++ implementations, 
making the high performance C++ implementations available from Python.

### incubator-datasketches-postgres
This site provides the postgres-specific adaptors that wrap the C++ implementations making
them available to the Postgres database users.


## Web Site

### incubator-datasketches-website (was DataSketches.github.io) 
This is the DataSketches web site source, and is constantly being updated with new material 
and to be current with the GitHub master.
This site is not versioned.

## Command-Line Tool
These repositories provide a command-line tool that provides access to the following sketches:
- Frequent Items
- HLL
- Quantiles
- Reservoir Sampling
- Theta Sketches
- VarOpt Sampling

This tool can be installed from Homebrew.

### sketches-cmd

### homebrew-sketches

### homebrew-sketches-cmd


## Deprecated sites
The code in these sites are no longer maintained and will eventually be removed.

### sketches-android
This is a new repository dedicated to sketches designed to be run in a mobile client, such as a cell phone. 
It is still in development and should be considered experimental.

### experimental
This repository is an experimental staging area for code that will eventually end up in another 
repository. This code is not versioned.


### sketches-misc
Demos, command-line access, characterization testing and other code not related to production 
deployment.

This code is offered "as is" and primarily as a reference so that users can understand how some of 
the performance characterization plots were obtained. This code has few unit tests, if any, 
and was never intended for production use. 
Nonetheless, some folks have found it useful. If you find it useful, go for it. 
This code is not versioned.
    
Sketches-misc Packages             | Package Description
-----------------------------------|---------------------
org.apache.datasketches                 | Utility functions used by the sketches-misc packages
org.apache.datasketches.cmd             | Support for Command Line functions **Being Redesigned**
org.apache.datasketches.demo            | Simple demo for brute-force vs Theta and HLL sketches **Will be superceded by Command Line functions**
org.apache.datasketches.quantiles       | Utility for computing & printing space table for Quantiles Sketches (only in the test branch)
org.apache.datasketches.sampling        | Benchmarks and Entropy testing for sampling sketches

### characterization-cpp
This is the parallel characterization repository with a parallel objective to the Java characterization repository.

### experimental-cpp
This repository is an experimental staging area for C++ code that will eventually end up in another 
repository.  
