---
layout: doc_page
---
[Prev](/docs/KMVsketch2.html)
[Next](/docs/KMVsketch4.html)

#The KMV Sketch, Step 3
Now lets choose <i>k = 3</i> which means that we will keep the 3 smallest hash values that the cache has seen.  We will also alter the definition of <i>d</i> to be the value of the <i>k<sup>th</sup></i> smallest hash the cache has seen or <i>V(k<sup>th</sup>) = 0.195 = d</i>.  This is also known as the <i>k<sup>th</sup> Minimum Value</i> or <i>KMV</i>.  Since these measurements are relative to zero, a sketch constructed like this is also known as a <i>Bottom-k</i> sketch.  (It could well have been a <i>Top-k</i> sketch, but referencing to zero is just simpler.)

Our new estimator becomes <i>(k-1)/V(k<sup>th</sup>)</i> = 2/0.195 = 10.26, which is much closer to 10, but with such a small sample size we were also lucky. The minus one in the numerator falls out of the statistics and is required for the estimate to be unbiased[1]. 

<img class="ds-img" src="/docs/img/KMV3.png" alt="KMV3" />

Note that with our new estimator based on the <i>k</i> minimum values in the cache we don't have to keep any hash values larger than <i>V(k<sup>th</sup>)</i>.  And, since <i>k</i> is a constant our cache will have a fixed upper bound size independent of how many hash values it has seen.

[1] For those interested in the mathematical proofs, 
<a href="http://www-sop.inria.fr/members/Frederic.Giroire/publis/Gi05.pdf">Giroire</a>
has a straightforward and easy-to-follow development.

[Prev](/docs/KMVsketch2.html)
[Next](/docs/KMVsketch4.html)

