---
layout: doc_page
---
[Prev](KMVfirstEst.html)<br>
[Next](KMVrejection.html)

## The KMV Sketch, Better Estimator, Size = <i>k</i>
Now lets choose <i>k = 3</i>, which means that we will keep the 3 smallest hash values that the cache has seen.  The fractional distance that these <i>k</i> values consume is simply the value of the k<sup>th</sup> hash value, or <i>V(k<sup>th</sup>)</i>, which in this example is 0.195. This is also known as the <i>k<sup>th</sup> Minimum Value</i> or <i>KMV</i>.  Since these measurements are relative to zero, a sketch constructed like this is also known as a <i>Bottom-k</i> sketch.  (It could well have been a <i>Top-k</i> sketch, but referencing to zero is just simpler.)

<img class="doc-img-full" src="{{site.docs_img_dir}}KMV3.png" alt="KMV3" />

We want not only a more accurate estimate, but one that is also <u><i>unbiased</i></u>.  I'm going to skip a few pages of calculus[1] and reveal that we only need to subtract one in the numerator to achieve that.  Our new, unbiased KMV estimator becomes

<img src="{{site.docs_img_dir}}Est2Formula.png" alt="Est2Formula" width="600" />

This is much closer to 10, but with such a small sample size we were also lucky. 

Note that with our new estimator based on the <i>k</i> minimum values in the cache we don't have to keep any hash values larger than <i>V(k<sup>th</sup>)</i>.  And, since <i>k</i> is a constant our cache will have a fixed upper bound size independent of how many hash values it has seen.

[1] For those interested in the mathematical proofs, 
<a href="http://www-sop.inria.fr/members/Frederic.Giroire/publis/Gi05.pdf">Giroire</a>
has a straightforward and easy-to-follow development.

[Prev](KMVfirstEst.html)<br>
[Next](KMVrejection.html)

