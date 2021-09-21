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
## Key Features

### [Sketch Features Matrix]({{site.docs_dir}}/Architecture/SketchFeaturesMatrix.html)

### Common Sketch Properties

* Please refer to the [Sketch Criteria]({{site.docs_dir}}/Architecture/SketchCriteria.html) for the criteria for sketches to be included in the library.
* Query results are <b>approximate</b> but within well defined error bounds that are user 
  configurable by trading off sketch size with accuracy.
* Designed for <a href="{{site.docs_dir}}/Architecture/LargeScale.html">Large-scale</a> computing environments 
  that must handle <b>Big Data</b>, e.g.:
    * [Hadoop](https://hadoop.apache.org/)
    * [Pig](https://pig.apache.org)
    * [Hive](https://hive.apache.org)
    * [Druid](https://druid.apache.org)
    * [Spark](https://spark.apache.org)
* <b>Maven deployable</b> and registered with the [Central Repository](https://search.maven.org/#search|ga|1|DataSketches).
* Extensive documentation with the systems developer in mind.
* Designed for production environments:
    * Available in multiple languages: Java, C++, [Python](https://github.com/apache/datasketches-cpp/tree/master/python)
    * Binary compatible across systems and languages 

### Built-In, General Purpose Functions

* General purpose [Memory Component]({{site.docs_dir}}/Memory/MemoryComponent.html) for managing data off the Java Heap. 
This enables systems designers the ability to manage their own large data heaps with 
dedicated processor threads that would otherwise put undue pressure on the Java heap and 
its garbage collection.
* General purpose implementaion of Austin Appleby's 128-bit MurmurHash3 algorithm, 
  with a number of useful extensions.

### Robust, High Quality Implementations.
* Unit Tests:
    * Extensive test code leveraging [TestNG](https://testng.org).
    * High Test Code coverage (> 90%) as measured by [JaCoCo]https://www.eclemma.org/jacoco) and published by 
[Coveralls](https://coveralls.io).
* Reproducible Characterization Studies
    * All our published speed and accuracy performance results can be reproduced using the code included in the 
[Characterization](https://github.com/apache/datasketches-characterization) repository.
* Comprehensive Javadocs that satisfy 
[JDK8 Javadoc](https://docs.oracle.com/javase/8/docs/technotes/guides/javadoc/index.html) standards.

### Opportunities to Extend

* There is ample opportunity for interested parties to contribute additional algorithms in this exciting area.

## Key Algorithms

### Count Distinct / Count Unique

#### Solves Computational Challenges Associated with Unique Identifiers

* <b>Estimating cardinality</b> of a stream with many duplicates
* Performing [Set Operations]({{site.docs_dir}}/Theta/ThetaSketchSetOps.html) (e.g., Union, Intersection, 
  and Difference) on sets of unique identifiers
* Estimates of the <b>error bounds</b> of the result can be obtained directly from the result sketch

#### Four families of Count Unique algorithms:

* [The HLL Sketch]({{site.docs_dir}}/HLL/HLL.html). The famous HyperLogLog algorithm when stored sketch size is of utmost concern.
* [The CPC Sketch]({{site.docs_dir}}/CPC/CPC.html). The Compressed Probabilistic Counting algorithm when maximizing accuracy per stored sketch size is of utmost concern.
* [The Theta Sketch Framework]({{site.docs_dir}}/Theta/ThetaSketchFramework.html). Theta sketches enable real-time set-expression computations and can operate on or off the java heap.
* [The Tuple Sketch]({{site.docs_dir}}/Tuple/TupleOverview.html). Tuple sketches are associative sketches that are useful for performing approximate join operations and extracting other kinds of statistical behavior associated with unique identifiers.

  
### Quantiles

* [Quantiles Sketch Overview]({{site.docs_dir}}/Quantiles/QuantilesSketchOverview.html). Get normal or inverse PDFs or CDFs of the distributions of any numeric value from your raw data in a single pass with well defined error bounds on the results.
  
### Frequent Items

* [Frequent Items Sketches]({{site.docs_dir}}/Frequency/FrequencySketchesOverview.html) Get the most frequent items from a stream of items.
  
### Sampling

* [Reservoir Sampling]({{site.docs_dir}}/Sampling/ReservoirSampling.html) Knuth's well known Reservoir sampling "Algorithm R", but extended to enable merging across different sized reservoirs.
* [Weighted Sampling]({{site.docs_dir}}/Sampling/VarOptSampling.html) Edith Cohen's famous sampling algorithm that enables computing subset sums of weighted samples with optimum variance.


