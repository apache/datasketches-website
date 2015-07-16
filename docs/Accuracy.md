---
layout: doc_page
---

#Set Operations and Accuracy

When performing union operations with first generation sketches there will not be any degredation of error.  For example, if you build a thousand sketches of size k=4096 from raw data, the union of all of these sketches will have the same RSE as the original sketches, which is about 1.6%.

However, if you perform a union of sketches produced from the intersection or difference operations, or from sketches produced with smaller <i>k</i> configurations, the RSE will be dominated by the RSE of these later sketches.

<img class="ds-img" src="{{site.docs_img_dir}}AlphaError.png" alt="AlphaError" />


