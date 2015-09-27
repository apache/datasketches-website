---
layout: doc_page
---

#Theta Sketch Set Operations Accuracy

##Union Operations

###Source sketches and target with the same <i>Nominal Entries</i> or <i>k</i>

As long as all source sketches and target have been configured with the same <i>Nominial Entries</i> or <i>k</i>, 
and there has been no other intervening <i>Intersection</i> or <i>AnotB</i> operations, 
the accuracy of the resulting <i>Union</i> sketch will have the same Relative Standard Error (RSE) as 
determined by the <i>Nominal Entries</i> or <i>k</i> value.

###Source sketches and target with different <i>Nominal Entries</i> or <i>k</i>

The Relative Standard Error (RSE) of the resulting <i>Union</i> sketch will be that determined by the smallest 
<i>Nominal Entries</i> or <i>k</i> value.

##Intersection or AnotB Operations

Predicting accuracy with intersections and difference operations is more complex.  

###Source sketches and target with the same <i>Nominal Entries</i> or <i>k</i>

If the source sketches and target are configured with the same <i>k</i>, the accuracy of the result sketch does 
have a relatively straightforward mathematical solution and intuition. 
The resulting accuracy is 

<center>RSE = <i>sqrt( est(Union(A,B)) / est(Intersection(A,B)) ) * sqrt( 1/ (k-1) )</i></center>

####Example

The intuition for this can be explained using this simple example of intersection 
(set difference would behave similarly):

Suppose we have as inputs two segments of unique identifiers, <i>A</i> and <i>B</i>.

Assumptions:

* We will use a sketch size of <i>k = 4096</i> entries with an RSE ~ 1/sqrt(k) = 1.6%.
* Segment <i>A</i> has 4 million (4M) unique identifiers.
* Segment <i>B</i> has 4 thousand (4K) unique identifiers and is a proper subset of <i>A</i>.

Steps:

* Build the Sketches.  Sketch <i>S<sub>A</sub></i> will be fed segment <i>A</i> and Sketch <i>S<sub>B</sub></i> 
will be fed segment <i>B</i>.
    * Segment <i>B</i> fits exactly in <i>S<sub>B</sub></i>, so <i>&theta;<sub>B</sub></i> = 4K/4K = 1.0.
    * Sketch <i>S<sub>A</sub></i> will end up with <i>&theta;<sub>A</sub></i> = 4K/4M = .001.

The hash values retained in <i>S<sub>A</sub></i> represent a uniform sampling of all of the unique identifiers 
in segment <i>A</i> and the resulting value of i>&theta;<sub>A</sub></i> represents the effective sampling rate
required to end up with <i>k</i> samples, which is ~ 4K/4M = .001.

Even though in the raw data all the values of segment <i>B</i> are in segment <i>A</i>, the probability 
that all the 4K samples of <i>S<sub>B</sub></i> appear <i>S<sub>A</sub></i> is extremely unlikely since 
only one in one-thousand can be randomly chosen.

#####<b>The Theta Rule for Set Operations</b>
* <i>&theta;<sub>result</sub> = min(&theta;<sub>A</sub> , &theta;<sub>B</sub>)</i>.
* All entries retained in the result sketch must be less than <i>&theta;<sub>result</sub></i>.

For our example:

* <i>&theta;<sub>result</sub> = min(0.001, 1.0) = .001
* All the entries from <i>S<sub>A</sub></i> already qualify. 
* Since all 4K values in <i>S<sub>B</sub></i> are uniformly distributed between 0 and 1.0, only .001 of them 
or approximately 4 of the bottom values will remain.
* The resulting intersection sketch will have, on average, only 4 values and a theta of .001.

The mean estimate from the intersection sketch will be 4/.001 = 4K. 
This happens to be correct using this hand-wavy analysis but in general is a random result with a variance. 
The proof that the estimate will be unbiased is in the attached <a href="SketchEquations.pdf">Sketch Equations</a>.

The RSE of a sketch with only 4 values is ~ 1/sqrt(4) = .5 or 50% error. 
This is considerably larger than the RSE of either <i>S<sub>A</sub></i> or <i>S<sub>B</sub></i>, 
which is ~ 1.6% as stated above.

The insight to be gained from this example is that it was the theta (sampling rate) of the sketch of the 
larger population that caused the increase in error. 
And, for this example, increasing the sketch size of <i>S<sub>A</sub></i> would improve the resulting error. 
The general case may be more complex.

More formally, if we define a factor <i>F</i> to be the ratio 
(see <a href="SketchEquations.pdf">Sketch Equations</a>):

<center><i>F</i> = (size of Union(A,B) ) / (size of Intersection(A,B).</center>

Then a simple way to compute the resulting RSE of an intersection (or difference) is

<center>RSE<sub><i>intersection</i></sub> = <i>sqrt(F) * (RSE of input Sketches)</i></center>

And in our example:

<center>RSE<sub><i>intersection</i></sub> = sqrt(4M/4K) * 1/sqrt(4K) = 31.63 * 0.016 =  0.5 = 50%.</center>


###Source sketches and target with different <i>Nominal Entries</i> or <i>k</i>

In the general case this scenario is even more complex and difficult to predict mathematically, but a conservative
estimate would be to use the above equations substituting <i>k</i> with the smallest of the participating 
<i>k</i> values.
