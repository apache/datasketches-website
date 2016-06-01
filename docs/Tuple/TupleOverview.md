---
layout: doc_page
---

## Tuple Sketch Overview

Tuple Sketches are extensions of the Theta Sketch, which can be represented internally as an array of hash values (of unique identifiers) and a value, theta, which represents the effective sampling rate determined by the sketch.

The analysis capabilities with the core Theta Sketch technology is quite powerful, nevertheless, it is limited to set expressions across multiple Theta Sketches.

<img class="doc-img-full" src="{{site.docs_img_dir}}/TupleStartsWithTheta.png" alt="TupleStartsWithTheta" />

Tuple Sketches are an extension of Theta Sketches that associate a Summary Object with each hash value.  A Summary Object can be anything, but is typically one or more numeric or boolean state variables as members of columns. This is shown in the next figure.

<img class="doc-img-full" src="{{site.docs_img_dir}}/TupleWithAttributes.png" alt="TupleWithAttributes" />

Tuple Sketches are ideal for sumarizing attributes such as impressions or clicks. When the sketch is complete, the sum of all the counts in any particular column divided by theta of the sketch is an unbiased estimate of that attribute from which the sketch was collected from.

Summary Objects are class extensions of the generic base classes in the library. It is up to the developer of the extension how the summary fields are defined and how they should behave during updates or during set operations.

Keep in mind that all of these operations are stream-based.  The raw data from which these sketches are built only needs to be touched once.

The Tuple Sketches also provide sufficient methods so that user could develop a wrapper class that could faciliate approximate joins or other common database operations.  This concept is illustrated in this next diagram.

<img class="doc-img-half" src="{{site.docs_img_dir}}/TupleJoins.png" alt="TupleJoins" />
