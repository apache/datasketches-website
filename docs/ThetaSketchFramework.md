---
layout: doc_page
---
[Prev](KMVupdateVkth.html)<br>
[Next](ThetaSketchSetOps.html)

#Theta Sketch Framework
The <i>Theta Sketch Framework</i> (TSF) is an extension of the KMV sketch that enables multiple sketching algorithms to be implemented in a common theoretical framework and with a common API and greatly simplifies implementation of set expressions.

Note that in the KMV sketch the value <i>k</i> is overloaded with multiple roles:

1. <i>k</i> determines the RSE (accuracy) of the sketch
2. <i>k</i> determines the upper-bound size of the sketch
3. <i>k</i> is used as a constant in the estimator and RSE equations
4. <i>k</i> determines the <i>V(k<sup>th</sup>)</i> threshold, used to reject/accept hash values into the cache.

By unloading some of these roles, we will gain degrees of freedom to do some innovative things. 

Instead of having to track <i>V(k<sup>th</sup>)</i>, which is a member of the list of hash values, we are going to create a separate threshold variable and call it <i>theta</i> (&theta;). This effectively decouples #3 and #4 above from <i>k</i>. When the sketch is empty &theta; = 1.0.  After the sketch has filled with <i>k</i> minimum values &theta; is still 1.0.  When the next incoming unique value must be inserted into the sketch the <i>(k+1)<sup>th</sup></i> minimum value, is assigned to &theta; and removed from the cache.[1]

Mathematically, a <i>Theta Sketch</i> is defined as having two parameters, a set <i>S</i> of hash values, <i>h</i> (also referred to as <i>entries</i>), and a threshold &theta;; where all members, <i>h</i> of <i>S</i> are less than &theta;.  Ultimately, it will be the size of <i>S</i>, <i>|S|</i>, that will determine the stored size of a sketch, which decouples #2 above from the value <i>k</i>.  In the Theta Sketch Framework the value of <i>k</i> is a <i>user specified, configuration parameter</i>, which is used by the software to determine the target accuracy of the sketch and the maximum size of the sketch.

The unbiased estimate simplifies to \|S\|/&theta;, which is just the size of <i>S</i> divided by &theta;. 
We will discuss the RSE in a later section.

<img class="ds-img" src="{{site.docs_img_dir}}ThetaSketch1.png" alt="ThetaSketch1" />


[1] This is a limited "KMV perspective" on how &theta; gets assigned.  If you study the attached paper <b>TODO</b>, you will understand that there are multiple ways that &theta; can be assigned and we call this process the <i>Theta Choosing Function (TCF)</i>.  Different sketch algorithms have different TCFs.  We also take advantage of the ability to preset &theta; for up-front probability sampling, which we will discuss in another section.

[Prev](KMVupdateVkth.html)<br>
[Next](ThetaSketchSetOps.html)
