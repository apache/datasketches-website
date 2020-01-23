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
# KLL sketch vs t-digest

The goal of this article is to do an objective comparison of the KLL quantiles sketch implemented in this library and the <a href="https://github.com/tdunning/t-digest">t-digest</a>.

## Versions

* KLL sketch form <a href="https://github.com/DataSketches/sketches-core/releases/tag/sketches-core-0.11.1">sketches-core-0.11.1</a>
* <a href="https://github.com/tdunning/t-digest/commit/01ea144ca865361be6786fd502bb554c75105e3c">t-digest code as of Sep 14, 2017</a> (there is no published jar)

## Definition of rank

Consider the stream of <i>n</i> values presented to the sketch. The rank of a value would be its index in the sorted version of the input stream. If we divide the rank by <i>n</i>, it becomes the normalized rank, which is between 0 and 1. Suppose a particular value <i>x</i> is repeated several times. There is a range of ranks in the hypothetical sorted stream that might be assigned to <i>x</i>:

* <i>min rule</i>: mass of the distribution of values less than <i>x</i>
* <i>max rule</i>: mass of the distribution of values less than or equal to <i>x</i>
* <i>mid rule</i>: halfway between the <i>min rule</i> and the <i>max rule</i>

KLL sketch uses the <i>min rule</i>. If one value is added to the sketch (even repeatedly), its rank is 0.

It is not clear what rule t-digest uses. There is a discrepancy between the definition of rank in Javadoc and the implementation. The definition reads (above cdf(x) method in TDigest.java): "returns the fraction of all points added which are <= x". This would be the <i>max rule</i>. However, there is a helping method in tests that implements the <i>mid rule</i>. Moreover, experiments with streams of the same repeated value show that the <i>min rule</i> might explain the results better (see the plot below), but not exactly. For completeness the comparison was done using all tree rules for t-digest.

This plot is to validate the basic assumptions about the sketches. The same repeated value was used as the input. The expected rank error is 0%.

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-error-same-value.png" alt="same value rank error plot" />

## Size

The starting point in this comparison is a choice of parameters for KLL sketch and t-digest in such a way that they have approximately the same size. It is quite difficult to measure the size of a Java object in memory, therefore the serialized size was used as the best available measure of size, which is also important for many practical applications in large systems. There are two ways to serialize t-digest: asBytes() and asSmallBytes(). It was found that t-digest with compression=100 (recommended in the documentation) approximately matches the size of KLL with the default parameter K=200 if the asBytes() method is used, and compression=200 approximately matches KLL if serialized using asSmallBytes() method. For completeness the comparison was done at both levels of compression.

The input for the following size measurements was generated using uniform random values from 0 to 1 (Random.nextFloat() for KLL sketch and Random.nextDouble() for t-digest)

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-size.png" alt="KLL200 vs TD100 serialized size plot" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td200-size.png" alt="KLL200 vs TD200 serialized size plot" />

## Accuracy

### Specification of error of KLL sketch

The error &epsilon; of the KLL sketch is specified as a fraction of the normalized rank of the hypothetical sorted stream of values presented to the sketch.

Suppose the sketch is queried using getRank() method to obtain the estimated rank <i>r<sub>est</sub></i> of a value presented to the sketch. If the true rank of this value is <i>r<sub>true</sub></i>, the estimated rank must be within the specified rank error &epsilon; of the true rank with 99% probability:
&#124;&nbsp;<i>r<sub>est</sub></i>&nbsp;-&nbsp;<i>r<sub>true</sub></i>&nbsp;&#124;&nbsp;<&nbsp;&epsilon;

KLL sketch has methods to obtain normalized rank error for both single-sided (rank) and double-sided (PMF) queries. The provided rank error is the best fit to 99th percentile of empirically measured maximum error in thousands of trials.

Note that KLL sketch does not make any assumptions about the distribution of values. In principle the values do not even have to be numeric. The only requirement is the "less than" relation in the value space. The implementation under test is specialized for values of type float, but the algorithm does not take into account any "distance" in the value space. Therefore there are no error guarantees in terms of values, only the bounds are provided: lower and upper bound values such that the true quantile should be found somewhere between them with 99% probability.

### Specification of error of t-digest

The error of the t-digest is specified, similarly to KLL sketch, as a fraction of the normalized rank.

The t-digest algorithm is taking into account the distance between values, but does not provide any accuracy guarantees in the value space.

The implementation of t-digest (as of this writing) doesn't have any methods to obtain rank error or error bounds.

### Accuracy plots

#### Input

* Uniform distribution between 0 and 1 (Random.nextFloat() for KLL sketch and Random.nextDouble() for t-digest)
* Gaussian distribution with mean 0 and standard deviation 1 (Random.nextGaussian())
* Growing blocks of repeated values (starts with 1 and 0.001 probability of incrementing, after every increment the probability is decreased by the factor of 0.98)

#### Vertical scale

Since t-digest shows rank error up to 100%, Y axis was plotted using logarithmic scale to be able to see details in the sub 1% region. Zero values were substituted with 10<sup>-2</sup>.

#### t-digest compression=100

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-error-uniform.png" alt="KLL200 vs TD100 rank error uniform input" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-error-gaussian.png" alt="KLL200 vs TD100 rank error gaussian input" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-error-blocky.png" alt="KLL200 vs TD100 rank error blocky input" />

#### t-digest compression=200

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td200-error-uniform.png" alt="KLL200 vs TD200 rank error uniform input" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td200-error-gaussian.png" alt="KLL200 vs TD200 rank error gaussian input" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td200-error-blocky.png" alt="KLL200 vs TD200 rank error blocky input" />

## Speed

The input for the following speed measurements was generated using uniform random values from 0 to 1 (Random.nextFloat() for KLL sketch and Random.nextDouble() for t-digest)

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-td200-update-time.png" alt="KLL200 vs TD100 and TD200 update time" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-serialize-time.png" alt="KLL200 vs TD100 serialize time" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td200-serialize-time.png" alt="KLL200 vs TD200 serialize time" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td100-deserialize-time.png" alt="KLL200 vs TD100 deserialize time" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-td200-deserialize-time.png" alt="KLL200 vs TD200 deserialize time" />

## Source code

The code to reproduce these measurements is available in the <a href="https://github.com/DataSketches/characterization/tree/tdigest">Datasketches/characterization repository</a>
