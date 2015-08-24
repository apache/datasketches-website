---
layout: doc_page
---

##Update Speed

###Resize Factor = X1
The following graph illustrates the update speed of 3 different sketches from the library: the Heap QuickSelect Sketch, the Direct QuickSelect Sketch, and the Heap Alpha Sketch.
The X-axis is the number of unique values presented to a sketch. The Y-axis is the average time to perform an update.  It is computed as the total time to update X-uniques divided by X-uniques.

The very high values prior to 4 uniques are due to Java overhead and JVM warmup.  The hump in the middle of the graph is due to the internal hash table filling up and forcing an internal rebuild and reducing theta.  For this plot the sketches were configured with <i>k</i> = 4096. 
The peaks on the QS plots represent successive reqbuilds.  The downward slope on the right side of the hump is the sketch speeding up because it is rejecting more and more incoming hash values due to the continued reduction in the value of theta.
The Alpha sketch uses a more advanced hash table update algorithm that defers the first rebuild until after theta has started decreasing.  This is the little spike just to the right of the hump.
As the number of uniques continue to increase the update speed of the sketch becomes asymptotic to the speed of the hash function itself, which is about 5 nanoseconds.  

<img class="doc-img-full" src="{{site.docs_img_dir}}UpdateSpeed.png" alt="UpdateSpeed" />

 * The Heap Alpha Sketch (red) is the fastest sketch and primarily focused on real-time streaming environments and operates only on the Java heap.
In this test setup and performing an "average" over all the test points from 8 to 8 million uniques the Alpha sketch update rate averages about 100 million updates per second.
 * The Heap QuickSelect sketch (blue) is next, also on-heap, averages about 81 million updates per second.
 * The Direct QuickSelect sketch (green) runs off-heap in direct, native memory and averages about 63 million updates per second. 

####How this graph was generated
The goal of this measurement was to measure the limits of how fast these sketches could update data from a continuous data stream not limited by other system overhead, string or array processing. In order to remove random noise from the plots, each point on the graph represents an average of many trials.  For the low end of the graph the number of trials per point is 2^13 or 8K. At the high end at 8 million uniques per trial the number of trials is 2^4 or 16.  

The sketches were configured with the optional <i>ResizingFactor = X1</i>, which means the internal cache was initially configured to be at full size at the start so there would not be any internal resizing of the cache during the test. See the Plot Parameters below. 

It needs to be pointed out that these tests were designed to measure the maximum update speed under ideal conditions so "your mileage may vary"!
Very few systems would actually be able to feed a single sketch at this rate so these plots represent an upper bound and not realistic update rates in more complex systems environments. Nonetheless, this demonstrates that the sketches would consume very little of an overall system's budget for updating, if there was one, and are quite suitable for real-time streams.

The graphs on this page were generated using the SketchPerformance program in the com.yahoo.sketches.theta package as part of the test hierarchy. 
There is more documentation with the code.

####Plot Parameters
Note: the prefix "lg" is shorthand notation for Log\_base\_2.

Sketch Profile |
---------------|------------
lgK, (K)  | 12, (4096)
Family  | ALPHA or QUICKSELECT
ResizeFactor | X1
Direct  | True or False

Trials Profile |
---------------|------------
lgMinTrials  | 4
lgMaxTrials  | 13
lgMaxU       | 23
PPO          | 16

###Resize Factors = X1, X2 and X8
To illustrate how the the optional <i>Resize Factor</i> affects performance refer to the following graph.  All three plots were generated using the Heap QuickSelect Sketch but with different Resize Factors.

<img class="doc-img-full" src="{{site.docs_img_dir}}UpdateSpeedWithRF.png" alt="UpdateSpeedWithRF" />

* The blue curve is the same as the blue curve in the graph at the top of this page. 
It was generated with <i>ResizeFactor = X1</i>, which means the sketch cache was initially created at full size, thus there is no resizing of the cache during the life of the sketch.  (There will be <i>rebuilding</i> but not resizing.)
* The red curve is the same sketch but configured with <i>ResizeFactor = X2</i>, which means the sketch cache is initially created at a small size (enough for about 16 entries), and then when the sketch needs more space for the cache, it is resized by a factor of 2. Each of these resize events are clearly seen in the plot as sharp spikes in the speed performance where the sketch must pause to resize and reconstruct the hash table in the new cache.  These spikes fall at factors of 2 along the X-axis (with the exception of the last interval, which is a factor of 4 for reasons too complicated to explain here.)
* The green curve is the same sketch but configured with <i>ResizeFactor = X8</i>.  An RF = X4 is also available but not shown.  

As one would expect the overall speed of the RF = X2 sketch is slower than the RF = X1 sketch and the RF = X8 sketch is inbetween due to the amount of time the sketch spends in resizing the cache.

The tradeoff here is the classic memory size versus speed.  Suppose you have millions of sketches that need to be allocated and your input data is highly skewed (as is often the case).  Most of the sketches will only have a few entries and only a small fraction of all the sketches will actually go into estimation mode and require a full-sized cache.  The Resize Factor option allows a memory allocation that would be orders of magnitude smaller than would be required if all the sketches had to be allocated at full size.  The default Resize Factor is X8, which is a nice compromise between X1 and X2.

####Plot Parameters

Sketch Profile |
---------------|------------
LgK, (K)  | 12, (4096)
Family  | QUICKSELECT
ResizeFactor | X1, X2 or X8
Direct  | False


###Measurement System
  Model Name:	MacBook Pro<br>
  Model Identifier:	MacBookPro10,1<br>
  Processor Name:	Intel Core i7<br>
  Processor Speed:	2.6 GHz<br>
  Number of Processors:	1<br>
  Total Number of Cores:	4<br>
  L2 Cache (per Core):	256 KB<br>
  L3 Cache:	6 MB<br>
  Memory:	16 GB 1600 MHz DDR3
