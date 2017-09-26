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









