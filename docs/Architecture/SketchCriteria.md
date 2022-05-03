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

# Sketch Criteria for DataSketches Library

There are lots of clever and useful algorithms that are sometimes called "sketches".  However, due to limited resources, in order to be included in the DataSketches library, we had to clearly define what we meant by the term "sketch".  Otherwise, we would end up with a hodge podge of algorithms and have to answer: Why don't we include algorithm X?.

In order to be in our library, a *Sketch* should exhibit these properties:

## Streaming / One-Touch 
Sketches are a class of streaming algorithms by definition, which means they only touch or process each item in a stream once.  This is absolutely essential for real-time applications.

## Small in Size
One of the key properties of any sketch is that it is a synopsis or summary of a much larger data set.  The whole point of a small summary is that it is faster to read and merge.  In this context, *small* means small with respect to the original data.  If the original data is terabytes in size, a single sketch of 100KB may not seem very different from a sketch of 50KB as both are very small compared to the original data.  

But *small* can also be important in an systems context. If that original terabyte of data generates 10,000 sketches, each sketch consuming 100KB, that amounts to a GB of storage.  Now the total memory use starts to be a concern.  Being able to reduce that by 50% by using a smaller (and otherwise equivalent) sketch can be a big deal.

Nonetheless, *small* is relevant to the specific application. Sketches can very from a few bytes to many megabytes depending on the specific sketch and how it has been configured. Whether it is small enough depends on the use-case and the specific environment. 

## Sublinear in Size Growth
Not only should a sketch start small, it needs to stay small as the size of the input stream grows.  Some sketches have an upper bound of size independent of the size of the input stream, which clearly makes them sublinear.  Other sketches may need to continue to increase their size as the stream grows.  For these sketches it is important that they do so very very slowly. They should grow sublinearly by no more then *O(log(n))* or preferrably by *O(k log(n/k))* or less.

## Mergeable
In order to be useful in large distributed computing environments, the sketches must be mergeable without additional loss of accuracy.  This is defined as

<p style="text-align: center;"><i>sketch(A + B) &asymp; sketch(A) U sketch(B),</i></p>

where<br>
&nbsp;&nbsp; "+" = concatination of streams A and B,<br>
&nbsp;&nbsp; "U" = merge or union,<br>
&nbsp;&nbsp; "&asymp;" = approximately equal within the error bounds of the sketch.

### Mergeable With Different Size Parameters
In addition to just being mergeable, sketches used in production environments must be mergeable with different sizing parameters.
 
In many production applications sketches might be stored for years because they are so much smaller than keeping the original data around, and orders-of-magnitude faster to merge.

Imagine an organization that has saved its sketches for several years with one size/accuracy parameter, then changes its policy about the size/accuracy needed going forward.  Unless the two differently configured sketches can be merged successfully (even if the accuracy degrades to the lower of the two configurations), the data from the older sketch data would be essentially lost. The only other alternative would require reprocessing all the old original data -- if it even exists!

## Data Insensitive
In many real production environments the data that needs to be processed is ugly. There are often missing or troublesome values in the stream.  It is naive to expect, for example, that a stream of integer time-spent values does not contain zeros or negative values.  In this case, an algorithm that makes the assumption that all the input values are always positive is a very fragile algorithm.  In a real-time streaming application, if the algorithm returns horrible answers because there happend to be zeros in the stream, there may be no opportunity to go back and reprocess the stream after tweeking the algorithm to now ignore zeros! This violates the *One-Touch* property and places a major burden on the system users.

There are many types of Data Insensitivity where the sketch should return meaningful results, within the specified error bounds:

* **Order Insensitive:** The sketch results should be independent of the order that the items are presented to the sketch. See [Sketching and Order Sensitivity](https://datasketches.apache.org/docs/Architecture/OrderSensitivity.html).
* **Distribution Insensitive:** The sketch results should be independent of how the data within the stream is distributed.  For example, the data might be distrubuted as Gaussian, Zipf, lognormal, power-law, or whatever.
* **Value Insensitive:** The sketch results should be independent of exceptional values in the stream.  For example, if the stream consists of double values, the sketch should be able to handle NaNs and Infinities in a sensible way.

There are practical limits to data insensitivity, for example, A sketch designed for handling *double* values should not be expected to handle *strings* or arbitrary objects in the same stream.

It is important that the sketch developers clearly document the data insensitivities that the sketch is designed to handle.

## Mathematically Proven Error and Size Properties
Sketch algorithms must have an openly published and reviewed theoretical basis for their operation including their error and merging properties.  Empirical algorithms do not qualify.

### Meaningful and Usable Error Bounds.
It is not sufficient that some algorithm has been published in a scientific paper. There are published and reviewed sketch algorithms that use error definitions that may be interesting theoretically, but very misleading in practice.  For example, if the authors of a paper define their measure of error to be the average error over a distribution of values (<i>L<sub>1</sub> error</i>), this it not what most users would expect.  Given a query, the user has no idea what part of the distribution his query is hitting, thus, the error result could be great or horrible and the user has no way to know. 

A much more meaningful and usable definition of error is, for example, one that holds for all possible queries simultaneously (<i>L<sub>&infin;</sub> error</i>).

