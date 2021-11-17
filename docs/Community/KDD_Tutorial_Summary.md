---
layout: blank_page
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

# Data Sketching for Real Time Analytics: Theory and Practice

## Abstract

Speed, cost, and scale. These are 3 of the biggest challenges in analyzing big data. While modern data systems continue to push the boundaries of scale, the problems of speed and cost are fundamentally tied to the size of data being scanned or processed. Processing thousands of queries that each access terabytes of data with sub-second latency remains infeasible. Data sketching techniques provide means to drastically reduce this size, allowing for real-time or interactive data analysis with reduced costs but with approximate answers.

This tutorial covers a number of useful data sketching and sampling methods and demonstrate their use using the Apache DataSketches project[3]. We focus particularly on common analytic problems such as counting distinct items, quantiles, histograms, heavy hitters, and aggregations with large group-bys. For these, we covers algorithms, techniques, and theory that can aid both practitioners and theorists in constructing sketches and designing systems that achieve desired error guarantees. For practitioners and implementers, we show how some of these sketches can be easily instantiated using the Apache Datasketches project.

## Audience

This tutorial targets researchers, data systems and infrastructure engineers, and data scientists interested in greatly speeding up or reducing the cost of processing big data sets in practice. It is also useful to theory oriented CS researchers who are interested in statistical techniques that are typically outside CS curricula.

The audience is expected to have a familiarity of probability and statistics that is typical for an undergraduate mathematical statistics or introductory graduate machine learning course.

## Materials

In addition to the prerecorded presentations, the slides and Jupyter notebooks are available. Note that the KLL notebook uses an update method that is only available in release candidate v2.1.0 but as of the tutorial date is not quite available in an official release (the latest is 2.0.0).

* Slides: [Theory (part 1)]({{site.docs_img_dir}}/Community/KDD_sketching_tutorial_pt1.pdf)
* Slides: [Practice (part 2)]({{site.docs_img_dir}}/Community/KDD_sketching_tutorial_pt2.pdf)
* Notebook: [KLL Sketch]({{site.docs_img_dir}}/Community/KLL_Sketch_Tutorial.ipynb)
* Theta SketchL: [Theta Sketch]({{site.docs_img_dir}}/Community/Theta_Sketch_Tutorial.ipynb)

## Outline

The tutorial will consist of two parts. The first focuses on methods and theory for data sketching and sampling. The second focuses on application and includes code examples using the Apache DataSketches project.

The audience should learn about

* techniques used to construct sketches such as sampling, quantization, and random projections
* statistical techniques for extracting information from and theoretically analyzing sketches
* problems that sketches are useful for
* the current state-of-the-art sketch for the problem
* inherent trade-offs when using sketches
* example applications of data sketches
* how to design systems to use sketches
* how to easily incorporate sketches using Apache DataSketches
* how to deal with error in practice

### Details

1. Introduction to data sketches (20 minutes)
    1. Definition
    2. Applications and motivation
    3. Examples
    4. Sketch components
        1. Construction
        2. Representation
        3. Estimation
    5. Optimality
    6. Mergeability and distributed processing
    7. Space vs accuracy measures
    8. Flexibility versus space
2. Data sketches (90 minutes)
    1. Sums + group by
        1. Count-Min and AGMS
        2. More accurate estimates using background distributions
        3. Linear sketches and inner products
    2. Frequent item
        1. Misra-Gries
        2. Extensions
    3. Subset sums
        1. Priority sampling and VarOpt
        2. Unbiased space saving
    4. Distinct Counting
        1. HyperLogLog and Theta sketch
        2. Streaming vs distributed
        3. Intersections and Unions
        4. Many counters
    5. Quantiles
        1. Manku-Rajagopalan-Lindsay and Karnin-Liberty-Lang
        2. IID streams
    6. Approximate set membership
        1. Bloom filters
        2. Cuckoo filters
        3. XOR filters
    7. Matrix sketches
