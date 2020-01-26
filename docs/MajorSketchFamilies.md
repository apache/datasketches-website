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
# Sketch Capability Matrix

<table>
<tr style="font-weight:bold"><td colspan=2></td><td colspan=3>Languages</td><td colspan=4>Set Operations</td><td colspan=4>System Integrations</td><td colspan=5>Misc.</td></tr>

<tr style="font-weight:bold"><td>Type</td><td>Sketch</td><td>Java</td><td>C++</td><td>Python</td><td>Union</td><td>Intersection</td><td>Difference</td><td>Jaccard</td><td>Hive</td><td>Pig</td><td>Druid<sup>1</sup></td><td>Spark<sup>2</sup></td><td>Concurrent</td><td>Compact</td><td>Java Generics</td><td>Off-Heap</td><td>Error Bounds</td></tr>

<tr style="font-weight:bold"><td colspan=18>Major Sketches</td></tr>
<tr><td>Cardinality/FM85</td><td>CpcSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/FM85</td><td>HllSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Cardinality/Theta</td><td>Sketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>Sketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td>Y</td></tr>
<tr><td>Quantiles/Cormode</td><td>DoublesSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Quantiles/Cormode</td><td>ItemsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>
<tr><td>Quantiles/KLL</td><td>FloatsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Frequencies</td><td>LongsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Frequencies</td><td>ItemsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>
<tr><td>Sampling</td><td>ReservoirLongsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Sampling</td><td>ReseerviorItemsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>
<tr><td>Sampling</td><td>VarOptItemsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>

<tr style="font-weight:bold"><td colspan=18>Specialty Sketches</td></tr>

<tr><td>Cardinality/FM85</td><td>UniqueCountMap</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>FdtSketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>ArrayOfDoubles</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>A double</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>An integer</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>ArrayOfStrings</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>User Engagement</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
</table>

<sup>1</sup> Integrated into Druid<br>
<sup>2</sup> Example Code

----


# High-Level Descriptions

## Cardinality Sketches

### CPC Sketch: Estimating Stream Cardinalities more efficiently than the famous HLL sketch!
This sketch was developed by the late Keven Lang, our chief scientist at the time, is an amazing *tour de force* of scientific design and engineering and has substantially better accuracy / per stored size than the famous HLL sketch. The theory and demonstration of its performance is detailed in Lang's paper [Back to the Future: an Even More Nearly Optimal Cardinality Estimation Algorithm](https://arxiv.org/abs/1708.06839).  This sketch is available in Java, C++ and Python. 

### [Theta Sketches]({{site.docs_dir}}/Theta/ThetaSketchFramework.html): Estimating Stream Expression Cardinalities
Internet content, search and media companies like Yahoo, Google, Facebook, etc., collect many tens of billions of event records from the many millions of users to their web sites each day.  These events can be classified by many different dimensions, such as the page visited and user location and profile information.  Each event also contains some unique identifiers associated with the user, specific device (cell phone, tablet, or computer) and the web browser used.  

<img class="doc-img-full" src="{{site.docs_img_dir}}/PeopleCloud.png" alt="PeopleCloud" />

These same unique identifiers will appear on every page that the user visits.  In order to measure the number of unique identifiers on a page or across a number of different pages, it is necessary to discount the identifier duplicates.  Obtaining an exact answer to a _COUNT DISTINCT_ query with massive data is a difficult computational challenge. It is even more challenging if it is necessary to compute arbitrary expressions across sets of unique identifiers. For example, if set _S_ is the set of user identifiers visiting the Sports page and set _F_ is the set of user identifiers visiting the Finance page, the intersection expression _S &#8745; F_ represents the user identifiers that visited both Sports and Finance.

Computing cardinalities with massive data requires lots of computer resources and time.
However, if an approximate answer to these problems is acceptable, [Theta Sketches]({{site.docs_dir}}/Theta/ThetaSketchFramework.html) can provide reasonable estimates, in a single pass, orders of magnitude faster, even fast enough for analysis in near-real time.

The [theta/Sketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/theta/Sketch.java) can operate both on-heap and off-heap, has powerful Union, Intersection, AnotB and Jaccard operators, has a high-performance concurrent form for multi-threaded environments, has both immutable compact, and updatable representations, and is quite fast. It is available in Java, C++ and Python. Because of its flexibility, it is one of the most popular sketches in our library.

