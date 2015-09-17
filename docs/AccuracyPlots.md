---
layout: doc_page
---

##Accuracy Plots 

When a sketch is constructed with code similar to:  

<div class="highlight"><pre><code class="language-text" data-lang="text">int k = 4096;
UpdateSketch sketch = UpdateSketch.builder().build(k);

// The sketch is fed unique values with a loop similar to
long u; //the x-coordinate value, the number of uniques to be fed to the sketch
for (int i=0; i&lt;u; i++) {
  sketch.update(i);
}

// After all the unique values have been fed to the sketch, the estimate of <i>u</i> is obtained from the sketch
double est = sketch.getEstimate();

// The upper and lower bounds at 1 RSE can be obtained from the sketch:
double ub = sketch.getUpperBound(1);
double lb = sketch.getLowerBound(1);

// The rebuild option can be used prior to obtaining the above values or storing the sketch:
sketch.rebuild();
</code></pre></div> 

The accuracy behavior of this QuickSelect Sketch (the default) will be similar to the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}QS4KError.png" alt="QS4KError" /> 

This type of graph is called a "pitchfork", which is used throughout this documentation to characterize the accuracy of the various sketches.

Pitchfork graphs have these common characteristics:

* The x-axis is Log(base 2) and represents the number of unique values fed into a sketch.
  * Points along the x-axis are evenly spaced along the log axis with the same number of Points Per Octave (PPO) for every multiple of 2 along the x-axis.
This means that the plotted x values form an exponential series of the form <i>2^(gi/PPO)</i>, where <i>gi</i> is the <i>Generating Index</i> for a particular point on the x-axis.
  * A <i>Trial</i> is the estimation result of a single sketch fed <i>x</i> unique values.
  * A <i>Trial Set<i> is the set of results of runing <i>T</i> independent trials at <i>x</i>.

* The y-axis measures the Relative Error (RE) of result estimates returned by the sketches in the trials, where <i>RE = MeasuredValue/TrueValue -1</i> and is plotted as a percent. 
  * An imaginary line drawn vertically from each x-axis point represents the range of error values that result from the Trial Set.  However, not all of the error values from all the trials are plotted. Instead, for each Trial Set, the result error values are sorted and then selected quantiles are chosen and then only those y-axis values are plotted. 
  * Connecting the plotted points with the same quantiles form the lines of the graph


The specifics of the above pitchfork graph:

* The sketch type is the Heap QuickSelect Sketch, which is the default theta UpdateSketch.
* The sketch was configured with <i>k = 4096</i>.
* The x-axis varies from 1024 (2^10) uniques per sketch to 1,048,576 (2^20) uniques per sketch. 
* PPO = 16.
* T = 1000.
* Wavy colored curves:
  * The light blue wavy curve in the center (at Y = 0%) represents the mean of the distribution of error values.  The median is not plotted but sits on top of the mean. The fact that the mean and median are at zero validates the assertion that the estimates from the sketch are <i>unbiased</i>.
  * The red wavy curve are the quantiles at +1 <i>Relative Standard Error</i> (RSE), which means ~15.87% of the results of all the trials are above that curve.
  * The purple wavy curve are the quantiles at -1 RSE, which means ~15.87% of the results of all the trials are below that curve.
* Straight segment curves:
  * The bright red straight segment lines are estimates from the sketch of the upper bound at plus 1 RSE. 
  * The bright purple straight segment lines are estimates from the sketch of the lower bound at minus 1 RSE.
* Reference lines:
  * The long-dash reference lines are at plus and minus 1 RSE computed from the mathematical theory. The fact that 68.3% of all the trial points lie within these two long-dashed lines validates the assertion that the basic RSE of a sketch is less than <i>1/sqrt(k)</i>.
  
This particular graph also illustrates some other subtle points about this particular sketch.

* The saw-toothed variation in the overall shaped is due to the fact that the QuickSelect sketch only updates its internal theta when the hash table fills up, which occurs when the hash table reaches <i>15k/8</i> (note that the estimation starts at almost 8K), at which point the sketch uses the QuickSelect algorithm to find the <i>(k + 1)</i><sup>th</sup> order statistic from the cache, assigns this value to theta, discards all values above theta, and rebuilds the hash table.  
* Because the number of valid points nearly reaches <i>2k</i> values means that the Relative Error of the sketch nearly reaches <i>1/sqrt(2k)</i>. The short-dashed lines are drawn at plus and minus <i>1/sqrt(2k)</i>. 
* This behavior could possibly result in nearly <i>2k</i> values being stored.

If the user does not want the sketch ever to exceed <i>k</i> values, then there is an optional rebuild method that can be used as mentioned in the above code snippet.
This would result in the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}QS4KErrorRebuild.png" alt="QS4KErrorRebuild" />

Note that the plus and minus 1 RSE now smoothly moves up to and follows the theoretically computed RSE reference lines and that estimation starts at 4K.  Because of the extra rebuild at the end the full cycle time of the sketch is a little slower and the average accuracy is a little less than without the rebuild.  This is a tradeoff the user can choose to use or not.

Another major sketch family is the Alpha Sketch.  Its pitchfork graph looks like the following:

<img class="doc-img-half" src="{{site.docs_img_dir}}Alpha4KError.png" alt="Alpha4KError" /> 

This sketch was also configured with a size of 4K entries, otherwise the defaults.
The wavy and smooth curves have the same interpretation as the wavy and straight segment lines for the QuickSelect Sketch above.
However, the short-dashed reference lines are computed at plus and minus <i>1/sqrt(2k)</i>, which means the Alpha Sketch is about 30% more accurate than the QuickSelect Sketch with rebuild for the same value of <i>k</i>.
