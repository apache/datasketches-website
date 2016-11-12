---
layout: doc_page
---

## Architecture

The DataSketches Library is organized into the following repositories:

### sketches-core
This repository has the core sketching classes and related functions that are used by the other 
repositories. 
This repository has no external dependencies outside of Java and TestNG, which is used only for 
unit tests. 
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.


High-level Package Structure   | Description
-------------------------------|---------------------
com.yahoo.sketches             | Some common functions and utilities
com.yahoo.sketches.frequencies | Frequent Item Sketches, for both longs and generics
com.yahoo.sketches.hash        | The 128-bit MurmurHash3 and adaptors
com.yahoo.sketches.hll         | HLL sketches, and HLL Map sketches
com.yahoo.sketches.memory      | Off-heap Memory management
com.yahoo.sketches.quantiles   | Sketches for quantiles, PMF and CDF functions, both doubles and generics
com.yahoo.sketches.sampling    | Reservoir sampling with generices 
com.yahoo.sketches.theta       | Theta sketches
com.yahoo.sketches.tuple       | Tuple sketches for both primitives and generics


### sketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

High-level Structure               | Package Description
-----------------------------------|---------------------
com.yahoo.sketches.pig.frequencies | Pig UDFs for Frequent Items sketches
com.yahoo.sketches.pig.hash        | Pig UDF for MurmerHash3
com.yahoo.sketches.pig.quantiles   | Pig UDFs for Quantiles sketches
com.yahoo.sketches.pig.theta       | Pig UDFs for Theta sketches
com.yahoo.sketches.pig.tuple       | Pig UDFs for Tuple sketches


### sketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.
This code is versioned and the latest release can be obtained from
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

High-level Structure                 | Package Description
-------------------------------------|---------------------
com.yahoo.sketches.hive.frequencies  | Hive UDF and UDAFs for Frequent Items sketches
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
    
High-level Structure               | Package Description
-----------------------------------|---------------------
com.yahoo.sketches.benchmark       | Benchmarking code for the HLL sketches
com.yahoo.sketches.cmd             | Support for Command Line functions
com.yahoo.sketches.demo            | Simple demo for brute-force vs Theta & HLL sketches
com.yahoo.sketches.hll             | Error Characterization and Command-line functions for experimenting with CountUniqueMap
com.yahoo.sketches.performance     | Speed and Error Characteriation of Theta an HLL sketches
com.yahoo.sketches.sampling        | Benchmarks and Entropy testing

### experimental
This repository is an experimental staging area for code that will eventually end up in another 
repository. This code is not versioned and not registered with 
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>.

### DataSketches.github.io
This is the DataSketches.github.io web site, and is constantly being updated with new material 
and to be current with the latest releases of the registered repositories.
This site is not versioned and not registered with 
<a href="https://search.maven.org/#search|ga|1|datasketches">Maven Central<a/>. 
  