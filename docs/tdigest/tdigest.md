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
# t-digest overview

This is an algorithm and data structure for estimating ranks and quantiles of distributions of numerical values.

The implementation in this library is based on the MergingDigest in following [reference implementation](https://github.com/tdunning/t-digest).

The implementation in this library has a few differences from the reference implementation:

* merge does not modify the input
* different serialization similar to other sketches in this library, reading the reference implementation format is supported
* C++ template implementation for float and double types

Unlike all other algorithms in the library, t-digest is empirical and has no mathematical basis for estimating its error and its results are dependent on the input data. However, for many common data distributions, it can produce excellent results.

The library contains a few different quantile sketches for estimating distributions (ranks and quantiles). All other quantile skeches can handle arbitrary comparable types and always retain a small set of items from the input stream. All queries that return approximations in the input domain return one of the retained items from the input. t-digest is different: it works on numeric data only (floating point types), retains and returns values not necessarilly seen in the input (interpolated).

The closest alternative to t-digest in this library is [REQ sketch](https://datasketches.apache.org/docs/REQ/ReqSketch.html). It prioritizes one chosen side of the rank domain: either low rank accuracy or high rank accuracy. t-digest (in this implementation) prioritizes both ends of the rank domain and has lower accuracy towards the middle of the rank domain (median).

The more input values t-digest observes the more it tends to be biased (tends to underestimate low ranks and overestimate high ranks):

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_err_vs_rank_k100_n2e10.png" alt="ErrorVsRank2e10" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_err_vs_rank_k100_n2e15.png" alt="ErrorVsRank2e15" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_err_vs_rank_k100_n2e20.png" alt="ErrorVsRank2e20" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_err_vs_rank_k100_n2e25.png" alt="ErrorVsRank2e25" />

Rank error vs stream size:

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_rank_error_k100.png" alt="RankErrorK100" />

Comparisons with REQ sketch:

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_vs_req_memory.png" alt="TDigestVsReqMemory" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tgigest_vs_req_serialized_size.png" alt="TDigestVsReqSeializedSize" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_vs_req_rank_error_0.99.png" alt="TDigestVsReq0.99" />

<img class="doc-img-full" src="{{site.docs_img_dir}}/tdigest/tdigest_vs_req_hra_exp_1.5.png" alt="TDigestVsReqHRAexp1.5" />
