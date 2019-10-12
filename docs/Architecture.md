---
layout: doc_page
---

# Architecture

The DataSketches Library is organized into the following repository groups:

## Java

### sketches-core
This repository has the core sketching classes, which are leveraged by some of the other repositories.   
This repository has no external dependencies outside of the DataSketches/memory repository, Java and TestNG for unit tests. 
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

<b>High-level Repositories Structure</b>

Sketches-core Packages.        | Package Description
-------------------------------|---------------------
org.apache.datasketches             | Common functions and utilities
org.apache.datasketches.cpc         | New Unique Counting Sketch with better accuracy per size than HLL
org.apache.datasketches.frequencies | Frequent Item Sketches, for both longs and generics
org.apache.datasketches.hash        | The 128-bit MurmurHash3 and adaptors
org.apache.datasketches.hll         | Unique counting HLL sketches for both heap and off-heap.
org.apache.datasketches.hllmap      | The (HLL) Unique Count Map Sketch
org.apache.datasketches.kll         | New quantiles sketch with better accuracy per size than the standard quantiles sketch.
org.apache.datasketches.quantiles   | Sketches for quantiles, PMF and CDF functions, both doubles and generics and for heap and off-heap.
org.apache.datasketches.sampling    | Weighted and uniform reservoir sampling with generics
org.apache.datasketches.theta       | Unique counting Theta Sketches for both heap and off-heap
org.apache.datasketches.tuple       | Tuple sketches for both primitives and generics

### memory

Memory Packages                | Package Description
-------------------------------|---------------------
org.apache.datasketches.memory               | Low level, high-performance Memory data-structure management primarily for off-heap. 

### sketches-android
This is a new repository dedicated to sketches designed to be run in a mobile client, such as a cell phone. 
It is still in development and should be considered experimental.

### sketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

Sketches-hive Packages               | Package Description
-------------------------------------|---------------------
org.apache.datasketches.hive.cpc          | Hive UDF and UDAFs for CPC sketches
org.apache.datasketches.hive.frequencies  | Hive UDF and UDAFs for Frequent Items sketches
org.apache.datasketches.hive.hll          | Hive UDF and UDAFs for HLL sketches
org.apache.datasketches.hive.kll          | Hive UDF and UDAFs for KLL sketches
org.apache.datasketches.hive.quantiles    | Hive UDF and UDAFs for Quantiles sketches
org.apache.datasketches.hive.theta        | Hive UDF and UDAFs for Theta sketches
org.apache.datasketches.hive.tuple        | Hive UDF and UDAFs for Tuple sketches

### sketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

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

### sketches-vector
This is a new repository dedicated to sketches for vector and matrix operations. It is still somewhat experimental.

### characterization
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


### experimental
This repository is an experimental staging area for code that will eventually end up in another 
repository. This code is not versioned and not registered with 
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.


### sketches-misc
Demos, command-line access, characterization testing and other code not related to production 
deployment.

This code is offered "as is" and primarily as a reference so that users can understand how some of 
the performance characterization plots were obtained. This code has few unit tests, if any, 
and was never intended for production use. 
Nonetheless, some folks have found it useful. If you find it useful, go for it. 
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.
    
Sketches-misc Packages             | Package Description
-----------------------------------|---------------------
org.apache.datasketches                 | Utility functions used by the sketches-misc packages
org.apache.datasketches.cmd             | Support for Command Line functions **Being Redesigned**
org.apache.datasketches.demo            | Simple demo for brute-force vs Theta and HLL sketches **Will be superceded by Command Line functions**
org.apache.datasketches.quantiles       | Utility for computing & printing space table for Quantiles Sketches (only in the test branch)
org.apache.datasketches.sampling        | Benchmarks and Entropy testing for sampling sketches


## C++ and Python

### sketches-core-cpp
This is the evolving C++ implementations of the same sketches that are available in Java. 
These implementations are *binary compatible* with their counterparts in Java.
In other words, a sketch created and stored in C++ can be opened and read in Java and visa-versa.

This site also has our Python adaptors that basically wrap the C++ implementations, 
making the high performance C++ implementations available from Python.

### sketches-postgres
This site provides the postgres-specific adaptors that wrap the C++ implementations making
them available to the Postgres database users.

### characterization-cpp
This is the parallel characterization repository with a parallel objective to the Java characterization repository.

### experimental-cpp
This repository is an experimental staging area for C++ code that will eventually end up in another 
repository.


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


## Web Site

### DataSketches.github.io
This is the DataSketches.github.io web site, and is constantly being updated with new material 
and to be current with the GitHub master.
This site is not versioned and not registered with 
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>. 
  
