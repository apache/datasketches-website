---
layout: doc_page
---

## Architecture

The DataSketches Library is organized into the following repositories:

### characterization
This relatively new repository is for code that we use to characterize the accuracy and speed performance of the sketches in 
the library and is constantly being updated.  Examples of the job command files used for various tests can be found in the src/main/resources directory.
Some of these tests can run for hours depending on its configuration.

Characterization Packages                       | Package Description
------------------------------------------------|---------------------
com.yahoo.sketches.characterization             | Common functions and utilities
com.yahoo.sketches.characterization.memory      | Memory performance
com.yahoo.sketches.characterization.quantiles.  | Quantiles performance
com.yahoo.sketches.characterization.uniquecount | Performance of Theta and HLL sketches


### experimental
This repository is an experimental staging area for code that will eventually end up in another 
repository. This code is not versioned and not registered with 
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.


### memory

Memory Packages                | Package Description
-------------------------------|---------------------
com.yahoo.memory               | Low level Memory data-structure management primarily for off-heap. 

### sketches-android
This is a new repository dedicated to sketches designed to be run in a mobile client, such as a cell phone. It is still in development and should be considered experimental.

### sketches-core
This repository has the core sketching classes, which are leveraged by some of the other repositories.   
This repository has no external dependencies outside of the DataSketches/memory repository, Java and TestNG for unit tests. 
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

<b>High-level Repositories Structure</b>

Sketches-core Packages.        | Package Description
-------------------------------|---------------------
com.yahoo.sketches             | Common functions and utilities
com.yahoo.sketches.frequencies | Frequent Item Sketches, for both longs and generics
com.yahoo.sketches.hash        | The 128-bit MurmurHash3 and adaptors
com.yahoo.sketches.hll         | Unique counting HLL sketches for both heap and off-heap.
com.yahoo.sketches.hllmap      | The (HLL) Unique Count Map Sketch
com.yahoo.sketches.quantiles   | Sketches for quantiles, PMF and CDF functions, both doubles and generics and for heap and off-heap.
com.yahoo.sketches.sampling    | Weighted and uniform reservoir sampling with generics
com.yahoo.sketches.theta       | Unique counting Theta Sketches for both heap and off-heap
com.yahoo.sketches.tuple       | Tuple sketches for both primitives and generics


### sketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

Sketches-hive Packages               | Package Description
-------------------------------------|---------------------
com.yahoo.sketches.hive.frequencies  | Hive UDF and UDAFs for Frequent Items sketches
com.yahoo.sketches.hive.hll          | Hive UDF and UDAFs for HLL sketches
com.yahoo.sketches.hive.quantiles    | Hive UDF and UDAFs for Quantiles sketches
com.yahoo.sketches.hive.theta        | Hive UDF and UDAFs for Theta sketches
com.yahoo.sketches.hive.tuple        | Hive UDF and UDAFs for Tuple sketches


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
com.yahoo.sketches                 | Utility functions used by the sketches-misc packages
com.yahoo.sketches.cmd             | Support for Command Line functions **Being Redesigned**
com.yahoo.sketches.demo            | Simple demo for brute-force vs Theta and HLL sketches **Will be superceded by Command Line functions**
com.yahoo.sketches.quantiles       | Utility for computing & printing space table for Quantiles Sketches (only in the test branch)
com.yahoo.sketches.sampling        | Benchmarks and Entropy testing for sampling sketches


### sketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

Sketches-pig Packages              | Package Description
-----------------------------------|---------------------
com.yahoo.sketches.pig.frequencies | Pig UDFs for Frequent Items sketches
com.yahoo.sketches.pig.hash        | Pig UDF for MurmerHash3
com.yahoo.sketches.pig.hll         | Pig UDFs for HLL sketches
com.yahoo.sketches.pig.quantiles   | Pig UDFs for Quantiles sketches
com.yahoo.sketches.pig.sampling.   | Pig UDFs for Sampling sketches
com.yahoo.sketches.pig.theta       | Pig UDFs for Theta sketches
com.yahoo.sketches.pig.tuple       | Pig UDFs for Tuple sketches


### sketches-vector
This is a new repository dedicated to sketches for vector and matrix operations. It is still somewhat experimental.



### DataSketches.github.io
This is the DataSketches.github.io web site, and is constantly being updated with new material 
and to be current with the GitHub master.
This site is not versioned and not registered with 
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>. 
  