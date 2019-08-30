---
layout: doc_page
---

## Compressed Probability Counting (CPC) Sketch<sup>1</sup>
The cpc package contains implementations of Kevin J. Lang's CPC sketch (footnote).
The stored CPC sketch can consume about 40% less space than an HLL sketch of comparable accuracy.
Nonetheless, the HLL and CPC sketches have been intentially designed to offer different tradeoffs so that, in fact, they complement each other in many ways.

Similar to the HLL sketch, the primary use-case for the CPC sketch is for counting distinct values as a stream, and then merging multiple sketches together for a total distinct count. 

Neither HLL nor CPC sketches provide means for set intersecctions or set differences.  If you anticipate your application might require this capability you are better off using the Theta family of sketches.







________________________


<sup>1</sup><small>
Kevin J Lang. Back to the future: an even more nearly optimal cardinality estimation algorithm. arXiv preprint https://arxiv.org/abs/1708.06839, 2017.
</small>
