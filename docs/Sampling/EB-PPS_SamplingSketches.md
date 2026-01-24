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
    * [Key Concepts of EB-PPS](#key-concepts-of-eb-pps)
    * [Computational Advantages](#computational-advantages)
    * [Applications in Machine Learning](#applications-in-ML)
    * [EB-PPS versus VarOpt for large datasets](#eb-pps-vs-varopt1)
    * [The fixed sample size verses PPS property tradeoff](#sample-size-vs-pps-tradeoff)
        * [The Core Conflict](#the-core-conflict)
        * [Prioritizing Fixed Sample Size (e.g., VarOpt)](#fixed-sample-size)
        * [Prioritizing the PPS Property (e.g., EB-PPS)](#pps-property)
        * [Summary Table](#summary-table)
    * [How EB-PPS works with varying weights](#eb-pps-weights)
        * [EB-PPS versus VarOpt Table ](#eb-pps-vs-varopt2)
    * [Situations that make PPS vital for classifier performance](#pps-for-classifiers)
        * 1. [Training Bayes-Optimal Classifiers](#training-classifiers)
        * 2. [Handling Severe Class Imbalance](#class-imbalance)
        * 3. [Maintaining Probability Calibration](#probability-calibration)
        * 4. [Legal and Ethical Fairness](#legal-ethical-fairness)

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

EB-PPS is a modern sampling scheme designed to resolve a fundamental conflict in statistical sampling: the inability to simultaneously guarantee exact **Probability Proportional to Size (PPS)** and a fixed sample size for all datasets. It is advantageous for its simplicity, efficiency, and ability to balance the need for exact PPS with the constraint of a fixed sample size. 

<a id="key-concepts-of-eb-pps"></a>
### Key Concepts of EB-PPS
Traditional PPS schemes often prioritize keeping a fixed sample size *k*, which sometimes forces them to violate the PPS property (where each item's selection probability must be exactly proportional to its weight). EB-PPS flips this trade-off: 

* **Strict PPS Property:** It enforces the PPS property at all times, ensuring item-inclusion probabilities are exactly proportional to their weights.
* **Bounded Sample Size:** It uses the target *k* as a strict upper bound rather than a fixed requirement.
* **Optimality:** If the sample size cannot be exactly *k* while maintaining strict PPS, the algorithm produces a sample with the maximal possible expected size and minimal variance.

<a id="computational-advantages"></a>
### Computational Advantages

EB-PPS is particularly suited for high-volume data environments and modern era big data analytics: 

* **One-Pass Streaming:** It processes items sequentially and does not need to know the total population size in advance.

* **Efficiency:** It features an amortized processing time of *O(1)* **per item**, making it computationally superior to older state-of-the-art algorithms like VarOpt, which has *O(log k)* complexity.

* **Memory Management:** By bounding the sample size, it prevents storage overflows during real-time data ingestion.

<a id="applications-in-ML"></a>
### Applications in Machine Learning

A primary modern application for EB-PPS is training classifiers on imbalanced datasets: 

* **Bayes-Optimality:** By maintaining exact PPS, it allows standard training techniques (designed for 0-1 loss) to produce Bayes-optimal classifiers even when the true loss function is imbalanced.
* **Superior Performance:** Research indicates that smaller, well-curated EB-PPS samples can outperform larger samples from traditional schemes (like VarOpt) because they avoid the bias introduced by violating PPS.

<a id="eb-pps-vs-varopt1"></a>
### EB-PPS versus VarOpt for large datasets
EB-PPS is more efficient than VarOpt for large, high-volume datasets due to its superior computational complexity and optimized sample curation. Key advantages include:

* **Optimal Computational Complexity:** EB-PPS achieves an *O(1)* **amortized processing time** per item. This is an improvement over VarOpt, which typically requires *O(log k)* (or *O(log log k)* in some streaming implementations) time per item to maintain its internal structures.
* **Reduced Sample Size Requirements:** Because EB-PPS maintains strict PPS properties, it can achieve superior results with smaller samples. Research shows EB-PPS can produce classifiers with roughly 36% less loss than VarOpt while using sample sizes that are **one-third to one-half as large.** Smaller samples directly translate to faster downstream analytics and lower memory overhead.
* **Simplified Data Structures:** EB-PPS uses a single array of size *k* and does not require complex heap-based management, making it easier to implement and maintain in parallel systems.
* **Avoidance of Bias-Correction Overhead:** VarOpt often violates exact PPS to maintain a fixed sample size, requiring complex customized loss functions or additional training procedures to correct this bias. EB-PPS avoids these secondary computational costs by ensuring inclusion probabilities are "as advertised" from the start.

<a id="sample-size-vs-pps-tradeoff"></a>
### The fixed sample size verses PPS property tradeoff
The primary trade-off in weighted sampling remains the tension between maintaining a fixed sample size *k* and strictly adhering to the PPS property. These two goals are mathematically incompatible in many real-world datasets.

<a id="the-core-conflict"></a>
#### The Core Conflict
The fundamental issue arises when some items have such high weights that they "crowd out" the rest of the sample: 

* **Incompatibility:** If an item's weight is disproportionately large compared to the total weight of the population, a fixed *k* may force the algorithm to sample lower-weight items more frequently than their weight allows just to fill the *k* slots.
* **Statistical Implication:** When a fixed size is prioritized, the inclusion probabilities are no longer truly proportional to size (PPS), introducing statistical bias.

<a id="fixed-sample-size"></a>
#### Prioritizing Fixed Sample Size (e.g., VarOpt)
Most traditional PPS schemes, like VarOpt, prioritize keeping the sample size exactly at *k*.

* **Advantage:** Predictable resource consumption and downstream processing time, as the analyst knows exactly how many data points will be returned.
* **Disadvantage:** Violates the PPS property when large-weight items exist. This requires complex bias-correction or specialized loss functions during model training.

<a id="pps-property"></a>
#### Prioritizing the PPS Property (e.g., EB-PPS)
Modern approaches like EB-PPS prioritize strict adherence to the PPS property over a fixed count.

* **Advantage:** Maintains "as advertised" inclusion probabilities. This allows standard training algorithms to produce **Bayes-optimal classifiers** directly, which is critical for imbalanced datasets.
* **Disadvantage:** The sample size becomes a variable (though bounded by *k*). The actual size may fall below *k* if strict PPS compliance necessitates a smaller, more representative sample. 

<a id="summary-table"></a>
#### Summary Table

| Priority          | Strategy                    | Consequence              |
|:------------------|:----------------------------|:-------------------------|
| <b>Fixed Size</b> | Fill k slots at all costs.  | PPS property is violated; statistical bias is introduced. |
| <b>Strict PPS</b> | Use probabilities strictly proportional to weights. | Sample size becomes a variable upper-bounded by *k*. |

<a id="eb-pps-weights"></a>
### How EB-PPS works with varying weights
The algorithm's efficiency stems from how it handles incoming items regardless of their individual "size" or weight:

* **Flat Data Structure:** EB-PPS uses only a single array of size *k* (where *k* is the target sample size). Unlike VarOpt, which relies on a heap or priority queue to manage items by weight (resulting in *O(log k)* costs), EB-PPS performs simple updates to its array.
* **Constant-Time Decision Logic:** When a new item arrives with a specific weight, the algorithm performs a fixed number of arithmetic operations to determine if it should be included. These operations do not scale with the total population size or the sample size *k*.
* **Amortized Rebalancing:** While most items are processed in a single step, certain items may trigger a "reweighting" or rebalancing of the internal sample. Because these events occur with predictable, low frequency relative to the total number of items, the cost is spread out (amortized) to maintain an average of *O(1)* **per item**.
* **Weight Independence:** The computational cost remains constant whether the item has a very large weight (making it almost certain to be included) or a very small weight (making it rare). The algorithm only processes the current item's weight against its internal state, never needing to re-sort or re-scan the entire reservoir for every new entry.

<a id="eb-pps-vs-varopt2"></a>
#### EB-PPS versus VarOpt Table

| Feature | EB-PPS | VarOpt |
|:--------|:-------|:-------|
| <b>Complexity per Item</b> | *O(1)* (Amortized) | *O(log k)* (Standard) |
| <b>Data Structure</b> | Simple Array | Priority Queue / Heap |
| <b>PPS Property</b> | <b>Strictly enforced</b> | Violated if necessary to fix *k* |
| <b>Sample Size *(k)*</b> | Upper bound (Variable) | Fixed (Strict) |

<a id="pps-for-classifiers"></a>
### Situations that make PPS vital for classifier performance
Today, strict adherence to the Probability Proportional to Size (PPS) property—as prioritized by schemes like EB-PPS—is considered vital for classifier performance in the following high-stakes scenarios:

<a id="training-classifiers"></a>
#### 1: Training Bayes-Optimal Classifiers

For a classifier to be truly "optimal," it must minimize expected risk based on the data's true underlying distribution.

* **The PPS Link:** Exact PPS sampling ensures that the training subset maintains the statistical integrity of the original dataset's weights.
* **Direct Training:** When PPS is strictly maintained, you can use standard training algorithms (optimized for 0-1 loss) to produce Bayes-optimal decision boundaries. If PPS is violated (as often happens with fixed-size schemes like VarOpt), the resulting decision boundary shifts, leading to suboptimal performance unless complex, custom loss-correction is applied.

<a id="class-imbalance"></a>
#### 2: Handling Severe Class Imbalance
In datasets where the minority class is extremely rare (e.g., fraud detection or rare disease diagnosis), small errors in inclusion probability can cause the classifier to ignore critical but rare signals.

* **Avoiding "Majority Bias":** Inaccurate sampling often leads a model to simply predict the majority class for all instances to achieve high "accuracy" while failing at its actual task.
* **Exact Representation:** Strict PPS ensures that the weight assigned to these rare cases is exactly preserved in the sample, forcing the model to learn the minority class features correctly.

<a id="probability-calibration"></a>
#### 3: Maintaining Probability Calibration
Calibration refers to the model's ability to provide accurate probability estimates (e.g., "there is a 70% chance of malignancy") rather than just a 0/1 label.

* **Clinical Utility:** In healthcare, over- or under-estimating risks can lead to dangerous overtreatment or missed diagnoses.
* **PPS Advantage:** Because EB-PPS does not distort the inclusion probabilities to force a fixed sample size, the resulting model is inherently better calibrated. The probabilities it outputs reflect the true risk levels of the original population.

<a id="legal-ethical-fairness"></a>
#### 4: Legal and Ethical Fairness
Today, algorithmic fairness is a major regulatory focus. Biased sampling is a primary source of "AI bias" that leads to prejudiced outcomes in lending, hiring, or healthcare. 

* **Predictive Parity:** Strict PPS allows for the construction of fair Bayes-optimal classifiers that satisfy "predictive parity," ensuring that the model's error rates and accuracy are consistent across different protected groups (e.g., race, gender).
* **Liability Mitigation:** Using sampling methods that guarantee statistical integrity (like EB-PPS) helps developers demonstrate that their models were trained on a representative, unbiased representation of the data, reducing legal liability for "erroneous training data". 


*(Note: much of this overview was generated by Gemini AI, it may contain errors.)*
