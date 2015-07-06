---
layout: doc_page
---
[Prev](/docs/KMVsketch1.html)
[Next](/docs/KMVsketch3.html)

#The KMV Sketch, Step 2
For this step we are going to cheat a little so that we can learn about estimation. Imagine that our "Big Data" only had 10 values and we have loaded all 10 value into our little cache.  As one can see, the values are roughly evenly distributed between zero and one so our hash transform is doing its job.

So now let's consider the problem: How few hash values would we have to retain of this set of 10 to estimate the value 10? 

##The First Estimator
Suppose we kept only one value, so <i>k = 1</i>, and we chose the smallest hash value out of all the hash values in the set, which, in this case, is 0.008.  We could assume that since the hash values are random-uniformly distributed that the separation between the hash values are roughly the same.  Let's label that separation between values as <i>d</i>.  Our first estimator could be just dividing 1.0 by <i>d</i>. Unfortunately, 1/0.008 is about 125, which is way larger than 10.  And as one can see, there is a lot of variation in the separation of each of the hash values.  If the hash values had come out differently, the smallest hash value could have been almost 0.2 (as illustrated by the separation between the 3rd and 4th values) and our first estimator, 1/0.191 would be about 5, which is too small.  

Our first estimator, <i>1/d</i> is too noisy to be useful.

<img class="ds-img" src="/docs/img/KMV2.png" alt="KMV2" />

[Prev](/docs/KMVsketch1.html)
[Next](/docs/KMVsketch3.html)

