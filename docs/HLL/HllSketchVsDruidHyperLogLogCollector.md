---
layout: doc_page
---

# HLL sketch vs Druid HyperLogLogCollector

The goal of this article is to compare the HLL sketch implemented in this library to the <a href="https://github.com/druid-io/druid/tree/master/hll/src/main/java/io/druid/hll">Druid HyperLogLogCollector</a>.

## Versions

* KLL sketch form <a href="https://github.com/DataSketches/sketches-core/releases/tag/sketches-core-0.11.1">sketches-core-0.11.1</a> (April 20, 2018)
* Druid HyperLogLogCollector from <a href="https://github.com/druid-io/druid/releases/tag/druid-0.12.0">druid-0.12.0</a> (March 8, 2018)

## Size

The starting point in this comparison was a choice of parameter <i>k=2048</i> for HLL sketch in such a way that it would approximately match the size of Druid HyperLogLogCollector, which has no parameters available to the user. It is quite difficult to measure the size of a Java object in memory, therefore the serialized size was used as the best available measure of size, which is also important for many practical applications in large systems.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-size.png" alt="HLL2048 vs Druid HLLC serialized size plot" />

## Single sketch accuracy

The following plots are what we call pitch-fork plots. The X-axis is the number of distinct identifiers presented to the sketch. The Y-axis is the relative error plotted as +/- percent where values above zero represent an overestimate and values below zero represent an underestimate.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-error-95pct.png" alt="HLL2048 vs Druid HLLC error 95% confidence interval plot" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-error-99pct.png" alt="HLL2048 vs Druid HLLC error 99% confidence interval plot" />

## Merge accuracy

The HLL algorithm is fully mergeable. In simple terms this means no penalty for partitioning the input in any number of ways, processing these partitions separately and merging the resulting sketches. The resulting estimate must be within the same error bounds as if the whole input was presented to a single sketch.

In practice, the implementation of the HLL sketch in this library uses HIP (Historical Inverse Probability) estimate for a single sketh, which is slightly (about 20-30%) more accurate than a composite estimate. The HIP estimate cannot be used for merged sketches, so the implementation has to fall back to a more conservative composite estimate. This difference can be perceived as a slight penalty for merging, or as a slight benefit from not merging.

The following is an empirical measurement of relative error after merging:

	distinct keys per sketch = 32768
	number of sketches = 8192
	number of trials = 100
	
	Datasketches HLL:
	True count: 2.68435456E8
	Mean estimate: 2.682570869364015E8
	Mean Relative Error: 0.017548665411422536
	Total Job Time: 0:07:33.375
	
	Druid HLL:
	True count: 2.68435456E8
	Mean estimate: 2.259574435603495E8
	Mean Relative Error: 0.1582429276393743
	Total Job Time: 0:37:10.089

Theoretical standard error for 11-bit HLL (K=2048) is about 2.3%. The HLL sketch form this library performed well within expectations showing about 1.75% relative error on average. The Druid HLL sketch showed about 15.8% relative error, which is significantly worse than expected. Notice that it took about 5 times longer to run the same process using Druid HLL sketch.

## Source code

The code to reproduce these measurements is available in the <a href="https://github.com/DataSketches/characterization/tree/druid-hyperloglogcollector">Datasketches/characterization repository</a>
