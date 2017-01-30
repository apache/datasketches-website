---
layout: doc_page
---

## Unioning Sketches with Different values of <i>k</i>

<p>One of the benefits of the Theta Sketch algorithms is that they support the union of sketches that have been created with different values of \(k\) or <i>Nominal Entries</i>. More specifically, it is possible to create a Union operation with a \(k_U\) and then update the union with sketches created with different \(k_i\) that can be either larger or smaller than \(k_U\).</p>

<p>The interesting case, of course, is where \(k_U &gt; k_i\), and, it turns out that it is possible that the Relative Standard Error, \(\color{black}{RSE = 1/{\sqrt{k}}}\), of the resulting Union, can be improved, 
i.e., \(RSE_U &lt; min(RSE_i)\).</p>

This is in contrast to the HLL algorithm, where unioning is only possible with the same \(k\) or with values that are powers-of-2 smaller. 
In this case the \(RSE_U = min(RSE_i)\).

