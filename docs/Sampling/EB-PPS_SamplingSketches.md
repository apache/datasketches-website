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
## Contents
<!-- TOC -->
* [EB-PPS Sampling Sketches](#eb-pps-sampling-sketch)
<!-- TOC -->

<a id="eb-pps-sampling-sketch"></a>
## Exact Bound Probability-Proportional-to-Size (EB-PPS) Sampling Sketches

An implementation inspired by the paper:"Exact PPS Sampling with Bounded Sample Size",
B. Hentschel, P. J. Haas, Y. Tian. Information Processing Letters, 2023.

The EB-PPS Sampling Sketch is a one-pass streaming algorithm that ensures Probability-Proportional-to-Size (PPS) sampling while strictly bounding the sample size to a target *k*. It maintains the exact PPS property, handles imbalanced data efficiently, and is computationally superior for large-scale applications compared to other reservoir-type sketches.

Key features and benefits of EB-PPA include:

* **Sample Size Control:** EB-PPS ensures the sample size never exceeds the target *k*, making it ideal for scenarios requiring strict storage or time constraints.
* **Optimal Statistical Properties:** When possible, the sample size is exactly *k*, while in other cases, it maintains maximal expected value and minimal variance.
* **Efficiency:** As a one-pass streaming algorithm, it is highly efficient, which is crucial for processing large, real-time, or streaming data.
* **Application in Machine Learning:** This method is particularly useful for training classifiers on imbalanced datasets, where it helps in overcoming issues with biased results.
* **Comparison with Other Methods:** In the above referenced paper, EB-PPS is compared to other methods such as Conditional Poisson Sampling (CPS) and Pareto PPS sampling, which also aim to have inclusion probabilities proportional to given size measures.

EB-PPS is advantageous for its simplicity, efficiency, and ability to balance the need for exact PPS with the constraint of a fixed sample size.

EB-PPS is a modern sampling scheme designed to resolve a fundamental conflict in statistical sampling: the inability to simultaneously guarantee exact **Probability Proportional to Size (PPS)** and a fixed sample size for all datasets. 

### Key Concepts of EB-PPS
Traditional PPS schemes often prioritize keeping a fixed sample size *k*, which sometimes forces them to violate the PPS property (where each item's selection probability must be exactly proportional to its weight). EB-PPS flips this trade-off: 

* **Strict PPS Property:** It enforces the PPS property at all times, ensuring item-inclusion probabilities are exactly proportional to their weights.
* **Bounded Sample Size:** It uses the target *k* as a strict upper bound rather than a fixed requirement.
* **Optimality:** If the sample size cannot be exactly *k* while maintaining strict PPS, the algorithm produces a sample with the maximal possible expected size and minimal variance.

### Computational Advantages

EB-PPS is particularly suited for high-volume data environments and 2026+ era big data analytics: 

* **One-Pass Streaming:** It processes items sequentially and does not need to know the total population size in advance.

* **Efficiency:** It features an amortized processing time of *O(1)* **per item**, making it computationally superior to older state-of-the-art algorithms like VarOpt, which has *O(\log n)* complexity.

* **Memory Management:** By bounding the sample size, it prevents storage overflows during real-time data ingestion.

### Applications in Machine Learning

A primary 2026 application for EB-PPS is training classifiers on imbalanced datasets: 

* **Bayes-Optimality:** By maintaining exact PPS, it allows standard training techniques (designed for 0-1 loss) to produce Bayes-optimal classifiers even when the true loss function is imbalanced.
* **Superior Performance:** Research indicates that smaller, well-curated EB-PPS samples can outperform larger samples from traditional schemes (like VarOpt) because they avoid the bias introduced by violating PPS.
