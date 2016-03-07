---
layout: doc_page
---

## Quantiles Sketch Overview

This is a stochastic streaming sketch that enables near-real time analysis of the 
approximate distribution of real values from a very large stream in a single pass. 
The analysis is obtained using a getQuantiles() function or its inverse functions the 
Probability Mass Function from getPMF() and the Cumulative Distribution Function from getCDF().

Consider the following example of a stream of over 230 million time-spent events, which record the amount of time in milliseconds that a user spends on a page before moving to a different page by taking some action, such as a click. An exact, brute-force approach to computing various quantiles 
would require sorting all 230 million values. Assign rank values to all 230 million items from 1 to 230 million. The value at rank = 115M is the median or 50th percentile.  The normalized ranks are 
computed by dividing the rank by the size of the list.  Thus, the normalized ranks are fractions between zero and one.  Quantiles are _values_ corresponding to a normalized rank. Therefore, the median value = quantile(0.5).  The quantile(0.95) represents the value from the stream such that only 5% of all the values are equal to or larger than that value. 

The relevant pseudo-code snippets would look something like this:

    QuantilesSketch sketch = QuantilesSketch.builder().build(256); //256 gives < 1% rank error
    while ( remainingValues == true) { //stream in all the values, one by one
      sketch.update(nextValue());
    }
    
    //Query the sketch with a sorted array of 101 normalized ranks from zero to one: 
    
    double[] normRanks = {0, .01, .02, ... , .99, 1.0};
    double[] values = sketch.getQuantiles(normRanks); //result array of 101 values.

When these values are plotted against the normalized ranks we get something like this:

<img class="doc-img-full" src="{{site.docs_img_dir}}TimeSpentQuantiles.png" alt="TimeSpentQuantiles" />

This is very revealing about the distribution of values in the stream.  Just reading from the graph, the median is about 3000 and the 90th percentile is about 30,000 and so on. One can also obtain the min and max values seen by the sketch. From the values of the quantiles query, it is straightforward to compute a set of splitpoints for a histogram plot. As one can see from the quantiles plot, the values range is almost 7 orders-of-magnitude.  I chose 5 points per factor of 10
and computed 36 equally spaced (on the log axis) splitpoints ranging from 1.0 to 1E7:

    double[] splitpoints = {1.00, 1.58, ... , 1E7};
    double[] pmf = sketch.getPMF(splitpoints);

The following histogram is plotted by multiplying all the pmf values by getN(), which is the total number of events seen by the sketch.

<img class="doc-img-full" src="{{site.docs_img_dir}}TimeSpentHistogram.png" alt="TimeSpentHistogram" />

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

Similarly, there may be minor directional inconsistencies. For example, the resulting array of 
values obtained from getQuantiles(fractions[]) input into the reverse directional query 
getPMF(splitPoints[]) may not result in the original fractional values.

### Code Snippets

Code examples are best gleaned from the test code that exercises all the various capabilities of the
sketch.  Here are some brief snippets, simpler than the above graphs, to get you started.

#### Median and Top Quartile

    QuantilesSketch qs = QuantilesSketch.builder().build(); //default k = 128
    
    for (int i=0; i < 1000000; i++) { //stream length is generally unknown
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

#### Simple Frequency Histogram

    QuantilesSketch qs = QuantilesSketch.builder().build(); //default k = 128
    
    for (int i=0; i < 1000000; i++) { //stream length is generally unknown
      qs.update(i); //load the sketch
    }
    
    //create a histogram
    long n = qs.getN();
    double[] splitPoints = {100000, 500000, 900000};
    
    double[] fractionalRanks = qs.getPMF(splitPoints);
    int bins = fractionalRanks.length;
    
    double freq;
    for (int i=0; i < bins-1; i++) {
      freq = fractionalRanks[i] * n;
      System.out.println(freq + " < "+splitPoints[i]);
    }
    freq = fractionalRanks[bins-1] * n;
    System.out.println(freq + " >= "+ splitPoints[bins-2]);
    
    /* Output similar to
    98304.0  <  100000.0
    401408.0 <  500000.0
    400384.0 <  900000.0
    99904.0  >= 900000.0
    */

#### Merging

    QuantilesSketch qs1 = QuantilesSketch.builder().build(); //default k = 128
    QuantilesSketch qs2 = QuantilesSketch.builder().build();
    long size = 1000000; //generally unknown
    for (int i=0; i < size; i++) { //update each value into the sketch
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
