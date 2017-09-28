---
layout: doc_page
---

# DataSketches HLL vs HLL++
The DataSketches HyperLogLog (DS-HLL) sketch implemented in this library has been highly optimized for speed, accuracy and size. The goal of this paper is to do an objective comparison of the DataSketches HyperLogLog versus the popular Clearspring Technologies [HyperLogLogPlus (HLL++)](https://github.com/addthis/stream-lib/blob/master/src/main/java/com/clearspring/analytics/stream/cardinality/HyperLogLogPlus.java) implementation, which is based on the Google paper [HyperLogLog in Practice: Algorithmic Engineering of a State of The Art Cardinality Estimation Algorithm](https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40671.pdf).

## DataSketches HLL vs. HLL++ Error Behavior

### High-Level Summary
The DS-HLL sketch has better error properties than the HLL++ sketch.  This can be easily seen from the following side-by-side comparison:

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllVsHllppAcc.png" alt="/hll/HllVsHllppAcc.png" />

The colored curves represent different quantile contours of the measured error distribution. The orange and green curves are the contours at the quantiles corresponding to +/- one standard deviation from the mean error, and which define the 68% confidence bounds. The red and blue curves are the contours at +/- 2 standard deviations and define the 95.4% confidence bounds. The brown and purple curves are the contours at +/- 3 standard deviations and define the 99.7% confidence bounds. The mean (gray) and median (black) overlap each other and hug the axis close to zero. 

### DataSketches HLL Sketch Error

Measuring the error properties of these stochastic algorithms is tricky and requires a great deal of thought into the design of the characterization program that measures it. Getting smooth-looking plots requires many tens of thousands of trials, which even with fast CPUs requires a lot of time. 

For accuracy purposes, the DS-HLL sketch is configured with one parameter, *Log_2(K)* which we abreviate as *LgK*. This defines the number of bins of the final HyperLogLog stage, and defines the bounds[1] on the accuracy of the sketch as well as its ultimate size. Thus, specifying a *LgK = 12*, means that the final HyperLogLog stage of the sketch will have *k = 2<sup>12</sup> = 4096* bins. A sketch with *LgK = 21* will ultimately have *k =2,097,152* or 2MiBins, which is a very large sketch.

Simlarly, the HLL++ sketch has a parameter *p*, which acts the same as *LgK*. 

The whole point of these HyperLogLog sketches is to achieve small error per bit of storage space for all values of *n*, where *n* is the number of uniques presented to the sketch. For this reason it would be wasteful to allocate the full HLL-Array of bins when the sketch has been presented with only a few values. So typical implementations of these sketches have a *warm-up* (also called *sparse*) mode, where the early counts are encoded and cached. When the cached sparse values reach a certain size (depending on the implementation), these values are flushed to a full HLL-Array. The HLL-Array is effectively fixed in size thereafter no matter how large *n* gets. This warm-up (or sparse) mode of operation allows the sketch to maintain a low error / stored-bit ratio for all values of *n*. 

Both the DS-HLL and the HLL++ sketches have a warm-up or "sparse" mode. This mode is automatic for the DS-HLL sketch, but the HLL++ sketch requires a second configuration parameter *sp*.  This is a number with similar properties to *p*. It is an exponent of 2 and it determines the error properties of the sparse mode stage.  The documentation states that *sp* can be zero, in which case the sparse mode is disabled, or *p <= sp <= 32*. 
This will become more clear when we see some of the plots.

Let's start with our first plot of the accuracy of DS-HLL sketch configured with *LgK = 21*.  We choose this very large sketch so that the behavior of the sparse mode and the final HyperLogLog stage becomes quite clear.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllK21T16U24.png" alt="HllK21T16U24.png" />

This is what we call the *pitch-fork plot*. 

The X-axis is *n*, the true number of uniques presented to the sketch. The range of values on this X-axis is from 1 to 2<sup>24</sup>. There are 16 *trial-points* between each power of 2 along the X-axis (except at the very left end of the axis where there are not 16 distinct integers between the powers of 2). At each trial-point, 2<sup>16</sup> = 65536 trials are executed. The number of trials per trial-point is noted in the chart title as *LgT=16*.  Each trial feeds a new DS-HLL sketch configured for 2<sup>21</sup> = 2,097,152 bins. This is noted in the chart title as *LgK=21*. No two trials use the same uniques. (To generate this plot required >10<sup>12</sup> updates and took 9 hours to complete.)

#### DS-HLL Measured Error
The Y-axis is a measure of the error of the sketch. Since these sketches are stochastic, each trial can produce a different estimate of what the true value of *n* is. If the estimate is larger than *n* it is an overestimate and the resulting relative error, *RE = est/n - 1*, will be positive. If the estimate is an underestimate, the RE will be negative. 

It is not practical to plot all 2 billion error values on a single chart. What is plotted instead are the quantiles at chosen Fractional Ranks (FR) of the error distribution at each trial-point. When the quantiles at the same FR are connected by lines they form the contours of the shape of the error distribution.  For example, if *FR = 0.5*, it defines the median of the distribution. In this plot the median is black and hidden behind the mean, which is gray, both of which hug the X-axis at zero.

Six different FR values have been chosen in addition to the median (0.5).  These values correspond to the FR values of a Standard Normal Distribution at +/- 1, 2, and 3 Standard Deviations from the mean. These FR values can be computed from the CDF of the Normal Distribution as *FR = (1 + erf( s/&radic;2 ))/2*, where *s = standard deviation*.  The translation from +/- *s* to fractional ranks is as follows:

| Std Dev | Fractional Rank |
|:-------:|:----------------|
| +3  | 0.998650102 |
| +2  | 0.977249868 | 
| +1  | 0.841344746 |
|  0 | 0.5 |
| -1 | 0.158655254 |
| -2 | 0.022750132 |
| -3 | 0.001349898 |

Due to the Central Limit Theorm, with sufficiently large values of *n*, the error distribution will approach the Normal Distribution. The resulting quantile curves using the above FR values allows us to associate the error distribution of the sketch with conventional confidence intervals commonly used in statistics. 

For example, the area between the orange and the green curves corresponds to +/- 1 Standard Deviations or (0.841 - .158) = 68.3% confidnece. The area between the red and the blue curves corresponds to +/- 2 Standard Deviations or (0.977 - 0.023) = 95.4% confidence. Similarly, the area between the brown and the purple curves corresponds to +/- 3 Standard Deviations or (0.998 - 0.001) = 99.7% confidence. This implies that out of the 65,536 trials, about 196 (0.3%) of the estimates will be outside the brown and purple curves.

The standard way of measuring the error of cardinality sketches is what we call the *Relative Standard Error* or *RSE*. And specifically, we want to measure the RSE of the Relative Error or *RSE-RE*. This is just computing the standard deviation of the 65536 Relative Error measurements in each trial. The beauty of using the RSE is that for a 95% confidence interval we just multiply the measured RSE of the sketch by two.

However, the Standard Deviation is relative to the mean. And if the mean is different from zero due to bias, the RSE would not reflect that and the couputed error would be less than it should be.  Instead of computing the RSE, we compute the *Root Mean Square* of the Relative Error or *RMS-RE*. The RMS is related to the RSE as follows:

*RSE<sup>2</sup> = &sigma;<sup>2</sup> = 1/n &Sigma;(x<sub>i</sub> - &mu;)<sup>2</sup> =  &Sigma;((x<sub>i</sub>)<sup>2</sup>) - &mu;<sup>2</sup>*, where *x<sub>i</sub> = RE<sub>i</sub>*.

*RMS<sup>2</sup> = &Sigma;((x<sub>i</sub>)<sup>2</sup>)*

This means that the RSE can be computed from the RMS via the mean and visa-versa:

*RMS<sup>2</sup> = RSE<sup>2</sup> + &mu;<sup>2</sup>*

If the mean is indeed zero then *RMS = RSE*.

We will discuss the left-hand part of these curves shortly.

#### DS-HLL Predicted Error
The predicted error of the sketch comes from the mathematics initially formulated by Philippe Flajolet[2] where he proves that the expected RSE of an HLL sketch, using Flajolet's HLL estimator is asymptotically:

*RSE<sub>HLL</sub> = F / &radic;k*, where *F &asymp; 1.04*

Any HLL implementation that relies on the Flajolet HLL estimator will not be able to do better than this. For this large sketch of *k = 2<sup>21</sup>, RSE = 717.4 ppm*.

However, the DS-HLL sketch uses a more recent estimator called the *Historical Inverse Probability[3]* or *HIP* estimator, which has a factor *F = 0.8326*.
So the expected asymptotic *RSE = 575 ppm*, which is a 20% improvement in error. 

The gridlines in this plot are at set to be multiples of the predicted RSE of the sketch.
Note that each of the curves appears to be approaching their respective gridline.
The green curve is approaching the first positive gridline, and the orange curve is approaching the first negative gridline, etc.
These curves will actually asymptote to these gridlines, but we would have to extend the X-axis out to nearly 100 million uniques to see it.
Because this would have taken weeks to compute, we won't show this effect here, but we will be able to demonstrate this with much smaller sketches later.

#### The Measured Error of the DS-HLL Warm-up Region
Starting from the left, the error appears to be zero until the value 693 where the -3 StdDev curve (Q(.00135)) drops suddenly to about 0.14% and then gradually recovers.
This is a normal phenomenon caused by quantization error as a result of counting discrete events, and can be explained as follows.

If you were to plot the histogram of the 693 values exactly at this point you would observe 691 values of 693 and 2 values of 692. 
If there were only one value of 692, its FR would be 1/693 = 0.001443 and is below the FR of 0.00135 so the Q(0.00135) = 693 because there is still one value of 693 below the FR of 0.00135.
As soon as another estimate of 692 appears, the Q(0.00135) suddenly becomes 692, which is one off of the true value of 693. 
Thus the error measured at this point becomes *692/693 -1 = -0.001443*, which is what is shown on the graph.

The cause of two estimates being 692 instead of 693, which is an underestimate of one, is due to collisons, which can be explained by the *Birthday Paradox*.
Even though the input values are sufficiently unique (utilizing a 128 bit hash function) the precision of the warm-up cache for this sketch is a little more than 26 bits.
With this precision, the Birthday Paradox predicts a collision proportional to the square-root of *2<sup>26</sup> = 2<sup>13</sup> = 8192.
So there is roughly a 50% chance that 1 out of 8K trials will collide.
Out of 65K trials, there are about 8 chances for a single collisions to occur.  In this case 2 such collisons occured.
This is a perfectly normal occurance for any stochastic process with a finite precision.

Remember that this occured on the quantile contour representing -3 standard deviations from the mean, which would occur less than 99.865% of the time, so it is rare indeed.

Moving to the right from this first downward spike reveals that this same quantization phenomenon occurs eventually on the the Q(.02275) countour and then later on the Q(.15866) contour. 
The impact is smaller for these higher contours because the unique counts are significantly higher and the impact of the addition of one more collision is proportionally less.

Continuing to move to the right in the unique count range of about 32K to about 350K we observe that the 7 contours become parallel and smoother.
To explain what is going on in this region we need to zoom in.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllK21T16U24_closeup.png" alt="HllK21T16U24_closeup.png" />

For this zoomed in plot each gridline is again multiples of the RMS-RE (or RSE-RE), but here the factor *F = 0.408* due to discoveries of new classes of estimators[4].
With the precision of 2<sup>26</sup>, the predicted RSE is *0.408 / 2<sup>13</sup> = 49.8 ppm* or about 50 ppm.
And as you can see the 7 quantile contours nicely approach their predicted asymptotes.

All of this demonstrates that the sketch is behaving as it should and matches the mathematical predictions.

### The Error Plots for the HLL++ Sketch
With the above detailed explanation of the behavior of the DS-HLL sketch, let's see how the HLL++ sketch behaves under the same test conditions. Here *LgK = p = 21* and *sp = 25*. 

There is one caveat: Because the HLL++ sketch is so slow, I had to reduce the number of trials from 65K to 16K per trial-point and it still took over 20 hours to produce the following graph:

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllppK21T14.png" alt="HllppK21T14.png" />

Look closely at the Y-axis scale, for this plot the Y-axis ranges from -0.5% to +0.5%.  Compare the scale for first DS-HLL plot where the Y-axis ranges from -0.1725% to +0.1725%! 
The gridlines are spaced at an RSE of 717 ppm while the DS-HLL sketch RSE is at 575 ppm. However, something is clearly amiss with the HLL++ internal estimator which causes the estimates to zoom up exponentially to a high peak before finally settling down to the predicted quantile contours.

The plot below is the close-up of the warm-up region of the HLL++.  The warm-up (or sparse mode) is indeed behaving with a precision of 25 bits.
Here the predicted *RSE = 0.707 / &radic;2<sup>25</sup> = 122 ppm*, which is 2.2 times larger than that of the DS-HLL sketch at 49.8 ppm.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllppK21T14_closeup.png" alt="HllppK21T14_closeup.png" />

The HLL++ documentation claims that *sp* can be set as large as 32.  However any value larger than 25 causes dramatic failure in estimation. 
The following demonstrates what happens with a much smaller sketch of *LgK = p = 14* and *sp = 26*.

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllppK14T13SP26.png" alt="HllppK14T13SP26.png" />

The sketch fails when attempting to transition from sparse mode to normal HLL mode at about 3/4 K = 12288 uniques.
The error dives to - 35% when a sketch of this size should have an RSE of 0.8%. 
The sketch provides no warning to the user that this is happening!

### The Ultimate Measure of Merit: Error for a Given Size
As described earlier, *RSE<sub>HLL</sub> = F / &radic;k*. 
If at every trial-point along the X-axis we multiply the measured RSE by the square-root of the serialized sketch size, we will have a measure of merit of the error efficiency of the sketch given the number of bytes the sketch consumes. For HLL-type sketches and large *n* this value should be asymptotic to a constant. In HLL mode the space it consumes is constant and the error will be a constant as well. Ideally, as the sketch grows through its warm-up phases the Measure of Merit will never be larger than its asymptotic value for large *n*.

This next plot computes this Measure of Merit for both the DS-HLL sketch and the HLL++ sketch. 

<img class="doc-img-full" src="{{site.docs_img_dir}}/hll/HllVsHllppMerit.png" alt="HllVsHllppMerit.png" />

Observe that the DS-HLL sketch is lower (i.e, better merit score) than the HLL++ sketch except for the region that is roughly from 10% of *k* to about *3k/4*, where the the HLL++ sketch is better.
This is because the designers of the HLL++ sketch chose to do compression of the sparse data array every time a new value is entered into the sketch, which needs to be decompressed when an estimate is requested. The advantage of compression is that it allows the switch from sparse to normal HLL mode to be deferred to a larger fraction of *k*. But this decision comes with a severe penalty in the speed performance of the sketch. Opting out of using sparse mode will achieve higher speed performance, but at the cost of much poorer error performance at the low end and thus poor Measure of Merit.

Above *3k/4*, the HLL++ sketch is not only considerably worse, but it fails the objective of always being less than the asymptotic value. 

## Speed



****

* [1] The word "bounds" is used to define a quantile contour of the empirical error distribution. A symetrical pair of these bounds then can be used to define a confidence interval.
* [2] Philippe Flajolet, E ́ric Fusy, Olivier Gandouet, and Fre ́de ́ric Meunier. Hyperloglog: the analysis of a near-optimal cardinality estimation algorithm. In *DMTCS Conference on Analysis of Algorithms*, pages 137–156, 2007.
* [3] Edith Cohen, All-Distances Sketches, Revisited: HIP Estimators for Massive Graphs Analysis, *ACM PODS 2014*.
* [4] Kevin Lang, Back to the Future: an Even More Nearly Optimal Cardinality Estimation Algorithm. https://arxiv.org/abs/1708.06839
 











