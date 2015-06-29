---
layout: simple_page
title: Overview 
subtitle:
---
<i>Data Sketches</i> is a Java software library of <i>sketch</i> algorithms that have evolved out of the relatively new computer science discipline of 
<a href="https://en.wikipedia.org/wiki/Streaming_algorithm"><i>Streaming Algorithms</i></a>[1].  This field has experienced a great deal of interest and growth coinciding with the growth of the Internet and the need to process and analyze <a href="https://en.wikipedia.org/wiki/Big_data">Big Data</a>.  The term <i>sketch</i>, with its allusion to an artist's sketch, has become the popular term to describe these algorithms and their data structures that implement the theory.

The primary goals for the implementations of these algorithms included in this library are as follows.

* Practical for large computing systems processing Big Data
  * Major emphasis on speed both in updating and set operations. These algorithms operate in Java nearly as fast as had they been implemented in C.  
  * Major emphasis on minimizing the operating and storage footprint.  In certain cases, the need for serialization and deserialization has been nearly eliminated.
  * Adaptors for Hadoop Pig and Hive are also included that implement the major functionality of these algorithms.
  * Easy to integrate into a wide range of systems.
    * The core library has no dependencies outside of Java.
    * The Hadoop Pig and Hive adaptor classes, provided separately, have the required Hadoop dependencies.
    * Maven deployable.
    * Extensive tests leveraging <a href="http://testng.org">TestNG</a>.
  * Optional features, especially useful for large computing systems include
    * Moving and managing data off the Java Heap.  This enables systems designers the ability to manage their own large data heaps with dedicated processor threads that would otherwise put undue pressure on the Java heap and its garbage collection.
    * Additional protection for managing hash seeds which is particularly important when processing sensitive user identifiers.
    * Built-in up-front sampling for cases where additional contol is required to limit overall memory consumption when dealing with millions of sketches.
* The current algorithms address computationally difficult problems related to data containing large numbers of unique identifiers with duplicates
  * Estimating cardinality
  * Performing set operations on groups of unique identifiers, such as Union, Intersection and Difference
  * Enabling extended analysis of these sets through associations, which could include approximate joins and behavior analysis
  * Obtaining estimates of the distribution of the error of the result
* There is ample opportunity for interested parties to contribute additional algorithms in this exciting area.
  * Extensive documentation to facilitate contribution.
















[1] Also known as "Approximate Query Processing", see <a href="http://people.cs.umass.edu/~mcgregor/711S12/sketches1.pdf">Sketch Techniques for Approximate Query Processing</a> and 
<a href="http://dl.acm.org/citation.cfm?id=2344401">Synopses for Massive Data: Samples, Histograms, Wavelets, Sketches</a>.