### [Tuple Sketches]({{site.docs_dir}}/Tuple/TupleOverview.html): Extending Theta Sketches to Perform Associative Analysis 
It is often not enough to perform stream expressions on sets of unique identifiers, it is very valuable to be able to associate additive data with these identifiers, such as impression counts, clicks or timestamps.  Tuple Sketches are a natural extension of the Theta sketch and have Java Genric forms, that enable the user do define the sketch with arbitrary "summary" data.  The [tuple/Sketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/tuple/Sketch.java) can operate both on-heap and off-heap, includes both Union and Intersection operators, has both immutable compact, and updatable representations. At the current time this sketch is only available in Java.

The Tuple sketch is effectively infinitely extendable and there are several common variants of the Tuple Sketch, which also serve as examples on how to extend the base classes, that are also in the library, including:

- [tuple/adouble/DoubleSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/tuple/adouble/DoubleSketch.java) with a single column of *double* values as the *summary*.
- [tuple/aninteger/IntegerSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/tuple/aninteger/IntegerSketch.java) with a single column of *int* values as the *summary*.
- [tuple/strings/ArrayOfStringsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/tuple/strings/ArrayOfStringsSketch.java), which is effectively a variable number of columns of strings as the *summary*.
- [tuple/ArrayOfDoublesSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/tuple/ArrayOfDoublesSketch.java), which enables the user to specify the number of columns of double values as the *summary*. This variant also provides both on-heap and off-heap operation.


### [HyperLogLog Sketches]({{site.docs_dir}}/HLL/HLL.html): Estimating Stream Cardinalities
The HyperLogLog (HLL) is a cardinality sketch similar to the above Theta sketches except they are anywhere from 2 to 16 times smaller in size.  The HLL sketches can be Unioned, but set intersection and difference operations are not provided intrinsically, because the resulting error would be quite poor.  If your application only requires cardinality estimation and Unioning and space is at a premium, the HLL sketch provided could be your best choice. 

The [hll/HllSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/hll/HllSketch.java) can operate both on-heap and off-heap, provides the Union operators, and has both immutable compact, and updatable representations. It is available in Java, C++ and Python.

### [HyperLogLog Map Sketch]({{site.docs_dir}}/HLL/HllMap.html): Estimating Stream Cardinalities of Key-Value Pairs
This is a specially designed sketch that addresses the problem of individually tracking value cardinalities of Key-Value (K,V) pairs in real-time, where the number of keys can be very large, such as IP addresses, or Geo identifiers, etc. Assigning individual sketches to each key would create unnecessary overhead. This sketch streamlines the process with much better space management.  This [hllmap/UniqueCountMap](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/hllmap/UniqueCountMap.java) only operates on heap and does not provide Union capabilites.  Since this is effectively a optimized hash-map of HLL sketches, the overall size is a direct function of the number of keys that it has seen it was intended to operate as a stand-alone sketch. This sketch is only available in Java.

## Quantiles Sketches

### [Quantiles Sketches]({{site.docs_dir}}/Quantiles/QuantilesOverview.html): Estimating Distributions from a Stream of Values
There are many situations where is valuable to understand the distribution of values in a stream. For example, from a stream of web-page time-spent values, it would be useful to know arbitrary quantiles of the distribution, such as the 25th percentile value, the median value and the 75th percentile value. The [Quantiles Sketches]({{site.docs_dir}}/Quantiles/QuantilesOverview.html) solve this problem and enable the inverse functions such as the Probability Mass Function (PMF) and the Cumulative Distribution Function (CDF) as well. It is relatively easy to produce frequency histograms such as the following diagram, which was produced from a stream of over 230 million time spent events. The space consumed by the sketch was about 43KB.

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/TimeSpentHistogram.png" alt="TimeSpentHistogram" />

There are two different families of quantiles sketches, the original [quantiles/DoublesSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/quantiles/DoublesSketch.java), which can be operated either on-heap or off-heap, and is also available in a Java Generic form for arbitrary comparable objects. It is only available in Java.

Later we developed the [kll/KllFloatsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/kll/KllFloatsSketch.java)  (Named after its authors), which is also a quantiles sketch, that achieves near optimal small size for a given accuracy. It is only available on-heap. It is available in Java, C++ and Python.

