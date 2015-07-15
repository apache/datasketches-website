---
layout: doc_page
---

#Set Operations and Accuracy

When performing union operations with first generation sketches there will not be any degredation of error.  For example, if you build a thousand sketches of size k=4096 from raw data, the union of all of these sketches will have the same relative distribution of error as the original sketches, which is about 1.6%.  

However, if you perform a union of sketches produced from the intersection or difference operations, the resulting error will be dominated by the relative error of these intersection or difference operations.




