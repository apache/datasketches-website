---
title: About DataSketches 
sectionid: overview
layout: simple_page
---
<i>Data Sketches</i> is a Java software library of <a href="/docs/SketchOrigins.html">sketch</a> algorithms.

<h2>Key Features</h2>

<h3>Solves Computational Challenges Associated with Unique Identifiers and Duplicates</h3>

  * <b>Estimating cardinality</b>
  * Performing <b>set operations</b> on groups of unique identifiers, such as Union, Intersection and Difference
  * Obtaining estimates of the <b>distribution of the error</b> of the result
  * Enables <b>extended analysis</b> of these sets through associations, which could include approximate joins and behavior analysis

<h3>Multiple sketch algorithms
* Theta Sketches for 

<h3>Designed for Large-scale Computing Systems</h3>
* <b>Small Footprint Per Sketch</b>
  * The operating and storage footprint for both row and column oriented storage are minimized with 
<a href="/docs/CompactStorage.html">compact binary representations</a>. 

* <b>Speed</b>
  * Single-pass, "one-touch" algorithms enable real-time processing capability.
  * These algorithms are <a href="/docs/UpdateSpeed.html"><i>Fast</i></a> and operate in Java nearly as fast as had they been implemented in C. 
  * Coupled with the compact binary representations, in many cases the need for costly serialization and deserialization has been eliminated.

* <b>Adaptors for Grid Computing</b>
  * Adaptors for <a href="/docs/Adaptors.html">Hadoop Pig and Hive</a> are also included that implement the major functionality of the core algorithms.

* <b>Easy to Integrate</b>
  * Can be integrated into virtually any Java-base system environment
  * The core library has no dependencies outside of Java.
  * The Hadoop Pig and Hive adaptor classes, are provided in separate repositories.
  * Maven deployable.

* <b>Specific Sketch Features</b>
  * <b>Hash Seed Handling</b>. Additional protection for managing hash seeds which is particularly important when processing sensitive user identifiers.
  * <a href="/docs/Sampling.html"><b>Sampling</b></a>. Built-in up-front sampling for cases where additional contol is required to limit overall memory consumption when dealing with millions of sketches.
  * Off-Heap <a href="/docs/MemoryPackage.html"><b>Memory Package</b></a>.  Large query systems often require their own heaps outside the JVM in order to better manage garbage collection latencies. The sketches in this package are designed to operate either on-heap or off-heap.
  * Built-in <b>Upper-Bound and Lower-Bound estimators</b>. You are never in the dark about how good of an estimate the sketch is providing.  All the sketches are able to estimate the upper and lower bounds of the estimate given a confidence level.
  
* <b>Built-In, General Purpose Functions</b>
  * General purpose Memory package for managing data off the Java Heap.  This enables systems designers the ability to manage their own large data heaps with dedicated processor threads that would otherwise put undue pressure on the Java heap and its garbage collection.
  * General purpose implementaion of Austin Appleby's 128-bit MurmurHash3 algorithm, with a number of useful extensions.

<h3>Robust, High Quality Implementations.</h3>
  * Extensive test code leveraging <a href="http://testng.org">TestNG</a>.
  * Code coverage is > 95% as measured by <a href="https://www.atlassian.com/software/clover/overview">Clover</a>.
  * Comprehensive Javadocs that satisfy <a href="http://www.oracle.com/technetwork/java/index.html">Java JDK8</a> standards.

<h3>Opportunities to Extend</h3>

There is ample opportunity for interested parties to contribute additional algorithms in this exciting area.

