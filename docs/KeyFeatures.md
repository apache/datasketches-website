---
layout: doc_page
---

<h3><i>Data Sketches</i> is a Java software library of <a href="https://en.wikipedia.org/wiki/Stochastic"><i>stochastic</i></a> 
        <a href="https://en.wikipedia.org/wiki/Streaming_algorithm"><i>streaming algorithms</i></a> and related tools.</h3>

<h2>Key Features</h2>

<h3>Common Sketch Properties</h3>
  * <b>Single-pass, "one-touch"</b> algorithms enable efficient processing in either real-time or batch
  * Query results are <b>approximate</b> but within well defined error bounds that are user configurable by trading off sketch size with accuracy
  * Designed for <b>big data</b> These sketch implementations are specifically designed for <a href="LargeScale.html">Large-scale</a> computing environments, and are heavily used within Yahoo
  * <b>Maven deployable</b> and registered with <a href="http://search.maven.org/#search|ga|1|DataSketches">The Central Repository</a>
  * Comprehensive <b>unit tests</b> and testing tools are provided
  * Extensive documentation with the systems developer in mind

<h3>Built-In, General Purpose Functions</h3>
  * General purpose <a href="MemoryPackage.html">Memory Package</a> for managing data off the Java Heap.  This enables systems designers the ability to manage their own large data heaps with dedicated processor threads that would otherwise put undue pressure on the Java heap and its garbage collection.
  * General purpose implementaion of Austin Appleby's 128-bit MurmurHash3 algorithm, with a number of useful extensions.

<h3>Robust, High Quality Implementations.</h3>
* Extensive test code leveraging <a href="http://testng.org">TestNG</a>.
* Benchmarking, speed and accuracy characterization and performance testing code included in the <i>test</i> package.
* Test Code coverage is > 90% as measured by <a href="https://www.atlassian.com/software/clover/overview">Atlassian Clover</a>.
* Comprehensive Javadocs that satisfy <a href="http://www.oracle.com/technetwork/java/index.html">Java JDK8</a> standards.
* Suitable for production environments.

<h3>Opportunities to Extend</h3>
* There is ample opportunity for interested parties to contribute additional algorithms in this exciting area.



<h2>Key Algorithms</h2>

<h3>Count Distinct / Count Unique</h3>

<h4>Solves Computational Challenges Associated with Unique Identifiers</h4>
  * <b>Estimating cardinality</b> of a stream with many duplicates
  * Performing <a href="ThetaSketchSetOps.html">set operations</a> (e.g., Union, Intersection, and Difference) on sets of unique identifiers
  * Estimates of the <b>error bounds</b> of the result can be obtained directly from the result sketch
  * Two families of Count Unique algorithms:
    * <a href="ThetaSketchFramework.html">The Theta Sketch Framework</a> algorithms that are tuned for operation on the java heap or off-heap.
    * <a href="HLL.html">The Hyper-Log Log algorithms<a/> when sketch size is of utmost concern.
  
<h3>Quantiles[1]</h3>
  * Get normal or inverse PDFs or CDFs of the distributions of any numeric value from your raw data in a single pass.
  * Well defined error bounds on the result.
  
<h3>Frequent Items[1]</h3>
  * Get the most frequent items from a stream of items.
  
<h3>Tuple Sketch[1]</h3>
  * Associative sketches that are useful for performing approximate join operations and extracting other kinds of behavior associated with unique identifiers.

<br>
<br>
<h5>1. Coming soon!</h5>
