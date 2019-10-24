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
[Prev]({{site.docs_dir}}/Theta/KMVbetterEst.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVupdateVkth.html)

## The KMV Sketch, Rejection Rules
Not needing to store hash values greater than <i>V(k<sup>th</sup>)</i> means we can automatically reject any incoming hash values greater than or equal to <i>V(k<sup>th</sup>)</i> up front.

Additionally, we can reject any incoming hash values that are duplicates of any of the hash values we already have in our <i>KMV</i> list.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/KMV4.png" alt="KMV4" />

[Prev]({{site.docs_dir}}/Theta/KMVbetterEst.html)<br>
[Next]({{site.docs_dir}}/Theta/KMVupdateVkth.html)

