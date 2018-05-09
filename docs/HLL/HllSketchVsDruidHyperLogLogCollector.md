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

## Accuracy

The following plots are what we call pitch-fork plots. The X-axis is the number of distinct identifiers presented to the sketch. The Y-axis is the relative error plotted as +/- percent where values above zero represent an overestimate and values below zero represent an underestimate.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-error-95pct.png" alt="HLL2048 vs Druid HLLC error 95% confidence interval plot" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/hll-sketch-vs-druid-error-99pct.png" alt="HLL2048 vs Druid HLLC error 99% confidence interval plot" />

## Source code

The code to reproduce these measurements is available in the <a href="https://github.com/DataSketches/characterization/tree/druid-hyperloglogcollector">Datasketches/characterization repository</a>
