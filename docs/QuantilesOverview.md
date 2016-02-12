---
layout: doc_page
---

## Quantiles Overview

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

### Code Snippets

Code examples are best gleaned from the test code that exercises all the various capabilities of the
sketch.  Here are some brief snippets to get you started.

#### Median and Top Quartile

<div class="highlight"><pre><code class="language-text" data-lang="text"
>QuantilesSketch qs = QuantilesSketch.builder().build(); //default k = 128

for (int i=0; i &lt; 1000000; i++) { //stream length is generally unknown
  qs.update(i); //load the sketch
}

double median = qs.getQuantile(0.5);
double topQuartile = qs.getQuantile(0.75);
System.out.println("Median = " + median);
System.out.println("75%ile = " + topQuartile);

/* Output similar to
Median = 500087.0
75%ile = 749747.0
*/
</code></pre></div>

#### Frequency Histogram

<div class="highlight"><pre><code class="language-text" data-lang="text"
>QuantilesSketch qs = QuantilesSketch.builder().build(); //default k = 128

for (int i=0; i &lt; 1000000; i++) { //stream length is generally unknown
  qs.update(i); //load the sketch
}

//create a histogram
long n = qs.getN();
double[] splitPoints = {100000, 500000, 900000};

double[] fractionalRanks = qs.getPMF(splitPoints);
int bins = fractionalRanks.length;

double freq;
for (int i=0; i &lt; bins-1; i++) {
  freq = fractionalRanks[i] * n;
  System.out.println(freq + " &lt; "+splitPoints[i]);
}
freq = fractionalRanks[bins-1] * n;
System.out.println(freq + " &ge; "+ splitPoints[bins-2]);

/* Output similar to
98304.0 &lt; 100000.0
401408.0 &lt; 500000.0
400384.0 &lt; 900000.0
99904.0 &ge; 900000.0
*/
</code></pre></div>

#### Merging

<div class="highlight"><pre><code class="language-text" data-lang="text"
>QuantilesSketch qs1 = QuantilesSketch.builder().build(); //default k = 128
QuantilesSketch qs2 = QuantilesSketch.builder().build();
long size = 1000000; //generally unknown
for (int i=0; i &lt; size; i++) { //update each value into the sketch
  qs1.update(i);
  qs2.update(i + 1000000);
}

Union union = Union.builder.build(); //creates a virgin Union
union.update(qs1);
union.update(qs2);

QuantilesSketch qs3 = union.getResult();
System.out.println(qs3.toString()); //Primarily for debugging

/* Output similar to
### HeapQuantilesSketch SUMMARY: 
   K                            : 128
   N                            : 2,000,000
   Seed                         : 0
   BaseBufferCount              : 128
   CombinedBufferAllocatedCount : 1,920
   Total Levels                 : 13
   Valid Levels                 : 6
   Level Bit Pattern            : 1111010000100
   Valid Samples                : 896
   Buffer Storage Bytes         : 15,360
   Preamble Bytes               : 36
   Normalized Rank Error        : 1.725%
   Min Value                    : 0.000
   Max Value                    : 1,999,999.000
### END SKETCH SUMMARY
*/
</code></pre></div>
