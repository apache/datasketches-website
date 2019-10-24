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
## Concurrency

Only the [Concurrent Theta Sketch]({{site.docs_dir}}/Theta/ConcurrentThetaSketch.html), which can be derived from the UpdateSketchBuilder can be considered thread-safe. None of the other sketches in this library have been designed for concurrent operation and should be considered __not thread safe__.

Most systems that incorporate sketches generally design a wrapper class that maps the required sketch API to the host system environment API.  This is the simplest place to encorporate thread synchronization.

Be aware that some systems (e.g. Spark) may assume thread safety of user modules especially during serialization and deserialization steps. 

As an example of how the issue of concurrency can rear it ugly head refer to this [issue](https://github.com/DataSketches/sketches-core/issues/178#issuecomment-365673204).




