---
layout: doc_page
---

## Designed for Large-scale Computing Systems

### Minimal Dependencies

* Can be integrated into virtually any Java-base system environment.
  
* The core library (including Memory) has no dependencies outside of the Java JVM at runtime.

### Maven Deployable

* Registered with <a href="https://search.maven.org/#search|ga|1|DataSketches">The Central Repository</a>

### Speed

* These single-pass, "one-touch" algorithms are <a href="{{site.docs_dir}}/Theta/ThetaUpdateSpeed.html"><i>fast</i></a> to enable real-time processing capability.
  
* Coupled with the compact binary representations, in many cases the need for costly serialization and deserialization has been eliminated.
  
* The sketch data structures are "additive" and embarassingly parallelizable. The Theta sketches can be merged without losing accuracy.

### Integration for Hive, Pig, Druid and Spark

* <a href="https://github.com/DataSketches/sketches-hive/tree/master/src/main/java/com/yahoo/sketches/hive">Hadoop / Hive Adaptors</a>.
  
* <a href="https://github.com/DataSketches/sketches-pig/tree/master/src/main/java/com/yahoo/sketches/pig">Hadoop / Pig Adaptors</a>.
  
* <a href="https://github.com/druid-io/druid/tree/master/extensions-core/datasketches/src/main/java/io/druid/query/aggregation/datasketches">Druid Adaptors</a>
  
* <a href="{{site.docs_dir}}/Theta/ThetaSparkExample.html">Spark Examples</a> 

### Specific Theta Sketch Features for Large Data

* <b>Hash Seed Handling</b>. Additional protection for managing hash seeds which is 
particularly important when processing sensitive user identifiers.

* <a href="{{site.docs_dir}}/Theta/ThetaPSampling.html"><b>Sampling</b></a>. Built-in up-front sampling for cases where additional 
contol is required to limit overall memory consumption when dealing with millions of sketches.

* Off-Heap <a href="{{site.docs_dir}}/Memory/MemoryPackage.html"><b>Memory Package</b></a>. 
Large query systems often require their own heaps outside the JVM in order to better manage garbage collection latencies. 
The sketches in this package are designed to operate either on-heap or off-heap.

* Built-in <b>Upper-Bound and Lower-Bound estimators</b>. 
You are never in the dark about how good of an estimate the sketch is providing. 
All the sketches are able to estimate the upper and lower bounds of the estimate given a 
confidence level.

* User configurable trade-offs of accuracy vs. storage space as well as other performance 
tuning options.

* Additional protection of sensitive data by user configuration of a hash seed that is 
not stored with the serialized data.

* <b>Small Footprint Per Sketch</b>. The operating and storage footprint for both 
row and column oriented storage are minimized with 
<a href="{{site.docs_dir}}/Theta/ThetaSize.html">compact binary representations</a>, which are much smaller 
than the raw input stream and with a well defined upper bound of size.
