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
Definitions:

* **Absolute Order Insensitivity** Any permutation of the order of a given input stream produces the exact same result.
* **Bounded Order Insensitivity** Any permutation of the order of a given input stream produces a result that is still within the defined error bounds of the sketch and confidence.

Sketching by its nature is a stochastic process and in general we cannot guarantee _absolute order insensitivity_. However, some of our sketches, with the correct configuration, can meet this definition, but in general, we do not recommend users depending on this strict definition of order insensitivity.

Nonetheless, all of our sketches do qualify as being _bounded order sensitive_.
In other words, the "true value" (computed using brute-force techniques) should be within our approximate error bounds with the specified confidence.


### Example: Theta Sketches

Only the internal QuickSelect Sketch (the default) can be order insensitive and _iff_ the final sketch is "trimmed" back to a maximum of _K_ values before an estimate is retrieved. For example:

```java
UpdateSketch sk = Sketches.updateSketchBuilder().build();
for (...) { /* load sketch with > 2 * K values */ }
double est = sk.getEstimate();   //this may be order sensitive
UpdateSketch sk2 = sk.rebuild(); //trims retained entries back to K
double est2 = sk2.getEstimate(); //this will be order insensitive
```

If you want a Compact Sketch to be order insensitive, you must first _rebuild()_, than do _compact()_.

When doing Unions with Theta Sketches, the getResult(...) automatically trims the result back to _K_.

The impact of the rebuild() is that the error will not be as good as the un-trimmed sketch, but you will get your desired order insensitivity. [For example](/docs/Theta/ThetaAccuracyPlots.html).

### HLL Sketches

HLL sketches used stand-alone, are _bounded order insensitive_.  After any merge / union operation the sketch qualifies as 
_absolute order insensitive_, but is less accurate.


### System Testing and Sketches

There are two primary ways that a "reference" standard is often obtained to use when system testing with sketches:

* Brute Force computation of the correct result.  The recommended approach.
* Assuming some prior test run produced the correct result.  This is not recommended, but many system teams do this anyway.  Even if the sketches are working correctly, this can result in double-sided error, so be careful!

Given a Brute Force reference, the proper way to establish correctness of the result of a test is to use the upper and lower bounds (or equivalent, depending on the sketch) provided by the sketch.  Suppose you use 2-sigma confidence bounds.  Then out of 1000 _statistically independent_ trials (runs), ~50 of the results will be outside the 2-sigma bounds. 

This is not happy news for system developers that are determined to have deterministic results.  But, there is no magic sauce to fix what is inherently a probabilistic result.
