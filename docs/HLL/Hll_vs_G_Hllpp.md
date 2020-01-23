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
# *HllSketch* vs Google HLL++ *ZetaSketch*
The DataSketches HyperLogLog *HllSketch*\[1\]\[2\] implemented in this library has been highly optimized for speed, accuracy and size. The goal of this paper is to do an objective comparison of the *HllSketch* versus the Google *ZetaSketch* implementation, which is based on Google's HyperLogLog++ paper[4]. These tests were performed on the *HllSketch* Apache DataSketches-java release version 1.2.0-incubating and on the HLL++ *ZetaSketch* version 0.1.0.

## *HllSketch* vs. *ZetaSketch* Error Behavior
