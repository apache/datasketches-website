---
layout: doc_page
---
[Prev](/docs/KMVsketch4.html)

#The KMV Sketch, Step 5
Our final rules: 

If the cache is presented with a hash value that is less than <i>V(k<sup>th</sup>)</i> and <i>not</i> a duplicate, we insert the new value in order, remove what was the <i>V(k<sup>th</sup>)</i> value and replace it with whatever was next in order, which becomes the new <i>V(k<sup>th</sup>)</i> value.

If the cache has been presented with less than <i>k</i> values,
the estimate equals the current count of values in the cache.

<img class="ds-img" src="/docs/img/KMV5.png" alt="KMV5" />

We now have a complete <i>KMV</i> sketch, with some amazing properties:

* The sketch has a fixed upper bound size of <i>k</i> values.
* De-duplication is part of the algorithm.
* If the sketch has retained less than <i>k</i> values, the resulting estimate is exact and is just the count of the values in the cache.  Otherwise, the unbiased estimate of the unique values seen by the sketch is <i>(k-1)/V(k<sup>th</sup>)</i>[1]
* The <i>Relative Standard Error</i> or <i>RSE</i> of the estimate is less than <i>1/sqrt(k-2)</i>[1],
which is a constant and independent of <i>n</i>, the number of uniques presented to the sketch.
For large enough values of <i>k</i> the error distribution is roughly Gaussian, which makes preditions of confidence intervals relatively straightforward.

Note that the sketch algorithms implemented in the DataSketches library are more sophisticated than the example KMV sketch outlined here.


[1] For those interested in the mathematical proofs, 
<a href="http://www-sop.inria.fr/members/Frederic.Giroire/publis/Gi05.pdf">Giroire</a>
has a straightforward and easy-to-follow development.


[Prev](/docs/KMVsketch4.html)

