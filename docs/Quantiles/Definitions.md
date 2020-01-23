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
# Definitions for Quantiles Studies

The goal of this article is to compare the accuracy performance of the Druid built-in Approximate Histogram to an exact, brute-force computation using actual data extracted from one of our back-end servers. 


## Hypothetically Sorted Stream (HSS) of Values
Consider an input stream of *n* values. Now imagine if this input stream was sorted from the smallest to largest value. This is what we call the *hypothetical sorted stream* (HSS) of input values. We call it *hypothetical* because we never actually perform this sort (it would be very expensive) so we ask you to imagine it. 

## Rank and Mass
We define the *natural rank* of a value in this HSS as its zero-based index that ranges from 0 to *n-1*. If we divide the natural rank by *n*, it becomes the *normalized or fractional rank*, and would range from 0 to *(n-1)/n* and be in the interval [0, 1). In our documentation when we refer to *rank* we are referring to the *normalized or fractional rank* and not the *natural rank*.

This *normalized rank* is closely associated with the concept of *mass*. If there are 1 million values in the HSS, then the value associated with the rank 0.5 represents the median value, or the center-point of the HSS *mass*, where half of the values (500,000) are below the median and half are above. 

## getQuantile(rank) and getRank(value)
We now have two hypothetical streams of values, a HSS of values and the stream of ranks where each value in the HSS is assigned a unique rank. 
The definiton of *quantile* is the *value* associated with a particular *rank*. So the function *getQuantile(r)* returns the value associated rank *r*.
Similarly, the function *getRank(v)* returns the rank associated with the value *v*.

If a particular value <i>v</i> is repeated several times in the HSS, there is obviously a range of ranks that are assigned to each of the identical <i>v</i> values. This results in asymmetry between *v = getQuantile(r)* and *r = getRank(v)*. So how do we answer the following two queries when there are duplicate values in the HSS?? 

* What rank is returned from the query <i>getRank(v)</i>? 
* What value is returned from the query <i>getQuantile(r)</i>? 

It turns out that there are 3 different rules for *getRank(v)* that you will find in the literature when *v* has duplicates in the HSS:

* <i>min rank rule</i>: return the smallest rank associated with the range of duplicate values <i>v</i>.
* <i>max rank rule</i>: return the largest rank associated with the range of duplicate values <i>v</i>.
* <i>mid rank rule</i>: return the rank closest to the midpoint of the range of duplicate values <i>v</i>.

In our analysis and algorithms we use the <i>min rank rule</i>.

If the value provided to *getRank(v)* does not exist in the HSS, the rule is to return the rank of the next largest value greater than *v*.
This is the rank that would include *v* as if it existed.

For example: given the following HSS and associated ranks:

```
Hypothetically Sorted Stream (HSS)
   Val  Natural Rank  Normalized Rank (divided by n = 9)
  1.00             0             0.00
  2.00             1             0.11
  3.00             2             0.22
  5.00             3             0.33
  5.00             4             0.44
  5.00             5             0.56
  7.00             6             0.67
  8.00             7             0.78
  9.00             8             0.89
```

The *getRank(v)* will return for each of the following:

```
getRank(v)
   Val  Rank
  0.00  0.00
  0.50  0.00
  1.00  0.00
  1.50  0.11
  2.00  0.11
  2.50  0.22
  3.00  0.22
  3.50  0.33
  4.00  0.33
  4.50  0.33
  5.00  0.33
  5.50  0.67
  6.00  0.67
  6.50  0.67
  7.00  0.67
  7.50  0.78
  8.00  0.78
  8.50  0.89
  9.00  0.89
```

For the *getQuantile(r)* query:

* <i>Strictly less-than quantile rule</i>: return the value associated with the largest rank that is strictly less-than *r*.

The *getQuantile(r)* will return for each of the following:

```
getQuantile(r)
  Rank   Val
  0.00  1.00
  0.10  1.00
  0.20  2.00
  0.30  3.00
  0.40  5.00
  0.50  5.00
  0.60  5.00
  0.70  7.00
  0.80  8.00
  0.90  9.00
  1.00  9.00
```

### Accuracy
Accuracy for quantiles sketches is measured with respect to the *rank* only.  Not the values.  

A specified accuracy of 1% at the median (rank = 0.50) means that the true value (if you could extract it from the HSS) should be 
between *getQuantile(0.49)* and *getQuantile(0.51)*. Note that this is an absolute rank error and not a relative rank error. 
Measured at the 10th percentile means that the true value (from the HSS) should be 
between *getQuantile(0.09)* and *getQuantile(0.11)*. 

### The Sketch and Data Independence
A *sketch* is an implementation of a *streaming algorithm*. By definition, a sketch has only one chance to examine each item of the stream.  It is this property that makes a sketch a *streaming* algorithm and useful for real-time analysis of very large streams that may be impractical to actually store. 

We also assume that the sketch knows nothing about the input data stream: its length, the range of the values or how the values are distributed. If the authors of a particular algorithm require the user to know any of the above attributes of the input data stream in order to "tune" the algorithm, the algorithm is not a sketch as it would require multiple passes through the input data in order to produce correct results.

The only thing the user needs to know is how to extract the values from the stream so that they can be fed into the sketch. 
It is also reasonable that the user knows the *type* of values in the stream: e.g., are they alphanumeric strings, numeric strings, or numeric primitives. These properties may determine the type of sketch to use as well as how to extract the appropriate quantities to feed into the sketch.

