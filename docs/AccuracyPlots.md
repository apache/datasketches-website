---
layout: doc_page
---

##Accuracy Plots 

When constructed with the default options (except for <i>k</i> or <i>Nominal Entries</i>), the accuracy behavior should be similar to the following:

<img class="doc-img-half" src="{{site.docs_img_dir}}QS4KError.png" alt="QS4KError" /> 
<img class="doc-img-half" src="{{site.docs_img_dir}}QS4KErrorRebuild.png" alt="QS4KErrorRebuild" />
<img class="doc-img-half" src="{{site.docs_img_dir}}Alpha4KError.png" alt="Alpha4KError" /> 


The above "pitchfork" plot illustrates the error behavior of the Alpha sketch family.  This sketch was configured with a size of 16K entries, otherwise the defaults.
The X-axis is the number of uniques fed to the sketch and the Y-axis is the estimation error as a percent relative to the true answer.  The black line is the mean. The purple and green lines are +/- 1 standard deviation, which correspond to the 68% confidence bounds.  The blue and red lines are +/- 2 standard deviations, which correspond to the 95% confidence bounds.

Note that the sketch returns the exact answer up to 16K uniques and after that the error gradually increases and then becomes asymptotic to 1/sqrt(2k).



When performing union operations with first generation sketches there will not be any degredation of error.  For example, if you build a thousand sketches of size k=4096 from raw data, the union of all of these sketches will have the same RSE as the original sketches, which is about 1.6%.

However, if you perform a union of sketches produced from the intersection or difference operations, or from sketches produced with smaller <i>k</i> configurations, the RSE will be dominated by the RSE of these later sketches.
