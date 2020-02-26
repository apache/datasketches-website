---
layout: no_toc_page
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

# Apache DataSketches (incubating)
## A software library of stochastic streaming algorithms 

Apache DataSketches is an open source, high-performance library of stochastic streaming algorithms commonly called "sketches" in the data sciences. Sketches are small, stateful programs that process massive data as a stream and provide approximate answers, with mathematical guarantees, to computationally difficult queries orders-of-magnitude faster than traditional, exact methods.

After 8 years of development and 5 years as in Open Source, we have begun the important migration from a stand-alone GitHub site to being a member of the Apache Software Foundation community.  While we undergo this migration, we beg your patience. 

* [How to contact us]({{site.docs_dir}}/Community/index.html)

* [Finding our code releases]({{site.docs_dir}}/Community/index.html)

* [Contributing]({{site.docs_dir}}/Community/index.html)


* As the repositories under GitHub.io/DataSketches migrate they will disapear from the the GitHub.com/DataSketches organization page. Please refer to this list be directed to the new locations. 
  * View all of our Apache DataSketches repositories as a [list](https://github.com/apache?utf8=%E2%9C%93&q=datasketches)
  * Names in **BOLD** are of the old github.com/datasketches/ organization repositories.

* Web Site and Web Site Source
  * **datasketches.github.io** moved to [datasketches.apache.org](https://datasketches.apache.org)
  * **github.com/DataSketches/DataSketches.github.io** moved to [github.com/apache/incubator-datasketches-website](https://github.com/apache/incubator-datasketches-website)

* Java Core
  * **sketches-core** moved to [incubator-datasketches-java](https://github.com/apache/incubator-datasketches-java) This is the **core** library that contains all sketch algorithms written in Java.
  * **memory** moved to [incubator-datasketches-memory](https://github.com/apache/incubator-datasketches-memory) Low-level component used by other java components.

* Java Adaptors
  * **sketches-hive** moved to [incubator-datasketches-hive](https://github.com/apache/incubator-datasketches-hive) Adapts the Java core to Apache Hive.
  * **sketches-pig** moved to [incubator-datasketches-pig](https://github.com/apache/incubator-datasketches-pig) Adapts the Java core to Apache Pig.
  * **sketches-vector** moved to [incubator-datasketches-vector](https://github.com/apache/incubator-datasketches-vector) Experimental sketches for vector and matrix processing.
  * [Apache Druid adaptors](https://github.com/apache/druid/tree/master/extensions-core/datasketches)

* C++ / Python Core
  * **sketches-core-cpp** moved to [incubator-datasketches-cpp](https://github.com/apache/incubator-datasketches-cpp) This is the **core** library that contains all major sketch algorithms written in C++ and Python.

* C++ Adaptors
  * **sketches-postgres** moved to [incubator-datasketches-postgresql](https://github.com/apache/incubator-datasketches-postgresql) Adapts the C++ core to PostgreSQL.

* Java and C++
  * **characterization** moved to [incubator-datasketches-characterization](https://github.com/apache/incubator-datasketches-characterization) Java and C++ Characterization suites for benchmarking and exhaustive testing.

* Obsolete Components
  * [sketches-android](https://github.com/DataSketches/sketches-android) An experimental proof-of-concept sketch for the Android cell-phone.
  * [sketches-misc](https://github.com/DataSketches/sketches-misc)
  * [experimental](https://github.com/DataSketches/experimental)
  * [characterization-cpp](https://github.com/DataSketches/characterization-cpp)
  * [experimental-cpp](https://github.com/DataSketches/experimental-cpp)

* Command Line Functions (Mostly obsolete. These may move to Apache later or replaced by Python) 
  * [homebrew-sketches-cmd](https://github.com/DataSketches/homebrew-sketches-cmd)
  * [sketches-cmd](https://github.com/DataSketches/sketches-cmd)
  * [homebrew-sketches](https://github.com/DataSketches/homebrew-sketches)

-----

Disclaimer: Apache DataSketches is an effort undergoing incubation at The Apache Software Foundation (ASF), sponsored by the Apache Incubator. Incubation is required of all newly accepted projects until a further review indicates that the infrastructure, communications, and decision making process have stabilized in a manner consistent with other successful ASF projects. While incubation status is not necessarily a reflection of the completeness or stability of the code, it does indicate that the project has yet to be fully endorsed by the ASF.
