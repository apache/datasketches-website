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

## System Merge Speed
The following graph illustrates the speed of merging millions of sketches in a large system environment.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/MergeSpeed.png" alt="MergeSpeed" />

### How this graph was generated

This system had already built millions of sketches in a "sketch mart" of millions of rows of data.  The goal of this measurement was to measure sketch merge performance in a realistic large system using thousands of different types of queries.  The "rows" represent many different combinations of dimensions and coordinates.  Every systems enviroment, data structures, and query profiles are different and this is no exception.  Nonetheless, as one can see from the graph, this system measured a maximum sketch merge rate of about 14.5 million sketches per second per processor thread.

### Measurement Systems
A cluster of typical data-center class systems.

