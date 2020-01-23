---
layout: doc_page
---
<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->
## KLL sketch

Implementation of a very compact quantiles sketch with lazy compaction scheme and nearly optimal accuracy per bit.
See <a href="https://arxiv.org/abs/1603.05346v2">this paper</a>.
The name KLL is composed of the initial letters of the last names of the authors.

The usage of KllFloatsSketch is very similar to DoublesSketch. Because the key feature of this sketch is compactness, it was implemented with float values instead of double values.
The parameter K that affects the accuracy and the size of the sketch is not restricted to powers of 2.
The default of 200 was chosen to yield approximately the same normalized rank error (1.65%) as the default DoublesSketch (K=128, error 1.73%). 

### Java example

```
import org.apache.datasketches.kll.KllFloatsSketch;

KllFloatsSketch sketch = new KllFloatsSketch();
int n = 1000000;
for (int i = 0; i < n; i++) {
  sketch.update(i);
}
float median = sketch.getQuantile(0.5);
double rankOf1000 = sketch.getRank(1000);
```

### Differences of KllFloatsSketch from DoublesSketch

* Smaller size for the same accuracy
* Slightly faster to update
* Parameter K doesn't have to be power of 2
* Operates with float values instead of double values
* No union object - one sketch is merged into another
* No off-heap implementation
* No separate updatable and compact forms

The starting point for the comparison is setting K in such a way that rank error would be approximately the same. As pointed out above, the default K for both sketches should achieve just that. Here is the comparison of the single-sided normalized rank error (getRank() method) for the default K:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-ds128-rank-error.png" alt="RankError" />

DoublesSketch has two forms with different serialized sizes: UpdateDoublesSketch and CompactDoublesSketch. KllFloatsSketch has no such distinction. It is always serialized in a compact form, and it is not much bigger than that in memory. Here is the comparison of serialized sizes:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-ds128-size.png" alt="SerializedSize" />

Some part of the size difference above is due to using items of float type as opposed to double type. Here is the comparison of the number of retained items to see the difference with no influence of the size of the item type:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-ds128-items.png" alt="NumberOfRetainedItems" />

Below is the accuracy per byte measure (the higher the better). Suppose rank error is 1%, so the corresponding accuracy would be 99%. Divide that by size in bytes to arrive at this measure of how expensive each percentage point is in terms of space needed:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-ds128-accuracy-per-byte-log.png" alt="AccuracyPerByte" />

Below is the update() method speed:

<img class="doc-img-full" src="{{site.docs_img_dir}}/quantiles/kll200-vs-ds128-update.png" alt="UpdateTime" />
