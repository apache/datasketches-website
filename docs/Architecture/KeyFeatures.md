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
    * [Google/BigQuery](https://cloud.google.com/blog/products/data-analytics/bigquery-supports-apache-datasketches-for-approximate-analytics)
    * [Druid](https://druid.apache.org)
    * [Spark](https://github.com/apache/datasketches-spark)
    * [PostgreSQL](https://github.com/apache/datasketches-postgresql)
    * [Hadoop/Hive](https://github.com/apache/datasketches-hive)
    * [Pig](https://github.com/apache/datasketches-pig)

* The Java-based sketches are registered with the <b>Maven Central Repository</b>. For example: [DataSketches-Java](https://search.maven.org/search?q=datasketches-java).
* Extensive documentation with the systems developer in mind.
* Designed for production environments:
    * Available in multiple languages: [Java](https://github.com/apache/datasketches-java), [C++](https://github.com/apache/datasketches-cpp), [Python](https://github.com/apache/datasketches-python), and [Go](https://github.com/apache/datasketches-go).
    * Binary compatible across systems and languages. For example, a sketch can be built and loaded in a C++ platform, then serialized and transported to a Java platform where it can be merged with other sketches and queried.

### Built-In, General Purpose Functions

* General purpose [Memory Component]({{site.docs_dir}}/Memory/MemoryComponent.html) for managing data off the Java Heap. 
This enables systems designers the ability to manage their own large data heaps with 
dedicated processor threads that would otherwise put undue pressure on the Java heap and 
its garbage collection.  Starting with Java Version 9.0.0, this functionality is now native to the Java 25 language.
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
* Comprehensive Javadocs.

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

* [The HLL Sketch]({{site.docs_dir}}/HLL/HllSketches.html). The famous HyperLogLog algorithm when stored sketch size is of utmost concern.
* [The CPC Sketch]({{site.docs_dir}}/CPC/CpcSketches.html). The Compressed Probabilistic Counting algorithm when maximizing accuracy per stored sketch size is of utmost concern.
* [The Theta Sketch Framework]({{site.docs_dir}}/Theta/ThetaSketches.html). Theta sketches enable real-time set-expression computations and can operate on or off the java heap.
* [The Tuple Sketch]({{site.docs_dir}}/Tuple/TupleOverview.html). Tuple sketches are associative sketches that are useful for performing approximate join operations and extracting other kinds of statistical behavior associated with unique identifiers.

  
### Quantiles

#### [Four families of Quantile algorithms]({{site.docs_dir}}/QuantilesAll/QuantilesOverview.html)
Get normal or inverse PDFs or CDFs of the distributions of any numeric value from your raw data in a single pass with well defined error bounds on the results.

### Frequency

* [Frequent Items Sketches]({{site.docs_dir}}/Frequency/FrequencySketchesOverview.html) Get the most frequent items from a stream of items.
* [CountMin sketch of Cormode and Muthukrishnan](https://github.com/apache/datasketches-java/blob/main/src/main/java/org/apache/datasketches/count/CountMinSketch.java)
* [Frequent Distinct Tuples](https://github.com/apache/datasketches-java/blob/main/src/main/java/org/apache/datasketches/fdt/FdtSketch.java)
  
### Sampling

* [Reservoir Sampling]({{site.docs_dir}}/Sampling/ReservoirSampling.html) Knuth's well known Reservoir sampling "Algorithm R", but extended to enable merging across different sized reservoirs.
* [Weighted Sampling]({{site.docs_dir}}/Sampling/VarOptSampling.html) Edith Cohen's famous sampling algorithm that enables computing subset sums of weighted samples with optimum variance.
* [Exact and Bounded Sampling Proportional to Size](https://github.com/apache/datasketches-java/blob/main/src/main/java/org/apache/datasketches/sampling/EbppsItemsSketch.java)

### Filters and Set Membership

* [Bloom Filter](https://github.com/apache/datasketches-java/blob/main/src/main/java/org/apache/datasketches/filters/bloomfilter/BloomFilter.java)


