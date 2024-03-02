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

#### Multiple Languages

* The DataSketches library is now available in three languages, Java, C++, and Python. A fourth language, Go, is in development.
 

### Compatibility Across Languages, Software Versions And Binary Serialization Versions
Large-scale computing environments may have a mix of various platforms utilizing different programming languages each with the possiblity of using different Software Versions of our DataSketches library.  Cross version compatibility of software is a challenge that all platforms face in general, and it is up to the platform maintainers to keep their software up-to-date. This not new and not different with the DataSketches library.  

Nonetheless, it our goal to strive to make it as easy as practically possible to serialize our sketches in one of our supported languages on one platform and to be deserialized in a different supported language, potentially on a different, even remote platform, and perhaps much later in time.  

With this goal in mind, here are some of the key strategic decisions we have made in the development of the DataSketches library. 

#### Two levels of versioning.

* **Software Version:** This is the release version, published via Apache.org and specified in the POM file or equivalent. This can change relatively frequently based on bug fixes and introduction of new capabilities. We follow the principles of *Semantic Versioning* as specified by [semver.org](https://semver.org).

* **Serialization Version:** (*SerVer*) This is a small integer placed in the preamble of the serialized byte array that indicates the version of the serialized structure for the sketch. This is very similar to Java's [*Class File Format Version*](https://en.wikipedia.org/wiki/Java_class_file). A single *SerVer* may represent multiple structures all based on the same sketch when stored in different states, e.g., *Single Item*, *Compact*, *Updatable*, etc). This *SerVer* changes very rarely, if at all. Of all of our sketches, only a few, e.g., Theta, KLL and Sampling, have had more than one *SerVer* over time. There are and will be many *Software Versions* of the same sketch that still use the same *SerVer*. When we have to update the *SerVer*, we provide the capability in the *Software Version* of the code associated with the new *SerVer* the ability to read and convert the old *SerVer* to the new *SerVer*. This is why our newest *Software Versions* can still read and interpret older *SerVer* serialized sketches that go back to when our project was started at Yahoo (2012), and before we went open-source (2015). Technically speaking this can be described as *Backward-Transient* compatibility [Schema Evolution and Compatibility](https://docs.confluent.io/platform/current/schema-registry/fundamentals/schema-evolution.html) and [Schema Evolution](https://en.wikipedia.org/wiki/Schema_evolution).

From the user's perspective, as long as the *SerVer* is the same, older *Software Versions* should be able to read sketch images created by newer *Software Versions*. But the APIs may be different, obviously. An older *Software Version* will not be able to take advantage of new features introduced in new *Software Versions*, but it should be able to do what it did before. In other words, there will be no loss of access to the serialized sketch and the older *Software Version* capabilities. A user should not need to access the *SerVer*, nonetheless it is always stored in index one of the serialized image. If a sketch is presented with a *SerVer* that it is not compatible with, the sketch should throw an exception and say what the problem is, just like Java does with its *Class File Format Versions*.

#### The Serialized Image of a Sketch
* The structure (or image) of a serialized sketch is independent of the language from which it was created. 
* The sketch image only contains little-endian primitives, such as int64, int32, int16, int8, double-64, float-32, UTF-8 strings, and simple array structures of those. While these serialized primitives between languages may not be strictly equal they can be interpreted to be logically equivalent. We do not support big-endian serialization.
* The sketch image is unique for each type of sketch.
* Simply speaking, a sketch image can be viewed as a blob of bytes, which is easily stored and easily transported using many different protocols, including Protobuf, Avro, Thrift, Byte64, etc.

As a result, sketches serialized in one supported language can be interpreted by a different supported language, with the caveat that due to language differences, availability of resources, and time to develop, not all sketches may be available in all languages at the same time.

### Easy Integration with Minimal Dependencies
We strive to make our sketch library easy to integrate into larger systems by keeping the number of external dependencies at a minimum.

* [Java Core](https://datasketches.apache.org/docs/Community/Downloads.html)
    * The Java core library (including Memory) has no dependencies outside of the Java JVM at runtime allowing simple integration into virtually any Java based system environment.
    * All of the Java components and artifacts are Maven Deployable and registered with [The Central Repository](https://search.maven.org/classic/#search%7Cga%7C1%7Cg%3A%22org.apache.datasketches%22)

* [C++ Core](https://datasketches.apache.org/docs/Community/Downloads.html)
    * The C++ core is written as all header files allowing easy integration into a wide range of operating system environments. 

* [Python](https://github.com/apache/datasketches-cpp/tree/master/python)
	* The C++ Core is extended using the python binding library [pybind11](https://github.com/pybind/pybind11) enabling high performance operation from Python.

### Speed
* These single-pass, "one-touch" algorithms are <i>fast ([see example](https://datasketches.apache.org/docs/Theta/ThetaUpdateSpeed.html))</i> to enable real-time processing capability.

* Sketches can be represented in an updatable or compact form. The compact form is smaller,  immutable and faster to merge.

* Some of the Java sketches have been designed to be instantiated and operated <i>off-heap</i>, whicn eliminates costly serialization and deserialization.

* The sketch data structures are "additive" and embarrassingly parallelizable. Sketches can be merged without losing accuracy.

### System Integrations
The following are system integrations that we have been involved with, but there are many more platform integrations out there that were performed by the individual platform teams.

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
Nearly all the sketches are able to estimate the upper and lower bounds of the estimate given a 
confidence level.

* User configurable trade-offs of accuracy vs. storage space as well as other performance 
tuning options.

* <b>Small Footprint Per Sketch</b>. The in-memory run-time and storage footprint for both 
row and column oriented storage are minimized with compact binary representations, which are much smaller 
than the raw input stream and with a well defined upper bound of size.
