---
layout: doc_page
---

## Quantiles Accuracy and Size

The accuracy of this sketch is a function of the configured value <i>k</i>, which also affects
the overall size of the sketch. Accuracy of this quantile sketch is always with respect to
the normalized rank.  A <i>k</i> of 256 produces a normalized, rank error of less than 1%. 
For example, the median value returned from getQuantile(0.5) will be between the actual values 
from the hypothetically sorted array of input values at normalized ranks of 0.49 and 0.51, with 
a confidence of about 99%.

<pre>
Table Guide for QuantilesSketch Size in Bytes and Approximate Error:
          K =&gt; |      16      32      64     128     256     512   1,024
    ~ Error =&gt; | 12.145%  6.359%  3.317%  1.725%  0.894%  0.463%  0.239%
             N | Size in Bytes ->
-------------------------------------------------------------------------
             0 |       8       8       8       8       8       8       8
             1 |      72      72      72      72      72      72      72
             3 |      72      72      72      72      72      72      72
             7 |     104     104     104     104     104     104     104
            15 |     168     168     168     168     168     168     168
            31 |     296     296     296     296     296     296     296
            63 |     424     552     552     552     552     552     552
           127 |     552     808   1,064   1,064   1,064   1,064   1,064
           255 |     680   1,064   1,576   2,088   2,088   2,088   2,088
           511 |     808   1,320   2,088   3,112   4,136   4,136   4,136
         1,023 |     936   1,576   2,600   4,136   6,184   8,232   8,232
         2,047 |   1,064   1,832   3,112   5,160   8,232  12,328  16,424
         4,095 |   1,192   2,088   3,624   6,184  10,280  16,424  24,616
         8,191 |   1,320   2,344   4,136   7,208  12,328  20,520  32,808
        16,383 |   1,448   2,600   4,648   8,232  14,376  24,616  41,000
        32,767 |   1,576   2,856   5,160   9,256  16,424  28,712  49,192
        65,535 |   1,704   3,112   5,672  10,280  18,472  32,808  57,384
       131,071 |   1,832   3,368   6,184  11,304  20,520  36,904  65,576
       262,143 |   1,960   3,624   6,696  12,328  22,568  41,000  73,768
       524,287 |   2,088   3,880   7,208  13,352  24,616  45,096  81,960
     1,048,575 |   2,216   4,136   7,720  14,376  26,664  49,192  90,152
     2,097,151 |   2,344   4,392   8,232  15,400  28,712  53,288  98,344
     4,194,303 |   2,472   4,648   8,744  16,424  30,760  57,384 106,536
     8,388,607 |   2,600   4,904   9,256  17,448  32,808  61,480 114,728
    16,777,215 |   2,728   5,160   9,768  18,472  34,856  65,576 122,920
    33,554,431 |   2,856   5,416  10,280  19,496  36,904  69,672 131,112
    67,108,863 |   2,984   5,672  10,792  20,520  38,952  73,768 139,304
   134,217,727 |   3,112   5,928  11,304  21,544  41,000  77,864 147,496
   268,435,455 |   3,240   6,184  11,816  22,568  43,048  81,960 155,688
   536,870,911 |   3,368   6,440  12,328  23,592  45,096  86,056 163,880
 1,073,741,823 |   3,496   6,696  12,840  24,616  47,144  90,152 172,072
 2,147,483,647 |   3,624   6,952  13,352  25,640  49,192  94,248 180,264
 4,294,967,295 |   3,752   7,208  13,864  26,664  51,240  98,344 188,456
</pre>

The following graphs illustrate the ability of the QuantilesSketch to characterize value distributions.

n = 1024 values was generated from Random's nextGaussian().  These values were then sorted and assigned
their actual normalized ranks from 0 to (n-1)/n.  This array of ranks become the actual ranks array
and the associated array of Gaussian values become the actual quantiles array.

A QuantilesSketch was created with K = 32 and fed the non-sorted actual quantiles values. 
After loading the sketch, a getCDF(sorted values array) produces an ordered array of estimated ranks.

These estimated result ranks (red) are then plotted against the actual ranks (black) and plotted in the following 
graph.  The upper bound error is plotted in blue and the lower bound error is ploted in green.

The error of the estimation from the sketch is called "Epsilon" and is always with respect to the 
rank axis. It is plotted as a visual metric on the graph to illustrate its size. 

This size of this sketch if stored, would be about 1832 bytes.

<img class="doc-img-half" src="{{site.docs_img_dir}}/QuantilesCDF.png" alt="QuantilesCDF" />

A getQuantiles(sorted actual rank array) produces an ordered array of estimated values. 
Plotting these values against the actual values produces the inverse CDF plot as follows:

<img class="doc-img-half" src="{{site.docs_img_dir}}/QuantilesInverseCDF.png" alt="QuantilesInverseCDF" />

Examining the absolute rank error vs the actual rank produces the following graph.  

<img class="doc-img-half" src="{{site.docs_img_dir}}/QuantilesCDFAbsRankError.png" alt="QuantilesCDFAbsRankError" />

All of these plots were generated from one set of values, and is not a test of the error bounds. 
To do that would require repeating this test thousands of times and then plotting the quantiles of the 
distribution of values for each of the 1024 points of the above graphs.  



