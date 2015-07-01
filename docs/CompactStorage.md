---
layout: doc_page
---

#Compact Binary Storage
Sketches can be instantiated in two primary forms.  Both of these forms can be instantiated either in the Java heap or in direct, off-heap memory using the Memory package.

* <b>Hash-Table (HT) Form</b>
The HT form is similar to how the sketch is instantiated by Java in the Java heap.  Typical of HT it consumes more space depending on how full the HT is.  However updating the sketch is much faster in this form and is the default for all the Update Sketches.

* <b>Compact Form</b>
Once updating a sketch is completed the HT is no longer needed, so the sketch can be stored in a compact form.  The size of this compact form is a simple function of the number of retained hash values (64-bits each) and a small preamble that varies from 8 to 24 bytes depending on the internal state of the sketch.  An empty sketch is represented by only 8 bytes.  The upper limit of the size of the sketch varies by the type of sketch but is in the range of <i>8*k to 16*k</i>, where <i>k</i> is the accuracy-size bound of the sketch, which is discussed in the tutorials.  

