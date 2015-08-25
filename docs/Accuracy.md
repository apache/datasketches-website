---
layout: doc_page
---

##Basic Sketch Accuracy



Sketch accuracy is usually measured in terms of Relative Error (<i>RE = Measured/Truth -1</i>).  Sketches are stochastic processes and the estimates produced are random variables that have a probability distribution that is close to the familiar Gaussian, which looks like the following.

<img class="doc-img-half" src="{{site.docs_img_dir}}Normal2.png" alt="Normal2" />

Here the sketch estimate is at the mean and median of the distribution.  Plus/minus one standard deviation (SD) in terms 
of Relative Error is called the <i>Relative Standard Error (RSE)</i>. 

The area under the curve of the Standard Normal (Gaussian) Distribution is defined to be 1.0.
The fractional area between two points on the X-axis is referred to as the <i>confidence level</i>.  
Thus, the confidence level (the fractional area) between plus 1 RSE and minus 1 RSE is 68.27% and
the confidence level between plus 2 RSE and minus 2 RSE is 95.4%. 

There are three fundamentally different sketch families in this library: the <i>QuickSelect</i>, <i>Alpha</i> and <i>HLL</i> families. The QuickSelect family is the default. 







