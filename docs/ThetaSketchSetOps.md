---
layout: doc_page
---

#Theta Sketch Set Operations
The Theta Sketch definition enables a uniform and simplified approach to performing the three standard set operations, <i>Union</i> (&#8746;), <i>Intersection</i> (&#8745;) and <i>Difference</i> (\\).

The diagram below illustrates how the Intersection operation can be performed by examining the internal hash values (entries) of both sketches, A and B.  Note that the <i>result</i> of a simple 2-way set operation is <i>another sketch</i>!  Performing Union and Difference operations are similar.  The equations for all three set operations are generalized below where the delta symbol, &Delta;, represents one of the three set operations.

<img class="doc-img-full" src="{{site.docs_img_dir}}ThetaSetOps.png" alt="ThetaSetOps" />

The fact that set operations produce sketches as results enables full set expressions, such as<br>
 ((A &#8746; B) &#8745; (C &#8746; D))\\(E &#8746; F).

