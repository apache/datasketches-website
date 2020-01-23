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
## Sketching and Order Sensitivity

Sketching by its nature is a stochastic process and in general we cannot guarantee _order insensitivity_.
All of our sketches (frequency, quantiles, Theta and HLL) should be assumed to be _order sensitive_.
The only "guarantee" that we offer is that the "true value" (computed using brute-force techniques) should be within our approximate error bounds with the specified confidence.

Having said that there are a few exceptions to this "assume order sensitivity" guideline.

### Theta Sketches

Only the QuickSelect Sketch (the default) can be order insensitive and ONLY if the final sketch is "trimmed" back to a maximum of _K_ values before an estimate is retrieved. For example:

```java
UpdateSketch sk = Sketches.updateSketchBuilder().build();
for (...) { /* load sketch with > 2K values */ }
double est = sk.getEstimate(); //this may be order sensitive (but not if sketch is in exact mode)
UpdateSketch sk2 = sk.rebuild(); //trims retained entries back to K
double est2 = sk2.getEstimate(); //this will be order insensitive
```

If you want a Compact Sketch to be order insensitive, you must _rebuild()_, first than do _compact()_.

When doing Unions with Theta Sketches, the getResult(...) automatically trims the result back to _K_.

The impact of the rebuild() is that the error will not be as good as the un-trimmed sketch, but you will get your desired order insensitivity. [For example](https://datasketches.apache.org/docs/Theta/ThetaAccuracyPlots.html).

### HLL Sketches

If you use the _getCompositeEstimate()_, the result should be order insensitive, but is less accurate than the _getEstimate()_, which uses the HIP estimator.  Unfortunately, the HIP estimator does not "survive" the union process so the error of the HLL sketches that have gone through a union process generally must fall back on the composite estimator.  (This is tracked internally and is rather complex as there are some special cases where the HIP estimator can still be used.)

### The HIP Estimator
The HIP estimator is inherently order sensitive, but provides significantly improved error properties over the composite approach, so it is too important to ignore.

### System Testing and Sketches

There are two primary ways that a "reference" standard is often obtained to use when system testing with sketches:

* Brute Force computation of the correct result.  The recommended approach.
* Assuming some prior test run produced the correct result.  I do not recommend this, but many system teams do this anyway.  Even if the sketches are working correctly, this can result in double-sided error, so be careful!

Given a Brute Force reference, the proper way to establish correctness of the result of a test is to use the upper and lower bounds (or equivalent, depending on the sketch) provided by the sketch.  Suppose you use 2-sigma confidence bounds.  Then out of 1000 _statistically independent_ trials (runs), ~50 of the results will be outside the 2-sigma bounds. 

This is not happy news for system developers that are determined to have deterministic results.  But, there is no magic sauce to fix what is inherently a probabilistic result.
