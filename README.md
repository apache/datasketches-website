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

# Apache DataSketches (incubating)
## A software library of stochastic streaming algorithms 

Apache DataSketches is an open source, high-performance library of stochastic streaming algorithms commonly called "sketches" in the data sciences. Sketches are small, stateful programs that process massive data as a stream and provide approximate answers, with mathematical guarantees, to computationally difficult queries orders-of-magnitude faster than traditional, exact methods.

After 8 years of development and 5 years as in Open Source, we have begun the important migration from a stand-alone GitHub site to being a member of the Apache Software Foundation community.  While we undergo this migration, we beg your patience. 

* Ways to contact us:
    * We have two ASF [the-ASF.slack.com](http://the-ASF.slack.com) slack channels:
        * datasketches -- general user questions
        * datasketches-dev -- similar to our Apache [dev@datasketches.apache.org](mailto:dev@datasketches.apache.org), except more interactive, but not as easily searchable.
    * For discussions about problems with any of our sketch repository components (core-java, core-cpp, Hive, Pig, Vector, Android, etc.)
        * [Google-groups forum](https://groups.google.com/forum/#!forum/sketches-user)
    * For issues about a specific component, please open issues on the appropriate GitHub repository (listed below). 
    * If you wish to contribute to our sketch development please contact us on our development email list
        * [dev@datasketches.apache.org](mailto:dev@datasketches.apache.org) 

* Finding our code releases:
    * Apache releases (versions 1.X.X):
        * Zip source: [dist.apache.org/repos/dist/release/incubator/datasketches/](https://dist.apache.org/repos/dist/release/incubator/datasketches/)
        * Jar Artifacts (Java only): [repository.apache.org/#nexus-search;quick~datasketches](https://repository.apache.org/#nexus-search;quick~datasketches)
    * Pre-Apache releases (versions 0.X.X)
        * Jar Artifacts (Java only): [search.maven.org/classic/#search%7Cga%7C1%7Ccom.yahoo.datasketches](https://search.maven.org/classic/#search%7Cga%7C1%7Ccom.yahoo.datasketches)


* As the repositories under GitHub.io/DataSketches migrate they will disapear from the the GitHub.com/DataSketches organization page. Please refer to this list be directed to the proper locations.

* Web Sites
  * [Original Website](http://DataSketches.GitHub.io)
  * [New Website](https://datasketches.apache.org) 
  * [Original Website Source](https://github.com/DataSketches/DataSketches.github.io)
  * [New Website Source](https://github.com/apache/incubator-datasketches-website)

* Java 
  * [memory now incubator-datasketches-memory](https://github.com/apache/incubator-datasketches-memory)
  * [sketches-core now incubator-datasketches-java](https://github.com/apache/incubator-datasketches-java)
  * [sketches-hive now incubator-datasketches-hive](https://github.com/apache/incubator-datasketches-hive)
  * [sketches-pig now incubator-datasketches-pig](https://github.com/apache/incubator-datasketches-pig)
  * [sketches-vector now incubator-datasketches-vector](https://github.com/apache/incubator-datasketches-vector)
  * [characterization now incubator-datasketches-characterization](https://github.com/apache/incubator-datasketches-characterization)

* C++
  * [sketches-core-cpp now incubator-datasketches-cpp](https://github.com/apache/incubator-datasketches-cpp)
  * [sketches-postgres now incubator-datasketches-postgresql](https://github.com/apache/incubator-datasketches-postgresql)

* Java/C++/Python (the following may be moved to Apache later)
  * [sketches-misc](https://github.com/DataSketches/sketches-misc)
  * [sketches-android](https://github.com/DataSketches/sketches-android)
  * [experimental](https://github.com/DataSketches/experimental)
  * [characterization-cpp](https://github.com/DataSketches/characterization-cpp)
  * [experimental-cpp](https://github.com/DataSketches/experimental-cpp)

* Command Line (These may move to Apache later or replaced by Python) 
  * [homebrew-sketches-cmd](https://github.com/DataSketches/homebrew-sketches-cmd)
  * [sketches-cmd](https://github.com/DataSketches/sketches-cmd)
  * [homebrew-sketches](https://github.com/DataSketches/homebrew-sketches)

-----

Disclaimer: Apache DataSketches is an effort undergoing incubation at The Apache Software Foundation (ASF), sponsored by the Apache Incubator. Incubation is required of all newly accepted projects until a further review indicates that the infrastructure, communications, and decision making process have stabilized in a manner consistent with other successful ASF projects. While incubation status is not necessarily a reflection of the completeness or stability of the code, it does indicate that the project has yet to be fully endorsed by the ASF.
