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

# Apache DataSketches GitHub Components

Our library is made up of components that are partitioned into GitHub repositories by language and dependencies. The dependencies of the core components are kept to a bare minimum to enable flexible integration into many different environments. Meanwhile, the Hive and Pig components, for example, have major dependencies on those envionments. 

If you have a specific issue or bug report that impacts only one of these components please open an issue on the respective component. If you are a developer and wish to submit a PR, please choose the appropriate repository.

## List of Component Repositories (Explained below)

| Repository                  | URL                                                       |
|-----------------------------|-----------------------------------------------------------|
| Java Core                   | <https://github.com/apache/datasketches-java>             |
| C++ Core                    | <https://github.com/apache/datasketches-cpp>              |
| Hive Adaptor                | <https://github.com/apache/datasketches-hive>             |
| Pig Adaptor                 | <https://github.com/apache/datasketches-pig>              |
| PostgreSQL Adaptor          | <https://github.com/apache/datasketches-postgresql>       |
| Memory                      | <https://github.com/apache/datasketches-memory>           |
| Characterization            | <https://github.com/apache/datasketches-characterization> |
| Website                     | <https://github.com/apache/datasketches-website>          |
| Vector  (Experimental)      | <https://github.com/apache/datasketches-vector>           |
| Server  (Under Development) | <https://github.com/apache/datasketches-server>           |
| Reserved for future use     | <https://github.com/apache/datasketches>                  |


## Core Algorithms
If you like what you see give us a **Star** on one of these two sites!

* **[Java](https://github.com/apache/datasketches-java)** (Versioned, Apache Released) This is the original and the most comprehensive collection of sketch algorithms. It has a dependence on the Memory component and the Java Adaptors have a dependence on this component. 

* **[C++](https://github.com/apache/datasketches-cpp)/[Python](https://github.com/apache/datasketches-cpp/tree/master/python)** (Versioned, Apache Released) This is newer and provides most of the major algorithms available in Java.  Our C++ adaptors have a dependence on this component.  The Pybind adaptors for Python are included for all the C++ sketches.

## Adapters
Adapters integrate the core components into the aggregation APIs of specific data processing systems. Some of these adapters are available as part of the library, other adapters are directly integrated into the target data processing application.

### Java Adaptors
* **[Apache Druid](https://datasketches.apache.org/docs/SystemIntegrations/ApacheDruidIntegration.html)** (Apach Released as part of Druid)
* **[Apache Hive](https://github.com/apache/datasketches-hive)** (Versioned, Apache Released)
    * [Hive Integration](https://datasketches.apache.org/docs/SystemIntegrations/ApacheHiveIntegration.html)
    * [Theta Sketch Example]({{site.docs_dir}}/Theta/ThetaHiveUDFs.html)
    * [Tuple Sketch Example]({{site.docs_dir}}/Tuple/TuplePigUDFs.html)
* **[Apache Pig](https://github.com/apache/datasketches-pig)** (Versioned, Apache Released)
    * [Pig Integration](https://datasketches.apache.org/docs/SystemIntegrations/ApachePigIntegration.html)
    * [Theta Sketch Example]({{site.docs_dir}}/Theta/ThetaPigUDFs.html)
    * [Tuple Sketch Example]({{site.docs_dir}}/Tuple/TuplePigUDFs.html) 


### C++ Adaptors
* **[PostgreSQL](https://github.com/apache/datasketches-postgresql)** (Versioned, Apache Released)
This site provides the postgres-specific adaptors that wrap the C++ implementations making
them available to the PostgreSQL database users. PostgreSQL users should download the PostgreSQL extension from [pgxn.org](https://pgxn.org/dist/datasketches/).  For examples refer to the README on the component site.
    * [PostgreSQL Integration](https://datasketches.apache.org/docs/SystemIntegrations/PostgreSQLIntegration.html)

## Other Components
* **[Memory](https://github.com/apache/datasketches-memory):** (Versioned, Apache Released) This is a low-level library that enables fast access to off-heap memory for Java.
* **[Characterization](https://github.com/apache/datasketches-characterization):** This is a collection of Java and C++ code that we use for long-running studies of accuracy and speed performance over many different parameters. Feel free to run these tests to reproduce many of the graphs and charts you see on our website.
* **[Vector (Experimental)](https://github.com/apache/datasketches-vector):** This component implements the [Frequent Directions Algorithm](/docs/Community/Research.html) [GLP16].  It is still experimental in that the theoretical work has not yet supplied a suitable measure of error for production work. It can be used as is, but it will not go through a formal Apache Release until we can find a way to provide better error properties.  It has a dependence on the Memory component.
* **[Website](https://github.com/apache/datasketches-website):** This repository is the home of our website and is constantly being updated with new material.
* **[Server (Under Development)](https://github.com/apache/datasketches-server)** 



## Deprecated Components
The code in these components are no longer maintained and will eventually be removed.

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

### Command-Line Tool
These repositories provide a command-line tool that provides access to the following sketches:
- Frequent Items
- HLL
- Quantiles
- Reservoir Sampling
- Theta Sketches
- VarOpt Sampling

This tool can be installed from Homebrew.

#### sketches-cmd

#### homebrew-sketches

#### homebrew-sketches-cmd
