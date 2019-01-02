---
layout: doc_page
---

# HLL sketch vs Druid HyperLogLogCollector

The goal of this article is to compare the HLL sketch implemented in this library to the <a href="https://github.com/druid-io/druid/tree/master/hll/src/main/java/io/druid/hll">Druid HyperLogLogCollector</a>.

## Versions

* HLL sketch form <a href="https://github.com/DataSketches/sketches-core/releases/tag/sketches-core-0.11.1">sketches-core-0.11.1</a> (April 20, 2018)
* Druid HyperLogLogCollector from <a href="https://github.com/druid-io/druid/releases/tag/druid-0.12.0">druid-0.12.0</a> (March 8, 2018)

## Size

The starting point in this comparison was a choice of parameter <i>K=2048</i> for HLL sketch in such a way that it would approximately match the size of Druid HyperLogLogCollector, which has no parameters available to the user. It is quite difficult to measure the size of a Java object in memory, therefore the serialized size was used as the best available measure of size, which is also important for many practical applications in large systems.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-size.png" alt="HLL2048 vs Druid HLLC serialized size plot" />

## Single sketch accuracy

The following plots are what we call pitch-fork plots. The X-axis is the number of distinct identifiers presented to the sketch. The Y-axis is the relative error plotted as +/- percent where values above zero represent an overestimate and values below zero represent an underestimate.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-error-95pct.png" alt="HLL2048 vs Druid HLLC error 95% confidence interval plot" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-error-99pct.png" alt="HLL2048 vs Druid HLLC error 99% confidence interval plot" />

## Merge accuracy

The HLL algorithm is fully mergeable. In simple terms this means that there should be no penalty for partitioning the input, processing these partitions separately, and finally merging the resulting sketches. The resulting estimate is required to be at least as accurate as if the whole input had been presented to a single sketch.

It was proved in <a href="http://algo.inria.fr/flajolet/Publications/FlFuGaMe07.pdf">Flajolet's HLL paper</a> that for sufficiently large N and K, the standard error (i.e. the normalized RMS error) of HLL should be 1.0389 / sqrt(K), whether or not any merging has occurred. This guarantee works out to be about 2.3% when K = 2^11.

The following is an empirical demonstration that the library's implementation of HLL exhibits the required behavior after merging, but Druid's implementation of HLL does not. The latter's measured standard error is about 7 times larger than HLL's theoretical guarantee. This is mostly due to bias; on average, Druid is undercounting by about 16 percent on this example.

	true count: 2.68435456E8
	distinct keys per sketch = 32768
	number of sketches = 8192
	number of trials = 100
	
	The library's implementation:
	Mean estimate: 2.6826835125572532E8
	Relative Standard Error: 0.021226565654784535 (less than 0.023)
	Total Job Time        : 0:07:33.241
	
	Druid's implementation:
	Mean estimate: 2.2560974267493743E8 (16 percent low)
	Relative Standard Error: 0.16058739888244847 (7 times 0.023)
	Total Job Time        : 0:36:43.451

Also, Druid's implementation was much slower.

Technical Note: the library's HLL sketches are more complicated than the standard HLL algorithm. In certain special cases where better-than-HLL accuracy is possible, the library employs other estimators, and even other stochastic processes and data structures. When those special cases no longer apply, the library falls back to
the standard HLL algorithm. As a result, when viewed as black boxes, the library's HLL sketches can exhibit a small penalty for merging, and therefore don't satisfy a strict definition of fully mergeable. However the library is always at least as accurate as standard HLL sketches, which <b>are</b> fully mergeable.

## Source code

The code to reproduce these measurements is available in the <a href="https://github.com/DataSketches/characterization/tree/druid-hyperloglogcollector">Datasketches/characterization repository</a>

## HLL sketch Druid module

The <a href="http://druid.io/docs/latest/development/extensions-core/datasketches-hll.html">Hll sketch module</a> for Druid is available as a part of the <a href="http://druid.io/docs/latest/development/extensions-core/datasketches-extension.html">DataSketches extension</a>.
