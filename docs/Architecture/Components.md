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

# Apache DataSketches GitHub Component Repositories

Our library is made up of multiple components that are partitioned into GitHub repositories by language and dependencies. The dependencies of the core components are kept to a bare minimum to enable flexible integration into many different environments. The Platform Adaptor components will have major dependencies on the respective platform envionments. 

If you have a specific issue or bug report that impacts only one of these components please open an issue on the respective component. If you are a developer and wish to submit a PR, please choose the appropriate repository.

If you like what you see give us a **Star** on these sites!

## Core Sketch Libraries
The key sketches of the Apache DataSketches libraries are available in three (soon four) programming languages.  By design, a sketch that is available in one language that is also available in a different language will be "binary compatible" via serialization.  For example, when serialized into its compact form, a sketch created by the DataSketches C++ library, can be read by the DataSketches Java library and visa versa.

Because of differences inherent in the languages, there will be some differences in the APIs, but we try to make the same basic functionality available across all the languages.

| Repository                                                                   | Distribution                                                               | Comments |
|------------------------------------------------------------------------------|----------------------------------------------------------------------------|--------|
| [Java Core](https://github.com/apache/datasketches-java)                     | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html) | This is the original and the most comprehensive collection of sketch algorithms. It has a dependency on the Memory component |
| [Memory (supports Java Core)](https://github.com/apache/datasketches-memory) | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html) | Provides high-performance access to off-heap memory |
| [C++ Core](https://github.com/apache/datasketches-cpp)                       | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html) | C++ was our second core language library and provides most of the major algorithms available in Java as well as a few sketches unique to C++. |
| [Python Core](https://github.com/apache/datasketches-python)                 | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html), [PyPI](https://pypi.org/project/datasketches/) | Python was our third core language library and contains most of the major sketch families that are in Java and C++. All the Python sketches are backed by the C++ library via Pybind. |
| [Go Core](https://github.com/apache/datasketches-go)                         | Under Development |  Go is our fourth core language and is still evolving. |

## Platform Adaptors
Adapters integrate the core library components into the aggregation APIs of specific data processing platforms. Some of these adapters are available as an Apache DataSketches distribution, other adapters are directly integrated into the target platform.

| Repository                                                                    | Distribution                                                               | Comments |
|-------------------------------------------------------------------------------|----------------------------------------------------------------------------|----------|
| [Google BigQuery Adaptor](https://github.com/apache/datasketches-bigquery)    | Under Development                                                          | Depends on C++ Core |
| [Apache Hive Adaptor](https://github.com/apache/datasketches-hive)            | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html) | Depends on Java Core, [Integrations](https://datasketches.apache.org/docs/SystemIntegrations/ApacheHiveIntegration.html) |
| [Apache Pig Adaptor](https://github.com/apache/datasketches-pig)              | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html) | Depends on Java Core, [Integrations](https://datasketches.apache.org/docs/SystemIntegrations/ApachePigIntegration.html) |
| [PostgreSQL Adaptor](https://github.com/apache/datasketches-postgresql)       | [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html), [pgxn.org](https://pgxn.org/dist/datasketches/) | Depends on C++ Core, [Integrations](https://datasketches.apache.org/docs/SystemIntegrations/PostgreSQLIntegration.html) |
| [Apache Druid Adaptor](https://druid.apache.org/docs/latest/development/extensions-core/datasketches-extension) | [Apache Druid Release](https://druid.apache.org/downloads) | Depends on Java Core, [Integrations](https://datasketches.apache.org/docs/SystemIntegrations/ApacheDruidIntegration.html) |

## Other

| Repository                                                                   | Distribution          | Comments |
|------------------------------------------------------------------------------|-----------------------|----------|
| [Characterization](https://github.com/apache/datasketches-characterization)  | Not Formally Released | Used for long-running studies of accuracy and speed performance over many different parameters. |
| [Website](https://github.com/apache/datasketches-website)                    | Not Formally Released | Public website |
| [Vector](https://github.com/apache/datasketches-vector)                      | Not Formally Released | This component implements the [Frequent Directions Algorithm](/docs/Community/Research.html) [GLP16].  It is still experimental in that the theoretical work has not yet supplied a suitable measure of error for production work. It can be used as is, but it will not go through a formal Apache Release until we can find a way to provide better error properties.  It dependends on the Memory component. |
| [Server](https://github.com/apache/datasketches-server)        | Not Formally Released | Under development |
