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
## VarOpt Sampling

VarOpt sampling allows one to construct a random sample of weighted items from a stream. Like with reservoir sampling, varopt sampling does not require knowing the final length of the stream in advance. As with all sketches in the library, varopt sampling sketches can be efficiently unioned.

The name VarOpt refers to the sketch's property of producing estimates of subset sums with optimal variance for a given sketch size, <tt>k</tt>. If we apply a predicate to the items in a varopt sample and, the sum of weights (which may be adjusted by the sketching process) of items matching that predicate will estimate the sum of weights of matching items from the entire stream with provably minimal variance.

The Sketches Library's VarOpt implementation is designed around generic objects:

* VarOptItemsSketch&lt;T&gt;

    This sketch provides a random sample of items of type &lt;T&gt; from the stream of weighted items.
    An item's inclusion probability will usually be proportional to its weight, with some
    important technical caveats to ensure the optimal variance property.

    If the user needs to serialize and deserialize the resulting sketch for storage or transport, 
    the user must also extend the <tt>ArrayOfItemsSerDe</tt> interface. Three examples of 
    extending this interface are included for <tt>Long</tt>,
    <tt>String</tt>, and <tt>Number</tt>: <tt>ArrayOfLongsSerDe</tt>, <tt>ArrayOfStringsSerDe</tt>,
    and <tt>ArrayOfNumbersSerDe</tt>.
    

### Space Usage

The reservoir is initialized with a parameter <tt>k</tt> indicating the maximum number of items 
that can be stored in the reservoir. In contrast to some other sketches in this library, the size does
not need to be a power of 2.

When serialized, these sketches use 32 bytes of header data in addition to the serialized size of the
items in the sketch. VarOpt unions may require some extra metadata beyond the regular header.


### Updating the sketch with new items

As with reservoir sampling, varopt sampling does not maintain a hash list of items or associate additional metadata with them;
each item presented to the sketch is handled independently. Consequently, the resulting sample may have
duplicate items if the input stream contains duplicates.


### More on VarOpt Sampling

The basic VarOpt algorithm was first presented by Cohen et al[1]. We have modified and extended that work to allow for unions of VarOpt samples, producing a result that remains a valid VarOpt sketch.

The underlying goal of VarOpt sampling is to provide the best possible estimate of subset sums of items in the sample. As an example, we might select a sample o size <tt>k</tt> from the ~3200 counties (a political administrative region below the level of a state) in the United States, using the county population as the weight. We could then apply a predicate to our sample -- for instance, counties in the state of California -- and sum the resulting weights. That sum is our estimate of the total population of the state. The weights used when computing subset sums will, in general, be adjusted values rather than the original input weights.

Unlike standard reservoir sampling, where each sample is considered independently, VarOpt attempts to minimize the total variance in the sample by selecting samples in a way such that they may be <em>negatively</em> correlated. This produces better estimates for subset sums, but does mean that our random sample is not necessarily uniform and that an item's inclusion probability is not a simple function of the item weight.


#### VarOpt Intuition

A mathematically rigorous description of VarOpt sampling is available in the previously cited paper. Here, we will provide a rough idea of what happens in a VarOpt sketch, under the assumption that our sketch has received enough items to have begun sampling.

In VarOpt sampling, we conceptually divide the input items into a <em>heavy</em> or <em>exact</em> group and a <em>light</em> group. The heavy items have large enough a weight that we will always include them our sketch, while the the light items are proportionally sampled. There is also a cutoff weight, known as tau, that divides heavy from light items and is equal to the sum total weight of all light items the sketch has ever seen divided by the current number of light items in the sketch. When an item is added to the sketch, we first check if its weight is greater than tau; if so, it is added as a heavy item, otherwise it is (probabilistically) added as a light item. If we accept the new item and our sketch is already full, then we need to perform some bookkeeping, updating the value of tau and possibly moving some heavy items to the light region, and then selecting a light item to evict.

From this brief description, we can already identify several interesting properties of VarOpt sketches. First, for a given input set, the heavy items are deterministic regardless of insertion order. Second, the value of tau is monotonically increasing.

When extracting items from a sketch, the heavy items retain their original weights. For the light items, we return a uniform adjusted weight. Although somewhat counter-intuitive because a very light input item receives the same weight as one just barely too light to qualify for the heavy region, this adjusted weight is important for providing a variance-optimal subset sum estimate.

When using VarOpt with uniform input weights, for instance giving all items a weight of 1.0, the sketch will perform standard reservoir sampling. Similarly, if operating over an infinite stream of items with finite weights, there will also be no heavy items and the sketch will return a standard weighted sample of items.

[1]: E. Cohen, N. G. Guffield, H. Kapla, C. Lund, M. Thorup, <em>Efficient Stream Sampling for Variance-Optimal Estimation of Subset Sums</em>, SIAM J. Comput. 40(5): 1402-1431, 2011.
