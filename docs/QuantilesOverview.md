---
layout: doc_page
---

##Quantiles Overview

This is a stochastic streaming sketch that enables near-real time analysis of the 
approximate distribution of real values from a very large stream in a single pass. 
The analysis is obtained using a getQuantiles() function or its inverse functions the 
Probability Mass Function from getPMF() and the Cumulative Distribution Function from getCDF().

Consider a large stream of one million values such as packet sizes coming into a network node.
The absolute rank of any specific size value is simply its index in the hypothetical sorted 
array of values.
The normalized rank is the absolute rank divided by the stream size, in this case one million. 
The value corresponding to the normalized rank of 0.5 represents the 50th percentile or median
value of the distribution, or getQuantile(0.5).  Similarly, the 95th percentile is obtained from 
getQuantile(0.95).

If you have prior knowledge of the approximate range of values, for example, 1 to 1000 bytes,
you can obtain the PMF from getPMF(100, 500, 900) that will result in an array of 
4 fractional values such as {.4, .3, .2, .1}, which means that 
40% of the values were &lt; 100, 
30% of the values were &ge; 100 and &lt; 500,
20% of the values were &ge; 500 and &lt; 900, and
10% of the values were &ge; 900.
A frequency histogram can be obtained by simply multiplying these fractions by getN(), 
which is the total count of values received. 
The getCDF(*) works similarly, but produces the cumulative distribution instead.


There is more documentation available on 
<a href="http://datasketches.github.io">DataSketches.GitHub.io</a>.

This is an implementation of the Low Discrepancy Mergeable Quantiles Sketch, using double 
values, described in section 3.2 of the journal version of the paper "Mergeable Summaries" 
by Agarwal, Cormode, Huang, Phillips, Wei, and Yi. 
<a href="http://dblp.org/rec/html/journals/tods/AgarwalCHPWY13"></a>

This algorithm is independent of the distribution of values, which can be anywhere in the
range of the IEEE-754 64-bit doubles. 

This algorithm intentionally inserts randomness into the sampling process for values that
ultimately get retained in the sketch. The result is that this algorithm is not 
deterministic. For example, if the same stream is inserted into two different instances of this 
sketch, the answers obtained from the two sketches may not be be identical.

Similarly, there may be directional inconsistencies. For example, the resulting array of 
values obtained from getQuantiles(fractions[]) input into the reverse directional query 
getPMF(splitPoints[]) may not result in the original fractional values.

