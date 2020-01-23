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
## [Theta Sketches]({{site.docs_dir}}/Theta/ThetaSketchFramework.html): Estimating Stream Expression Cardinalities
Internet content, search and media companies like Yahoo, Google, Facebook, etc., collect many tens of billions of event records from the many millions of users to their web sites each day.  These events can be classified by many different dimensions, such as the page visited and user location and profile information.  Each event also contains some unique identifiers associated with the user, specific device (cell phone, tablet, or computer) and the web browser used.  

<img class="doc-img-full" src="{{site.docs_img_dir}}/PeopleCloud.png" alt="PeopleCloud" />

These same unique identifiers will appear on every page that the user visits.  In order to measure the number of unique identifiers on a page or across a number of different pages, it is necessary to discount the identifier duplicates.  Obtaining an exact answer to a _COUNT DISTINCT_ query with massive data is a difficult computational challenge. It is even more challenging if it is necessary to compute arbitrary expressions across sets of unique identifiers. For example, if set _S_ is the set of user identifiers visiting the Sports page and set _F_ is the set of user identifiers visiting the Finance page, the intersection expression _S &#8745; F_ represents the user identifiers that visited both Sports and Finance.

Computing cardinalities with massive data requires lots of computer resources and time.
However, if an approximate answer to these problems is acceptable, [Theta Sketches]({{site.docs_dir}}/Theta/ThetaSketchFramework.html) can provide reasonable estimates, in a single pass, orders of magnitude faster, even fast enough for analysis in near-real time.

## [HyperLogLog Sketches]({{site.docs_dir}}/HLL/HLL.html): Estimating Stream Cardinalities
The HyperLogLog (HLL) is a cardinality sketch similar to the above Theta sketches except they are anywhere from 2 to 16 times smaller in size.  The HLL sketches can be Unioned, but set intersection and difference operations are not provided intrinsically, because the resulting error would be quite poor.  If your application only requires cardinality estimation and Unioning and space is at a premium, the HLL sketch provided could be your best choice. 

## [HyperLogLog Map Sketch]({{site.docs_dir}}/HLL/HllMap.html): Estimating Stream Cardinalities of Key-Value Pairs
This is a specially designed sketch that addresses the problem of individually tracking value cardinalities of Key-Value (K,V) pairs, where the number of keys can be very large, such as IP addresses, or Geo identifiers, etc. Assigning individual sketches to each key would create unnecessary overhead. This sketch streamlines the process with much better space management.


## [Quantiles Sketches]({{site.docs_dir}}/Quantiles/QuantilesOverview.html): Estimating Distributions from a Stream of Values
There are many situations where is valuable to understand the distribution of values in a stream. For example, from a stream of web-page time-spent values, it would be useful to know arbitrary quantiles of the distribution, such as the 25th percentile value, the median value and the 75th percentile value. The [Quantiles Sketches]({{site.docs_dir}}/Quantiles/QuantilesOverview.html) solve this problem and enable the inverse functions such as the Probability Mass Function (PMF) and the Cumulative Distribution Function (CDF) as well. It is relatively easy to produce frequency histograms such as the following diagram, which was produced from a stream of over 230 million time spent events. The space consumed by the sketch was about 43KB.

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/TimeSpentHistogram.png" alt="TimeSpentHistogram" />

## [Frequent Items Sketches]({{site.docs_dir}}/FrequentItems/FrequentItemsOverview.html): Finding the Heavy Hitter Objects from a Stream
It is very useful to be able to scan a stream of objects, such as song titles, and be able to quickly identify those items that occur most frequently.  The term <i>Heavy Hitter</i> is defined to be an item that occurs more frequently than some fractional share of the overall count of items
in the stream including duplicates.  Suppose you have a stream of 1M song titles, but in that stream there are only 100K song titles that are unique. If any single title consumes more than 10% of the stream elements it is a Heavy Hitter, and the 10% is a threshold parameter we call epsilon.

The accuracy of a Frequent Items Sketch is proportional to the configured size of the sketch, the larger the sketch, the smaller is the epsilon threshold that can detect Heavy Hitters. 

## [Tuple Sketches]({{site.docs_dir}}/Tuple/TupleOverview.html): Extending Theta Sketches to Perform Associative Analysis 
It is often not enough to perform stream expressions on sets of unique identifiers, it is very valuable to be able to associate additive data with these identifiers, such as impression counts or clicks.  Tuple Sketches are a recent addition to the library and can be extended with arbitrary "summary" data.  

## [Sampling Sketches]({{site.docs_dir}}/Sampling/ReservoirSampling.html): Uniform Sampling of a Stream into a fixed size space
This implements the famous Reservoir sampling algorithm and extends it with the capabilities that large-scale distributed systems really need: mergability (even with different sized sketches), uses Java Generics so that the base classes can be trivially extended for any input type (even polymorphic types), and an extensible means of performing serialization and deserialization. VarOpt sampling extends the family to weighted sampling, additionally providing subset sum estimates from the sample with provably optimal variance.

## Frequent Directions: Distributed, mergeable Singular Value Decomposition 
Part of a new separate sketches-vector package, Frequent Directions is in many ways a generalization of the Frequent Items sketch to handle vector data. This sketch computes an approximate singular value decomposition (SVD) of a matrix, providing a projection matrix that can be used for dimensionality reduction. SVD is a key technique in many recommender systems, providing shopping suggestions based on a customer's past purchases compared with other similar customers.
