---
layout: doc_page
---
[Back](KeyFeatures.html)

##Update Speed
The following graph illustrates the update speed of 3 different sketches from the library:

<img class="doc-img-full" src="{{site.docs_img_dir}}UpdateSpeed.png" alt="UpdateSpeed" />


 * The Heap Alpha Sketch (red) is the fastest sketch and primarily focused on real-time streaming environments and operates on-heap.
Its average update speed from 8 uniques to 8 million uniques is the straight red line and is a little over 80 million updates per second.
 * The Heap QuickSelect sketch (blue) is next, also on-heap, averages over 65 million updates per second.
 * The Direct QuickSelect sketch (green) runs off-heap in direct, native memory and averages over 47 million updates per second.

###How this graph was generated
The goal of this measurement was to measure the limits of how fast these sketches could update data from a continuous data stream not limited by other system overhead, string or array processing. In order to remove random noise from the plots, each point on the graph represents an average of many trials.  For the low end of the graph the number of trials per point is 2^13 or 8K. At the high end at 8 million uniques per trial the number of trials is 2^4 or 16.  The plots go off the top of the chart below 8 uniques per trial due to Java overhead in instantiating the class in memory and warm-up of the JIT compiler.  

As one can see from the graph, after about 64 uniques per trial, the speed of updating becomes essentially flat. As the internal hash table begins to fill up to its maximum size, specified by the chosen sketch size of 64K, the updating speed naturally slows down.  The different sketch algorithms have different strategies for managing the internal hash-table rebuilds and the Alpha Sketch is by far the fastest where it is able to defer its first rebuild until about 524K uniques as shown by the blip on the red plot.  Meanwhile, after about 128K all the sketches start filtering out input candidates by the variable sampling probability, theta.  As more and more uniques are presented to the sketch a larger fraction of the input gets filtered so the sketch speeds up and asymptocially approaches the speed of the hash function, which is about 6-7 nanoseconds.  At 8 million uniques the Alpha Sketch has reached a speed of over 142 million updates per second.

Very few systems would actually be able to feed a single sketch at this rate so these rates represent an upper bound and not realistic system update rates. Nonetheless, this demonstrates that the sketches would consume very little of an overall system's budget for updating, if there was one.

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

[Back](KeyFeatures.html)