---
layout: doc_page
---

## HLL Sketch
The hll package contains a very compact implementation of Phillipe Flajolet's
HLL sketch but with significantly improved error behavior.  If the ONLY use case for sketching is
counting uniques and merging, the HLL sketch is the highest performing in terms of accuracy for 
space consumed.  For large counts, this HLL version will be 16 to 32 times smaller for the same 
accuracy than the Theta Sketches mentioned above.

### Caveats
Large data with many dimensions and dimension coordinates are often highly skewed 
creating a "long-tailed" or power-law distribution of unique values per sketch. 
In this case a majority of sketches tend to have only a few entries and it is this long tail of
the distribution of sketch sizes that will dominate the overall storage cost for all of the 
sketches. The size advantage of the HLL will be significantly reduced down to a factor of 
two to four compared to theta sketches. This behavior is strictly a function of the 
distribution of the input data so it is advisable to understand and measure this phenomenon with
your own data.

The HLL sketch algorithm is more complex than the theta sketch algorithms and, as a result,
is about 50% slower in update times and can be a factor of 2 to 4 slower in average merge times 
especially when merging millions of sketches.

The HLL sketch is not recommended if you anticipate the need of performing set intersection 
or difference operations with reasonable accuracy, 
or if you anticipate the need to merge sketches that were constructed with different 
values of <i>k</i> or <i>Nominal Entries</i>.

HLL sketches do not retain any of the hash values of the associated unique identifiers, 
so if there is any anticipation of a future need to leverage associations with these 
retained hash values, Theta Sketches would be a better choice.

HLL sketches cannot be intermixed or merged in any way with Theta Sketches.

### HLL Accuracy

The accuracy behavior of the HLL sketch with the HIP estimator enabled will be similar to the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}Hll4KwHipError.png" alt="Hll4KwHipError" />

This HLL sketch was configured with <i>k</i> = 4096, for direct comparison with the accuracy graphs for the
theta sketches in [AccuracyPlots](AccuracyPlots.html).  For this graph the error behavior is shown for very 
small numbers of uniques to illustrate that HLL estimates <i>&le; k</i> are not zero as they are with the theta
sketches.  The wild swings in the very low range are quantization errors as a function of the test software
attempting to find, for example, the 2.5 percentile point when the number of actual unique values is very small.

Using the Historic Inverse Probability (HIP) estimator[1], the RSE of this sketch approaches <i>~ 0.836 / sqrt(k)</i>.
Keep in mind, however that the HLL sketch can be 16 to 32 times smaller in space utilization in a one-off comparison.

### HLL Speed

The update speed behavior of the HLL sketch will be similar to the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}Hll4KwHipSpeed.png" alt="Hll4KwHipSpeed" />

Because the HLL sketch algorithm is more complex its update speed is about 2X slower than the theta sketches. 
Its merge speed is about 2 to 4 times slower than the theta sketches.


* [1] Edith Cohen, All-Distances Sketches, Revisited: HIP Estimators for Massive Graphs Analysis, PODS 2014.

