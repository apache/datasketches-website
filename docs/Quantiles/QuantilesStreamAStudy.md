---
layout: doc_page
---

# Quantiles StreamA Study

The goal of this article is to compare the accuracy performance of the DataSketches Quantiles Sketch to an exact, brute-force computation using actual data extracted from one of our back-end servers. 

Please get familiar with the [Definitions]({{site.docs_dir}}/Quantiles/Definitions.html) for quantiles.

## Versions

* DataSketches sketches-core, Released Version 0.12.0  <a href="https://search.maven.org/classic/#artifactdetails%7Ccom.yahoo.datasketches%7Csketches-core%7C0.12.0%7Cjar">Maven Central Repository</a> Updated Aug 7, 2018.


## The Paper

The implementation of this Quantiles Sketch was originally inspired by 
[Mergable Summaries]https://dl.acm.org/citation.cfm?id=2213562), PODS, May, 2012 paper by Agarwal, Cormode, Huang, Phillips, Wei, and Yi.

## The Input Data
The data file used for this evaluation, *streamA.txt*, is real data extracted from one of our back-end servers.  It represents one hour of web-site time-spent data measured in milliseconds. The data in this file has a smooth and well-behaved value distribution with a wide dynamic range.  It is a text file and consists of consecutive strings of numeric values separated by a line-feeds. Its size is about 2GB.

## Creating the Exact CDF and Histograms For Comparison
In order to measure the accuracy of the Approximate Histogram, we need an exact, brute-force analysis of *streamA.txt*. 

The process for creating these comparison standards can be found [here]({{site.docs_dir}}/Quantiles/ExactQuantiles.html).

## The Results

### K = 256, Size = 21408 bytes

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/DSQSketchK256_StreamA_CDF.png" alt="DataSketches Quantiles Sketch StreamA CDF of ranks to quantiles" />  

The green dots in the above plot represents the Exact cumulative distribution (CDF) of ranks to quantile values. The red circles represent the values returned from the DS Quantiles Sketch *getQuantiles(double[])* function. 

The plot reveals a very tight fit to the exact quantiles over the full range. The thin black and blue lines just to the left and right of the plotted points represent the lower-bound and upper-bound error derived from the sketch's *getLowerBound()* and *getUpperBound()* functions. The normalized rank error specification for this sketch which was created with *k = 256* is +/- 0.717%.

The next plot is the Histogram generated from the values returned by the *getPMF(double[])* function. Each of the returned values is multiplied by *getN()* to produce the respecive mass of each bin.  
The Green bars represent the Exact Distribution, and the Orange bars represent the results obtained from the DS Quantiles sketch.

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/DSQsketchK256_StreamA_PMF.png" alt="DataSketches Quantiles Histogrm vs Exact" />

The histogram produced by the DS Quantiles sketch very closely matches the Exact Histogram. 

### K = 32, Size = 3232 bytes

The CDF plot:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/DSQSketchK32_StreamA_CDF.png" alt="DataSketches Quantiles Sketch StreamA CDF of ranks to quantiles" />

The Histogram plot:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/DSQsketchK32_StreamA_PMF.png" alt="DataSketches Quantiles Histogrm vs Exact" />

## The Evaluation Source
The following are used to create the plots above.

* [DSQsketch profiler](https://github.com/DataSketches/characterization/blob/master/src/main/java/com/yahoo/sketches/characterization/quantiles/QuantilesStreamAProfile.java)
* [DSQsketch K256 config](https://github.com/DataSketches/characterization/blob/master/src/main/resources/quantiles/QuantilesK256StreamAJob.conf)
* [DSQsketch K32 config](https://github.com/DataSketches/characterization/blob/master/src/main/resources/quantiles/QuantilesK32StreamAJob.conf)
* [StreamA Data file](https://github.com/DataSketches/characterization/blob/master/streamA.txt.zip) This is stored using git-lfs.

Run the above profilers as a java application and supply the config file as the single argument. The program will check if the data file has been unzipped, and if not it will unzip it for you. 




 
