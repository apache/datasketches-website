---
layout: doc_page
---

## Unioning Sketches with Different values of <i>k</i>

One of the benefits of the Theta Sketch algorithms is that they support the union of sketches that have been created with different values of <i>k</i> or <i>Nominal Entries</i>. More specifically, it is possible to create a Union operation with a <i>k<sub>U</sub></i> and then update the union with sketches created with different <i>k<sub>i</sub></i> that can be either larger or smaller than <i>k<sub>U</sub></i>. 

<p>The interesting case, of course, is where <i>k<sub>U</sub></i> &gt; <i>k<sub>i</sub></i>, and, it turns out that it is possible that the Relative Standard Error, $\color{black}{RSE = 1/{\sqrt{k}}}$, of the resulting Union, can be improved, i.e., <i>RSE<sub>U</sub></i> &lt; <i>min(RSE<sub>i</sub>)</i>.</p>

This is in contrast to the HLL algorithm, where unioning is only possible with the same <i>k</i> or with values that are powers-of-2 smaller. 
In this case the <i>RSE<sub>U</sub></i> = <i>min(RSE<sub>i</sub>)</i>.


