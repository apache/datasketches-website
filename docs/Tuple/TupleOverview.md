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
## Tuple Sketch Overview

Tuple Sketches are extensions of the Theta Sketch, which can be represented internally as an array of hash values (of unique identifiers) and a value, theta, which represents the effective sampling rate determined by the sketch.

The analysis capabilities with the core Theta Sketch technology is quite powerful, nevertheless, it is limited to set expressions across multiple Theta Sketches.

<img class="doc-img-full" src="{{site.docs_img_dir}}/tuple/TupleStartsWithTheta.png" alt="TupleStartsWithTheta" />

Tuple Sketches associate a Summary Object with each hash value.  A Summary Object can be anything, but is typically one or more numeric or boolean state variables as members of columns. This is shown in the next figure.

<img class="doc-img-full" src="{{site.docs_img_dir}}/tuple/TupleWithAttributes.png" alt="TupleWithAttributes" />

Tuple Sketches are ideal for summarizing attributes such as impressions or clicks. When the sketch is complete, the sum of all the counts in any particular column divided by theta of the sketch is an unbiased estimate of the sum of that attribute of the population from which the summary rows of sketch were drawn.  

Summary Objects are class extensions of the generic base classes in the library. It is up to the developer of the extension how the summary fields are defined and how they should be combined during updates or during set operations. 

Because the distribution of the attribute values is not known, it is not possible to provide meaningful error bounds on the projections of the attribute mean or variance onto the raw population. 

Keep in mind that all of these operations are stream-based.  The raw data from which these sketches are built only needs to be touched once.

The Tuple Sketches also provide sufficient methods so that user could develop a wrapper class that could facilitate approximate joins or other common database operations.  This concept is illustrated in this next diagram.

<img class="doc-img-half" src="{{site.docs_img_dir}}/tuple/TupleJoins.png" alt="TupleJoins" />