## Frequent Items / Heavy Hitters Sketches

### [Frequent Items Sketches]({{site.docs_dir}}/Frequency/FrequentItemsOverview.html): Finding the Heavy Hitter Objects from a Stream
It is very useful to be able to scan a stream of objects, such as song titles, and be able to quickly identify those items that occur most frequently.  The term <i>Heavy Hitter</i> is defined to be an item that occurs more frequently than some fractional share of the overall count of items
in the stream including duplicates.  Suppose you have a stream of 1M song titles, but in that stream there are only 100K song titles that are unique. If any single title consumes more than 10% of the stream elements it is a Heavy Hitter, and the 10% is a threshold parameter we call epsilon.

The accuracy of a Frequent Items Sketch is proportional to the configured size of the sketch, the larger the sketch, the smaller is the epsilon threshold that can detect Heavy Hitters. This sketch is available in two forms, as the [frequencies/LongsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/frequencies/LongsSketch.java) used for processing a stream of tuples {*long*, weight}, and the [frequencies/ItemsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/frequencies/ItemsSketch.java) used for processing tuples of {*T*, weight}, where *T* is arbitrary, uniquely identifyable object. These sketches are aggregating sketches in that the frequency of occurances of an item (or *long* key), is accumulated as the stream is processed. These sketches are available in Java, C++ and Python.

### [Frequent Distinct Tuples Sketch]({{site.docs_dir}}/Frequency/FrequentDistinctTuplesSketch.html): Finding the Heavy Hitter tuples from a Stream.
Suppose our data is a stream of pairs {IP address, User ID} and we want to identify the IP addresses that have the most distinct User IDs. Or conversely, we would like to identify the User IDs that have the most distinct IP addresses. This is a common challenge in the analysis of big data and the FDT sketch helps solve this problem using probabilistic techniques.

More generally, given a multiset of tuples with *N* dimensions *{d1,d2, d3, …, dN}*, and a primary subset of dimensions *M < N*, our task is to identify the combinations of *M* subset dimensions that have the most frequent number of distinct combinations of the *N - M* non-primary dimensions.

The [fdt/FdtSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/fdt/FdtSketch.java) is currently only available in Java, but because it is an extension of the Tuple Sketch family, it inherits many of the same properties: it can operate both on-heap and off-heap, includes both Union and Intersection operators, has both immutable compact, and updatable representations.

### Frequent Directions: Distributed, mergeable Singular Value Decomposition 
Part of a new separate sketches-vector package, Frequent Directions is in many ways a generalization of the Frequent Items sketch to handle vector data. This sketch computes an approximate singular value decomposition (SVD) of a matrix, providing a projection matrix that can be used for dimensionality reduction. SVD is a key technique in many recommender systems, providing shopping suggestions based on a customer's past purchases compared with other similar customers. This sketch is still experimental and feedback from interested users would be welcome.  This sketch can be found in the [Vector](https://github.com/apache/incubator-datasketches-vector) repository dedicated to vector and matrix sketches.  This sketch is only available in Java.

## Sampling Sketches

### [Sampling Sketches]({{site.docs_dir}}/Sampling/ReservoirSampling.html): Uniform Sampling of a Stream into a fixed size space
This family of sketches implements an enhanced version of the famous Reservoir sampling algorithm and extends it with the capabilities that large-scale distributed systems really need: mergability (even with different sized sketches), uses Java Generics so that the base classes can be trivially extended for any input type (even polymorphic types), and an extensible means of performing serialization and deserialization. 

The [sampling/ReservoirLongsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/sampling/ReservoirLongsSketch.java) accepts a stream of *long* values as identifiers with a weight of one, and produces a result Reservoir of a pre-determined size that represents a uniform random sample of the stream.

The [sampling/ReservoirItemsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/sampling/ReservoirItemsSketch.java) accepts a stream of type *T* as identifiers with a weight of one, and produces a result Reservoir of a pre-determined size that represents a uniform random sample of the stream.

The [sampling/VarOptItemsSketch](https://github.com/apache/incubator-datasketches-java/blob/master/src/main/java/org/apache/datasketches/sampling/VarOptItemsSketch.java) extends the Reservoir family to weighted sampling, additionally providing subset sum estimates from the sample with provably optimal variance. 

This family is currently only available in Java.


