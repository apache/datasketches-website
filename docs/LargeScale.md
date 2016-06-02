---
layout: doc_page
---

<h2>Designed for Large-scale Computing Systems</h2>

<h3>Speed</h3>
  * These single-pass, "one-touch" algorithms are <a href="{{site.docs_dir}}/Theta/ThetaUpdateSpeed.html"><i>fast</i></a> 
  to enable real-time processing capability.
  * Coupled with the compact binary representations, in many cases the need for costly 
  serialization and deserialization has been eliminated.
  * The sketch data structures are "additive" and embarassingly parallelizable. The Theta sketches can be 
  merged without losing accuracy.

<h3>Easy to Integrate</h3>
  * Can be integrated into virtually any Java-base system environment
  * The core library has no dependencies outside of Java.
  * The Hadoop, Druid, Pig, and Hive <a href="{{site.docs_dir}}/Adaptors.html">adaptors<a/>, are provided 
    in separate repositories for most of the core sketches.
  * <b>Maven deployable</b> and registered with 
  <a href="http://search.maven.org/#search|ga|1|DataSketches">The Central Repository</a>

<h3>Specific Theta Sketch Features for Large Data</h3>
  * <b>Hash Seed Handling</b>. Additional protection for managing hash seeds which is 
  particularly important when processing sensitive user identifiers.
  * <a href="{{site.docs_dir}}/Theta/ThetaPSampling.html"><b>Sampling</b></a>. Built-in up-front sampling for cases where additional 
  contol is required to limit overall memory consumption when dealing with millions of sketches.
  * Off-Heap <a href="{{site.docs_dir}}/MemoryPackage.html"><b>Memory Package</b></a>. 
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
