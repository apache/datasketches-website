---
layout: doc_page
---
#Features

* Multiple sketch algorithms for different speed, size and accuracy trade-offs.
* Small and [compact](/docs/CompactStorage.html). Typically much smaller than the raw input stream and with a well defined upper bound of size that is independent of the size of the input stream.
* Fast.  These sketches have been tuned for speed and are inherently "Single Pass" or "One Touch" and thus suitable for both real-time and batch environments.
  * [Fast Update Speed](/docs/UpdateSpeed.html).
  * [Fast Merge Speed](/docs/MergeSpeed.html).
* Highly Parallelizable.  The sketch data structures are "additive" in that they can be merged without losing relative accuracy. 
* Adapted for [grid computing](/docs/Adaptors.html).
* Designed for use with [Off-Heap memory](/docs/MemoryPackage.html).
* Special handling of [hash seeds](/docs/SensitiveDataProtection.html) for handling sensitive data
* [Up-front sampling](/docs/Sampling.html) capability for managing total memory usage for Sketch Marts with millions of sketches.
* Robust.  These sketches are throughly tested and suitable for production deployments.
* User configurable trade-off of error and sketch size. The error bounds are a function of the configured size of the sketch 




