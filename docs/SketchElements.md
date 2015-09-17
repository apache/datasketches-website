---
layout: doc_page
---

#Sketch Elements

Sketches are different from sampling in that they process all the elements of a stream, but only once. 
The first stage of the sketching process is a transformation that gives the input data stream the property of "white noise". 
This is normally achieved by coordinated hashing of the input unique keys and then normalizing the result to be a uniform random number between zero and one.

The second stage of the sketch is a data structure that follows a set of rules for retaining a small 
set of the hash values it receives from the transform stage. 
Sketches also differ from simple sampling schemes in that the size of the sketch has a configurable, 
fixed upper bound, which enables straightforward memory allocation.  

The final element of the sketch process is a set of estimator algorithms that upon a request 
examine the sketch data structure and return a result value. 
This result value will be approximate but will have well established and mathematically 
proven relative error distribution bounds that remain constant independent of the size of the input data stream.

<img class="doc-img-full" src="{{site.docs_img_dir}}SketchElements.png" alt="SketchElements" />

Sketches are typically

* Small. They can be orders of magnitude smaller than the raw input data stream and with a well defined upper bound of size that is independent of the size of the input stream.
* Fast.  The update times are independent of the size or order of the input stream. These sketches are inherently "Single Pass" or "One Touch".  The sketch only needs to see each item in the stream once.
* Highly Parallelizable.  The sketch data structures are "additive" in that they can be merged without losing relative accuracy.
* Approximate.  The relative error bounds are a function of the configured size of the sketch.

