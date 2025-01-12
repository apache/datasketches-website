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

## HLL Maximum<sup>1</sup> Sketch Size & Error Table

### Maximum<sup>1</sup> Serialized Size in Bytes Given lgConfigK and tgtHllType And
### Maximum Relative Error given chosen # of Standard Deviations from the Estimate

#### Columns
1. Chosen LgConfigK of the sketch
2. Chosen tgtHllType is HLL_4
3. Chosen tgtHllType is HLL_6
4. Chosen tgtHllThpe is HLL_8
5. Chosen # Standard Deviations = 1 corresponds to a 68.3% confidence interval
6. Chosen # Standard Deviations = 2 corresponds to a 95.5% confidence interval
7. Chosen # Standard Deviations = 3 corresponds to a 99.7% confidence interval 

|lgConfigK|HLL_4<br>Bytes|HLL_6<br>Bytes|HLL_8<br>Bytes|1 StdDev<br>68.3%|2 StdDev<br>95.5%|3 StdDev<br>99.7%|
|:----------:|:----------:|:----------:|:----------:|:----------:|:----------:|:----------:|
|           4|          64|          53|          56|      25.698%|      41.191%|      52.651%|
|           5|          72|          65|          72|      18.233%|      31.028%|      41.266%|
|           6|          88|          89|         104|      12.931%|      23.014%|      31.564%|
|           7|         136|         137|         168|       9.158%|      16.834%|      23.635%|
|           8|         200|         233|         296|       6.487%|      12.205%|      17.411%|
|           9|         328|         425|         552|       4.591%|       8.785%|      12.692%|
|          10|         616|         809|       1,064|       3.243%|       6.290%|       9.186%|
|          11|       1,128|       1,577|       2,088|       2.296%|       4.488%|       6.574%|
|          12|       2,216|       3,113|       4,136|       1.619%|       3.183%|       4.697%|
|          13|       4,264|       6,185|       8,232|       1.148%|       2.296%|       3.444%|
|          14|       8,488|      12,329|      16,424|       0.812%|       1.623%|       2.435%|
|          15|      16,936|      24,617|      32,808|       0.574%|       1.148%|       1.722%|
|          16|      33,832|      49,193|      65,576|       0.406%|       0.812%|       1.218%|
|          17|      67,624|      98,345|     131,112|       0.287%|       0.574%|       0.861%|
|          18|     135,208|     196,649|     262,184|       0.203%|       0.406%|       0.609%|
|          19|     270,376|     393,257|     524,328|       0.143%|       0.287%|       0.430%|
|          20|     540,712|     786,473|   1,048,616|       0.101%|       0.203%|       0.304%|
|          21|   1,081,384|   1,572,905|   2,097,192|       0.072%|       0.143%|       0.215%|

### Notes

1. The max sizes for the HLL_4 sketch type can be exceeded, but it is very rare. If it is exceeded it would be by a few percent.
2. The minimum serialized size (for an empty sketch) is 8 bytes.


