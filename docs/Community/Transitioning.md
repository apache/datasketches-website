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

# Transitioning from our previous GitHub Site.

After 8 years of development and 5 years in Open Source, we began in May, 2019 the important migration from a stand-alone GitHub site to being a member of the [Apache Software Foundation](https://apache.org) community.

While we undergo this migration, we beg your patience. 

As the repositories under GitHub.io/DataSketches migrate they will disapear from the the GitHub.com/DataSketches organization page. Please refer to this list be directed to the new locations. 

View all of our Apache DataSketches repository components as a [list](https://github.com/apache?utf8=%E2%9C%93&q=datasketches)

**Note** The following names in **BOLD** are of the old github.com/datasketches/ organization repositories.

* Web Site and Web Site Source
  * **datasketches.github.io** moved to [datasketches.apache.org](https://datasketches.apache.org)
  * **github.com/DataSketches/DataSketches.github.io** moved to [github.com/apache/datasketches-website](https://github.com/apache/datasketches-website)

* Java Core
  * **sketches-core** moved to [datasketches-java](https://github.com/apache/datasketches-java) This is the **core** library that contains all sketch algorithms written in Java.
  * **memory** moved to [datasketches-memory](https://github.com/apache/datasketches-memory) Low-level component used by other java components.

* Java Adaptors
  * **sketches-hive** moved to [datasketches-hive](https://github.com/apache/datasketches-hive) Adapts the Java core to Apache Hive.
  * **sketches-pig** moved to [datasketches-pig](https://github.com/apache/datasketches-pig) Adapts the Java core to Apache Pig.
  * **sketches-vector** moved to [datasketches-vector](https://github.com/apache/datasketches-vector) Experimental sketches for vector and matrix processing.
  * [Apache Druid adaptors](https://datasketches.apache.org/docs/SystemIntegrations/ApacheDruidIntegration.html)

* C++ / [Python](https://github.com/apache/datasketches-cpp/tree/master/python) Core
  * **sketches-core-cpp** moved to [datasketches-cpp](https://github.com/apache/datasketches-cpp) This is the **core** library that contains all major sketch algorithms written in C++ and Python.

* C++ Adaptors
  * **sketches-postgres** moved to [datasketches-postgresql](https://github.com/apache/datasketches-postgresql) Adapts the C++ core to PostgreSQL.

* Java and C++
  * **characterization** moved to [datasketches-characterization](https://github.com/apache/datasketches-characterization) Java and C++ Characterization suites for benchmarking and exhaustive testing.

* Obsolete Components
  * [sketches-android](https://github.com/DataSketches/sketches-android) An experimental proof-of-concept sketch for the Android cell-phone.
  * [sketches-misc](https://github.com/DataSketches/sketches-misc)
  * [experimental](https://github.com/DataSketches/experimental)
  * [characterization-cpp](https://github.com/DataSketches/characterization-cpp)
  * [experimental-cpp](https://github.com/DataSketches/experimental-cpp)

* Command Line Functions (Mostly obsolete. These may move to Apache later or replaced by [Python](https://github.com/apache/datasketches-cpp/tree/master/python)) 
  * [homebrew-sketches-cmd](https://github.com/DataSketches/homebrew-sketches-cmd)
  * [sketches-cmd](https://github.com/DataSketches/sketches-cmd)
  * [homebrew-sketches](https://github.com/DataSketches/homebrew-sketches)