3. Applications (60 minutes)
    1. Sketch-based architectures
    2. Examples
        1. Case Studies
        2. Sketch-enabled Packages
    3. Practical Usage
        1. Implementation subtlety and challenges
        2. Accepting approximation
        3. Understanding error
        4. System planning
    4. Demonstration in [Python](https://github.com/apache/datasketches-cpp/tree/master/python)
        1. Examples of several sketches
        2. Deeper dive with sampling
4. Extra topics (Time permitting) (10 minutes)
    1. Privacy using sketches % worth making time w/ GDPR/CCPA/etc
    2. Adversarial attacks
    3. Active research areas % not sure if  this is interesting?

## Presentor Bios

Daniel Ting is a researcher in Tableau working primarily on data sketching with sketching work published in KDD, SIGMOD, and NeurIPS. His work in the area was initially inspired by problems he encountered while on Facebook's core data science team where he built systems for large scale online experimentation. He obtained his PhD in Statistics from U.C. Berkeley.

Jon Malkin is a senior principal research engineer at Yahoo and a contributor to the Apache DataSketches project. He has experience with large-scale data processing, both brute-force and with sketches, from roles in computational advertising and website traffic analytics. He obtained his PhD in Electrical Engineering from the University of Washington.

## Contributor Bio

Lee Rhodes is a Distinguished Architect at Yahoo. He created the DataSketches project in 2012 to address analysis problems in Yahoo's large data processing pipelines. DataSketches was Open Sourced in 2015 and is now a top level project in the Apache Software Foundation. He was an author or coauthor on sketching work published in ICDT, IMC, and JCGS. He obtained his Master's Degree in Electrical Engineering from Stanford University.

## Societal Impact

Our society is impacted by the ability to do fast data analysis at scale. This tutorial aims to advance this ability by demonstrating how sketching can help and by making sketching more accessible to practitioners. It is also intended to influence future research in data sketches by putting more emphasis on practical algorithms and the importance of constant factors for sketching and by introducing statistical techniques that help solve these problems.

## References

1. P. K. Agarwal, G. Cormode, Z. Huang, J. M. Phillips, Z. Wei, and K. Yi. Mergeable Summaries. ACM Transactions on Database Systems, 38(4):26, 2013.
1. N. Alon, Y. Matias, and M. Szegedy. The space complexity of approximating the frequency moments. Journal of Computer and System Sciences, 58(1):137–147, 1999.
1. Apache. Datasketches. https://datasketches.apache.org, 2020.
1. Apache. Druid. https://druid.apache.org, 2020.
1. A. Broder and M. Mitzenmacher. Network applications of bloom filters: A survey. Internet mathematics, 1(4):485–509, 2004.
1. E. Cohen. All-distances sketches, revisited: Hip estimators for massive graphsanalysis.IEEE Transactions on Knowledge and Data Engineering, 27(9):2320–2334, 2015.
1. E. Cohen. Stream sampling for frequency cap statistics. In KDD. ACM, 2015.
1. E. Cohen, N. Duffield, H. Kaplan, C. Lund, and M. Thorup. Stream sampling for variance-optimal estimation of subset sums. In Proceedings of the twentieth annual ACM-SIAM symposium on Discrete algorithms, pages 1255–1264. SIAM, 2009.
1. G. Cormode, M. Garofalakis, P. J. Haas, and C. Jermaine. Synopses for massive data: Samples, histograms, wavelets, sketches. Foundations and Trends in Databases, 4(1–3):1–294, 2012.
Data Sketching for Real Time Analytics: Theory and Practice. KDD’20, August 2020, San Diego, CA.
1. G. Cormode and S. Muthukrishnan.  An improved data stream summary: the count-min sketch and its applications. Journal of Algorithms, 55(1):58–75, 2005.
1. G. Cormode, V. Shkapenyuk, D. Srivastava, and B. Xu. Forward decay: A practicaltime decay model for streaming systems. In ICDE, pages 138–149. IEEE, 2009.
1. G. Cormode and P. Veselý.  Tight lower bound for comparison-based quantile summaries. 2019.
1. A. Dasgupta, K. J. Lang, L. Rhodes, and J. Thaler.  A framework for estimating stream expression cardinalities. In ICDT, 2016.
1. N. Duffield, C. Lund, and M. Thorup. Priority sampling for estimation of arbitrarysubset sums. Journal of the ACM (JACM), 54(6):32, 2007.
1. O. Ertl. New cardinality estimation algorithms for hyperloglog sketches. arXiv preprintm arXiv:1702.01284, 2017.
1. B. Fan, D. G. Andersen, M. Kaminsky, and M. D. Mitzenmacher. Cuckoo filter:Practically better than bloom. In Proceedings of the 10th ACM International on Conference on emerging Networking Experiments and Technologies, pages 75–88, 2014.
1. M. Ghashami, E. Liberty, and J. M. Phillips. Efficient frequent directions algorithm for sparse matrices. KDD, 2016.
1. M. Greenwald and S. Khanna.  Space-efficient online computation of quantile summaries.SIGMOD, 2001.
1. Z. Karnin, K. Lang, and E. Liberty. Optimal quantile approximation in streams. In FOCS. IEEE, 2016.
1. E. Liberty. Simple and deterministic matrix sketching. In KDD. ACM, 2013.
1. G. S. Manku, S. Rajagopalan, and B. Lindsay. Random sampling techniques for space efficient online computation of order statistics of large datasets. SIGMOD, 1999.
1. J. Misra and D. Gries. Finding repeated elements. Science of computer programming, 2(2):143–152, 1982.
1. A. Shrivastava, A. C. Konig, and M. Bilenko. Time adaptive sketches (ada-sketches) for summarizing data streams. In Proceedings of the 2016 International Conference on Management of Data, pages 1417–1432, 2016.
1. M. Szegedy.  The DLT priority sampling is essentially optimal. In STOC, pp. 150–158. ACM, 2006.
1. D. Ting. Streamed approximate counting of distinct elements: beating optimal batch methods. In KDD, 2014.
1. D. Ting. Towards optimal cardinality estimation of unions and intersections withsketches.  In Proceedings of the 22nd ACM SIGKDD International Conference on Knowledge Discovery and Data Mining, pages 1195–1204. ACM, 2016.
1. D. Ting. Count-min: Optimal estimation and tight error bounds using empirical error distributions. In Proceedings of the 24th ACM SIGKDD International Conference on Knowledge Discovery & Data Mining, pages 2319–2328. ACM, 2018.
1. D. Ting. Data sketches for disaggregated subset sum and frequent item estimation. In Proceedings of the 2018 International Conference on Management of Data, pp. 1129–1140. ACM, 2018.
1. D. Ting and E. Brochu. Optimal subsampling with influence functions. In Advances in Neural Information Processing Systems, pp. 3650–3659, 2018.
1. L. Wang, G. Luo, K. Yi, and G. Cormode. Quantiles over data streams: an experimental study. In SIGMOD, 2013.
