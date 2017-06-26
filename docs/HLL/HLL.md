---
layout: doc_page
---

## HLL Sketch
The hll package contains a set of very compact implementations of Phillipe Flajolet's
HLL sketch but with significantly improved error behavior and excellent speed performance.

If the use case for sketching is primarily counting uniques and merging, 
the HLL sketch is the highest performing in terms of accuracy for space consumed.  
For large counts, these HLL sketches can be 2 to 16 times smaller for the same 
accuracy than the Theta Sketches mentioned above.

A new HLL sketch is created with a simple constructor:

    int lgK = 12; //This is log-base2 of k, so k = 4096.  lgK can be from 7 to 21
    HllSketch sketch = new HllSketch(lgK); //TgtHllType.HLL_4 is the default
    //OR
    HllSketch sketch = new HllSketch(lgK, TgtHllType.HLL_6);
    //OR
    HllSketch sketch = new HllSketch(lgK, TgtHllType.HLL_8);

All three different sketch types are <i>targets</i> in that the sketches start out in a warm-up mode 
that is small in size and gradually grows as needed until the full HLL array is allocated. 
The HLL\_4, HLL\_6 and HLL\_8 represent different levels of compression of the final HLL array
where the 4, 6 and 8 refer to the number of bits each bucket of the HLL array is compressed down to.
The HLL\_4 is the most compressed but generally slightly slower than the other two, especially during union operations.

All three types share the same API.  Updating the HllSketch is very simple:

    long n = 1000000;
    for (int i = 0; i < n; i++) {
      sketch.update(i);
    }

Each of the presented integers above are first hashed into 128-bit hash values that are used by the 
sketch HLL algorithm, so the above loop is essentially equivalent to using a random number generator 
initialized with a seed so that the sequence is deterministic.

Obtaining the cardinality results from the sketch is also simple:

    double estimate = sketch.getEstimate();
    double estUB = sketch.getUpperBound(1.0); //the upper bound at 1 standard deviation.
    double estLB = sketch.getLowerBound(1.0); //the lower bound at 1 standard deviation.
    //OR
    System.out.println(sketch.toString()); //will output a summary of the sketch.

Which produces a console output something like this:

    ### HLL SKETCH SUMMARY: 
      Log Config K   : 12
      Hll Target     : HLL_4
      Current Mode   : HLL
      LB             : 977348.7024560181
      Estimate       : 990116.6007366662
      UB             : 1003222.5095308956
      OutOfOrder Flag: false
      CurMin         : 5
      NumAtCurMin    : 1
      HipAccum       : 990116.6007366662


### HLL Accuracy Comparisons

The accuracy behavior of any one of the HLL sketches compared to the Theta-Alpha sketch will be similar to the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}/hll/RSEcomparisons.png" alt="RSEcomparisons.png" />

Accuracy is measured in terms of Relative Standard Error (RSE), which is one Standard Deviation of the error variance about the true cardinality.
This graph has 16 plot points per octave on the X-axis, and each plot point is the average of 4096 trials (LgT=12). 
The sketches were configured for K = 4096 (LgK=12). The Theta-Alpha sketch was chosen because its accuracy plot is simpler. 

The very low error of the HLL curve (red) below the transistion point at about 384 is the result of our new Coupon Estimator, 
which as an RSE of about 50 ppm.  The transition point is a function of the LgK parameter of the sketch.
The low range error of the Theta sketches is always zero below their transition point.

### HLL Speed Comparisons

The update speed behavior of the HLL sketches compared to the Theta-Alpha sketch will be similar to the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}/hll/SpeedComparisons.png" alt="SpeedComparisons.png" />

The Theta-Alpha sketch is clearly the fastest achieving about 6.5 nanoseconds per update at the high end and staying below 20 nS for nearly the entire range.  

All of the HLL types share the same growth strategy below the transition point to the HLL array, which on this graph occurs at about 384 uniques. Above the transition point, the HLL\_8 sketch is the fastest followed by the HLL\_6 an HLL\_4, which are fairly close together.


### HLL Serialized Size Comparisons

The serialization sizes of the HLL sketches compared to the Theta-Alpha sketch will be similar to the following graph:

<img class="doc-img-half" src="{{site.docs_img_dir}}/hll/SerCompactSizes.png" alt="SerCompactSizes.png" />

Both the HLL sketches and the Theta sketches have a similar growth strategy below their respective transition points. 
The HLL sketches grow in increments of 4 bytes, and the Theta sketches grow in increments of 8 bytes. 
Thus, below the HLL transistion point the Theta sketch is 2X larger than the HLL sketch.  
Above the transition point the HLL\_4 sketch has a 16X size advantage over the Theta sketch.

### Caveats
Large data with many dimensions and dimension coordinates are often highly skewed 
creating a "long-tailed" or power-law distribution of unique values per sketch.  
This can create millions of sketches where a vast majority of the sketches will have only a few entries.
It is this long tail of the distribution of sketch sizes that can dominate the overall storage cost for all of the sketches. 
In this scenario, the size advantage of the HLL can be significantly reduced down to a factor of two compared to Theta sketches. 
This behavior is strictly a function of the distribution of the input data so it is advisable to understand 
and measure this phenomenon with your own data.

The HLL sketch is not recommended if you anticipate the need of performing set intersection 
or difference operations with reasonable accuracy.

HLL sketches cannot be intermixed or merged in any way with Theta Sketches.

* [1] Edith Cohen, All-Distances Sketches, Revisited: HIP Estimators for Massive Graphs Analysis, PODS 2014.

