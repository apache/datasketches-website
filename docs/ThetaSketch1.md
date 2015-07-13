---
layout: doc_page
---
[Prev](/docs/KMVsketch5.html)
[Next](/docs/ThetaSketch2.html)

#The Theta Sketch
The <i>Theta Sketch Framework</i> (TSF) is an extension of the KMV sketch that enables multiple sketching algorithms to be implemented in a common theoretical framework and with a common API and greatly simplifies implementation of set expressions.

Note that in the KMV sketch the value <i>k</i> is overloaded with multiple roles:

1. <i>k</i> determines the RSE (accuracy) of the sketch
2. <i>k</i> determines the upper-bound size of the sketch
3. <i>k</i> is used as a constant in the estimator and RSE equations
4. <i>k</i> determines the <i>V(k<sup>th</sup>)</i> threshold, used to reject/accept hash values into the cache.

By unloading some of these roles, we will gain degrees of freedom to do some innovative things. 

Instead of having to track <i>V(k<sup>th</sup>)</i>, which is a member of the list of hash values, we are going to create a separate threshold variable and call it <i>theta</i> (&theta;). This effectively decouples #3 and #4 above from <i>k</i>. When the sketch is empty &theta; = 1.0.  After the sketch has filled with <i>k</i> minimum values &theta; is still 1.0.  When the next incoming unique value must be inserted into the sketch the <i>(k+1)<sup>th</sup></i> minimum value, (the one being tossed out) is assigned to &theta; and removed from the cache.  

Mathematically, a <i>Theta Sketch</i> is defined as having two parameters, a set <i>S</i> of hash values, <i>h</i> (also referred to as <i>entries</i>), and a threshold &theta;; where all members, <i>h</i> of <i>S</i> are less than &theta;.  Ultimately, it will be the size of <i>S</i> or <i>|S|</i> that will determine the stored size of a sketch, which decouples #2 above from the value <i>k</i>.  In the Theta Sketch the value of <i>k</i> is only used as a <i>user specified, configuration parameter</i>, which is used by the software to determine the minimum (or sometimes average) accuracy of the sketch.

The unbiased estimate simplifies to \|S\|/&theta;, which is just the size of <i>S</i> divided by &theta;.  
Computing the actual RSE is more complex (again, handled for you in the library), but it will always be &le; 1/sqrt(<i>k</i>-1). 

<img class="ds-img" src="/docs/img/ThetaSketch1.png" alt="ThetaSketch1" />

[Prev](/docs/KMVsketch5.html)
[Next](/docs/ThetaSketch2.html)
