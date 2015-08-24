---
layout: doc_page
---

##Basic Sketch Accuracy

Sketch accuracy is usually measured in terms of Relative Error (<i>RE = Measured/Truth -1</i>).  Sketches are stochastic processes and the estimates produced are random variables that have a probability distribution that is close to the familiar Gaussian, which looks like the following.

<img class="doc-img-qtr" src="{{site.docs_img_dir}}Normal2.png" alt="Normal2" />

The Estimate is at the mean and median of the distribution.  +/- one standard deviation (SD) in terms 
of Relative Error is called the <i>Relative Standard Error (RSE)</i>. The area or mass of the distribution betweeen +/- 1 RSE is 68.27% and is referred to as the <i>confidence</i> interval. +/- 2 RSE corresponds to a confidence interval of 95.4%. 

There are three fundamentally different sketch families in this library: the <i>QuickSelect</i>, <i>Alpha</i> and <i>HLL</i> families. The QuickSelect family is the default. 







