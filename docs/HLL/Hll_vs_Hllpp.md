---
layout: doc_page
---

## DataSketches HLL vs. HLL++

The DataSketches HyperLogLog (HLL) sketch implemented in this library has been highly optimized for speed, accuracy and size. The objective of this paper is to do an objective comparison of the DataSketches HLL versus the popular Clearspring Technologies [HyperLogLogPlus (HLL++)](https://github.com/addthis/stream-lib/blob/master/src/main/java/com/clearspring/analytics/stream/cardinality/HyperLogLogPlus.java) implementation, which is based on the Google paper [HyperLogLog in Practice: Algorithmic Engineering of a State of The Art Cardinality Estimation Algorithm](https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40671.pdf).

### Accuracy

Measuring accuracy of these stochastic algorithms is tricky and requires a great deal of thought into the design of the characterization program that measures it. Getting smooth-looking plots requires many tens of thousands of trials, which even with fast CPUs requires a lot of time. 

For accuracy purposes, the HLL sketch is configured with one parameter, *Log_2(K)* which we abreviate as *LgK*. This defines the number of bins of the final HyperLogLog stage, and defines the "bounds" on the accuracy of the sketch as well as its ultimate size. Thus, specifying a *LgK = 12*, means that the final HyperLogLog stage of the sketch will have 4096 bins (*2^12 = 4096*). A sketch with *LgK = 21* will ultimately have 2,097,152 or 2MiBins, which is a very large sketch.

Simlarly, the HLL++ sketch has a parameter *p*, which acts the same as *LgK*. 

The whole point of these HyperLogLog sketches is to achieve good accuracy per bit of storage space for all values of *n*, where *n* is the number of uniques presented to the sketch. For this reason it would be wasteful to allocate the full HLL-Array of bins when the sketch has been presented with only a few values. So typical implementations of these sketches have a warm-up (also called *sparse*) phase, where the early counts are cached and encoded. When the number of cached sparse values reaches a certain size (depending on the implementation), these values are flushed to a full HLL-Array. The HLL-Array is effectively fixed in size thereafter no matter how large *n* gets. This warm-up or sparse mode operation allows the sketch to maintain a low accuracy / stored-bit ratio for all values of *n*. 

Both the HLL and the HLL++ sketches have a "warm-up" or "sparse" mode. This mode is automatic for the HLL sketch, but the HLL++ sketch requires a second configuration parameter *sp*.  This is a number with similar properties to *p*, it is a power of 2 and it determines the accuracy of the sparse mode stage.  The documentation states that *sp* can be zero, in which case the sparse mode is disabled, or *p <= sp <= 32*. 
This will become more clear when we see some of the plots.

Let's start with our first plot of the accuracy of HLL sketch configured with *LgK = 21*.  I chose this very large sketch so that the behavior of the sparse mode and the final HyperLogLog stage becomes quite clear.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllK21T16U24.png" alt="HllK21T16U24.png" />

This is what we call the *pitch-fork plot*. 

The X-axis is *n*, the number of uniques presented to the sketch. There are 16 *trial-points* between each power of 2 along the X-axis. At each trial-point, *2^16 = 65536* trials are executed. The number of trials per trial-point is noted in the chart title as *LgT=16*.  Each trial feeds a new HLL sketch with *2^21 = 2,097,152* different unique values. This is noted in the chart title as *LgK=21*. No two trials use the same uniques. (To generate this plot required 10<sup>12</sup> updates and took 9 hours to complete.)

The Y-axis is a measure of the error of the sketch. Since these sketches are stochastic, each trial produces a different estimate of what the true value of *n* is. If the estimate is larger than *n* it is an overestimate and the resulting relative error, *RE = est/n - 1*, will be positive. If the estimate is an underestimate, the RE will be negative. 

It is not practical to plot all 2 billion error values on a single chart. What is plotted instead are the quantiles at chosen fractional ranks (FR) of the error distribution at each trial-point. When the quantiles at the same FR are connected by lines they form the contours of the shape of the error distribution.  For example, if *FR = 0.5*, it defines the median of the distribution. In this plot the median is black and hidden behind the mean, which is gray, both of which hug the X-axis at zero.

Six different FR values have been chosen in addition to the median (0.5).  These values correspond to the FR values of a Standard Normal Distribution at +/- 1, 2, and 3 Standard Deviations from the mean. These FR values can be computed from the CDF of the Normal Distribution. For example, the FR for +1 standard deviation, *s*, is *FR = (1 + erf(s/sqrt(s)))/2*, where *s = 1*.  The translation from +/- standard deviations to fractional ranks is as follows:

| Std Dev | Fractional Rank |
|:-------:|:----------------|
| 3  | 0.998650102 |
| 2  | 0.977249868 | 
| 1  | 0.841344746 |
|  0 | 0.5 |
| -1 | 0.158655254 |
| -2 | 0.022750132 |
| -3 | 0.001349898 |











