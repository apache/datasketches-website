---
layout: overview_page
---

## Architecture

The DataSketches Library is organized into the following repositories:

### sketches-core
This repository has the core sketching classes and related functions that are used by the other repositories. 
This repository has no external dependencies outside of Java and TestNG, which is used only for unit tests. 
Users that only need the sketching capability can download the latest sketches-core.jar from 
<a href="http://search.maven.org/#search|ga|1|datasketches">Maven Central<a/> using Maven or manually 
and adding it to the classpath.

High-level Structure      | Package Description
--------------------------|---------------------
com.yahoo.sketches        | Some common functions
com.yahoo.sketches.hash   | The MurmurHash3 and adaptors
com.yahoo.sketches.hll    | HLL sketches
com.yahoo.sketches.memory | Off-heap Memory management
com.yahoo.sketches.quantiles | Sketches for quantiles, PMF and CDF functions
com.yahoo.sketches.theta  | Theta sketches


### sketches-pig
This repository contains Pig User Defined Functions (UDF) for use within Hadoop grid environments. 
This code has dependencies on sketches-core as well as Hadoop and Pig. 
Users of this code are advised to use Maven to bring in all the required dependencies.

High-level Structure      | Package Description
--------------------------|---------------------
com.yahoo.sketches.hash   | Pig UDFs for MurmerHash3
com.yahoo.sketches.theta  | Pig UDFs for Theta sketches


### sketches-hive
This repository contains Hive UDFs and UDAFs for use within Hadoop grid enviornments. 
This code has dependencies on sketches-core as well as Hadoop and Hive. 
Users of this code are advised to use Maven to bring in all the required dependencies.

High-level Structure      | Package Description
--------------------------|---------------------
com.yahoo.sketches.theta  | Hive UDAFs for Theta sketches

### experimental
This repository is an experimental staging area for code that will eventually end up in another repository.

### DataSketches.github.io
This is the DataSketches.github.io web site.