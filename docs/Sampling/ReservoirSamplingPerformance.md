---
layout: doc_page
---
<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->
## Reservoir Sampling Performance

### Update Speed

The following table shows the update performance of a reservoir sketch. We look at 4 average per-item update times:
The first <tt>k</tt> items when initially filling the reservoir, the next <tt>k</tt> items when the probability of
accepting a new item is > 0.5, any remaining items where the acceptance probability is < 0.5, and the overall average.

Filling|p(accept)>=0.5|p(accept)<0.5|Overall
-----|-----|-----|-----
24.3ns|24.8ns|24.5ns|24.5ns

These results were generated with items consisting of <tt>Long</tt>s k=2^20 and n=2^24, and are the averaged result after
1000 priming iterations (not counted) and an additional 1000 test iterations. Tests were performed on a 2012 MacBook Pro.

### Union Speed

We always return sufficient information with our reservoir sample sketches to be able to union reservoirs to obtain
a reservoir sample over the combined inputs. Below, we present merge speeds for sketches for several values of <tt>k</tt>.

k | Mean (ms) | Stdev (ms)
-------|-------|----------
100    | 0.002 | 0.0004
1000   | 0.018 | 0.0009
10000  | 0.189 | 0.066
100000 | 1.846 | 0.232

Here, we used an item type of <tt>Integer</tt>, although the specific type seems to have no impact on performance. Because each
reservoir stores a set of <tt>Object</tt> pointers, we believe this will be true regardless of the object type, subject to memory
interactions for item types with large class objects. Each result was again produced with 1000 iterations. Each trial
unioned a set of 1000 sketches with a uniform <tt>k</tt> but filled based on a lognormal distribution around <tt>k</tt>; some reservoirs
had <tt>n < k</tt> while other reservoirs had moved into sampling mode.

Consistent with reservoir updates, we can see that union speed has a nearly linear relationship with reservoir size.

### Comparison with Apache DataFu

[DataFu](datafu.apache.org) is an Apache Incubator project that includes a set of Pig UDFs for various data processing purposes.
Because the focus of the Sketches library is on sublinear or bounded-size algorithms, compared the performance of our reservoir
sampling Pig UDF against DataFu's basic reservoir sampling, which uses a pre-specified maximum size.

We tested the two Pig UDFs using the same methodology: Generate input data, instantiate the UDF object, and time calls to <tt>exec()</tt>.
Lacking a dedicated Hadoop cluster for proper experimental control, tests were run directly in Java on a local (and rather aged) box.
The relative performance was consistent across various values of <tt>k</tt> so we present <tt>k=2048</tt> as a representative example.

![Graph comparing Sketches to DataFu for k=2048]({{site.docs_img_dir}}/sampling/compare_datafu.k2048.png){:width="600px"}

Because the data is input as a Pig <tt>DataBag</tt>, the UDF knows the input data size when starting processing. If the total input size is
less than the maximum reservoir size, the entire input data can be returned, which requires handling only a pointer to the DataBag,
which is a constant time independent of the size of the bag. As a result, the time per update decreases with increasing data size until
the total input data reaches <tt>k</tt>. While DataFu returns only the input bag, the Sketches library returns a <tt>Tuple</tt> of (n, k, samples)
to allow for future unioning; the need to allocate a new container explains the time difference between the systems in this time range.

When we must sample data, the Sketches library outperforms DataFu. DataFu's underlying data is stored as a priority queue, which
allows for compact code at the expect of more expensive updates. The performance difference is largest when <tt>n</tt> is only slightly
larger than <tt>k</tt>, which corresponds to the region in which new items are frequently accepted into the reservoir. With a heap-based
implementation, each accepted item incurs an update cost of log(k). As the number of items grows, the cost of processing each
data element becomes relatively larger and dominates the total cost, which is why DataFu's performance starts to asymptote towards
that of the Sketches library.

### Code Location

All testing code for these results is available in the [sketches-misc](https://github.com/DataSketches/sketches-misc) repository.
