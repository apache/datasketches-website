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
## Designed for Large-scale Computing Systems

### Easy Integration with Minimal Dependencies
* [Java Core](https://datasketches.apache.org/docs/Community/Downloads.html)
    * The Java core library (including Memory) has no dependencies outside of the Java JVM at runtime allowing simple integration into virtually any Java based system environment.
    * All of the Java components are Maven Deployable and registered with [The Central Repository](https://search.maven.org/classic/#search%7Cga%7C1%7Cg%3A%22org.apache.datasketches%22)

* [C++ Core](https://datasketches.apache.org/docs/Community/Downloads.html)
    * The C++ core is written as all header files allowing easy integration into a wide range of operating system environments. 

* [Python](https://github.com/apache/datasketches-cpp/tree/master/python)
	* The C++ Core is extended using the python binding library [pybind11](https://github.com/pybind/pybind11) enabling high performance operation from Python.

### Cross Language Binary Compatibility
* Sketches serialized from C++ or Python can be interpreted by compatible Java sketches and visa versa. 

### Speed
* These single-pass, "one-touch" algorithms are <i>fast ([see example](https://datasketches.apache.org/docs/Theta/ThetaUpdateSpeed.html))</i> to enable real-time processing capability.

* Sketches can be represented in an updatable or compact form. The compact form is smaller,  immutable and faster to merge.

* Some of the Java sketches have been designed to be instantiated and operated <i>off-heap</i>, whicn eliminates costly serialization and deserialization.

* The sketch data structures are "additive" and embarrassingly parallelizable. Sketches can be merged without losing accuracy.

### Systems Integrations
* [Druid Integration](https://datasketches.apache.org/docs/SystemIntegrations/ApacheDruidIntegration.html)  

* [Apache Hive](https://datasketches.apache.org/docs/SystemIntegrations/ApacheHiveIntegration.html)

* [Apache Pig](https://datasketches.apache.org/docs/SystemIntegrations/ApachePigIntegration.html)

* [PostgreSQL](https://datasketches.apache.org/docs/SystemIntegrations/PostgreSQLIntegration.html)

* [Spark Examples](https://datasketches.apache.org/docs/Theta/ThetaSparkExample.html) 

### Specific Sketch Features for Large Data
* <b>Hash Seed Handling</b>. Additional protection for managing hash seeds which is 
particularly important when processing sensitive user identifiers. Available with Theta Sketches.

* [Pre-Sampling]({{site.docs_dir}}/Theta/ThetaPSampling.html). Built-in up-front sampling for cases where additional 
contol is required to limit overall memory consumption when dealing with millions of sketches. Available with Theta Sketches.

* [Memory Component]({{site.docs_dir}}/Memory/MemoryComponent.html). 
Large query systems often require their own heaps outside the JVM in order to better manage garbage collection latencies. 
The Java sketches utilize this powerful component. 

* Built-in <b>Upper-Bound and Lower-Bound estimators</b>. 
You are never in the dark about how good of an estimate the sketch is providing. 
All the sketches are able to estimate the upper and lower bounds of the estimate given a 
confidence level.

* User configurable trade-offs of accuracy vs. storage space as well as other performance 
tuning options.

* <b>Small Footprint Per Sketch</b>. The operating and storage footprint for both 
row and column oriented storage are minimized with compact binary representations, which are much smaller 
than the raw input stream and with a well defined upper bound of size.
