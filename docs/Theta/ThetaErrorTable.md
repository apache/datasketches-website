---
sectionid: docs
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

## Choosing Desired Relative Error For Theta Sketches
Note: Early on, we used the term "Nominal Entries" as an alias for K, because there is only a nominal relationship between the parameter given in the class constructor and the actual number of retained values (and thus its size) of the sketch.  This was an intentional trade-off to improve performance.  Nonetheless, the number of retained entries as well as the current size of the sketch can always be obtained from the sketch itself.  Now we find it more convenient and compact to just use the term "K". 

### Quick Select Sketch (Default)



&nbsp;    |#Std Dev:   |1      |2      |3      |
:--------:|:----------:|:-----:|:-----:|:-----:|
          |<b>Conf:</b>|<b>68.27%</b>|<b>95.45%</b>|<b>99.73%</b>|
<b>LgK</b>|<b>K</b>    |<b>1 RSE</b>|<b>2 RSE</b>|<b>3 RSE</b>|
4         |16          |25.820%|51.640%|77.460%|
5         |32          |17.961%|35.921%|53.882%|
6         |64          |12.599%|25.198%|37.796%|
7         |128         | 8.874%|17.747%|26.621%|
8         |256         | 6.262%|12.524%|18.787%|
9         |512         | 4.424%| 8.847%|13.271%|
10        |1,024       | 3.127%| 6.253%| 9.380%|
11        |2,048       | 2.210%| 4.420%| 6.631%|
12        |4,096       | 1.563%| 3.125%| 4.688%|
13        |8,192       | 1.105%| 2.210%| 3.315%|
14        |16,384      | 0.781%| 1.563%| 2.344%|
15        |32,768      | 0.552%| 1.105%| 1.657%|
16        |65,536      | 0.391%| 0.781%| 1.172%|
17        |131,072     | 0.276%| 0.552%| 0.829%|
18        |262,144     | 0.195%| 0.391%| 0.586%|
19        |524,288     | 0.138%| 0.276%| 0.414%|
20        |1,048,576   | 0.098%| 0.195%| 0.293%|
21        |2,097,152   | 0.069%| 0.138%| 0.207%|
22        |4,194,304   | 0.049%| 0.098%| 0.146%|
23        |8,388,608   | 0.035%| 0.069%| 0.104%|
24        |16,777,216  | 0.024%| 0.049%| 0.073%|
25        |33,554,432  | 0.017%| 0.035%| 0.052%|
26        |67,108,864  | 0.012%| 0.024%| 0.037%|

### Alpha Sketch
Note: These RSE calculations are only valid for an Alpha Sketch prior to any merge (Union) operation.  After a union operation the RSE reverts to the QuickSelect numbers above.

&nbsp;    |#Std Dev:    |1      |2      |3      |
:--------:|:----------:|:-----:|:-----:|:-----:|
          |<b>Conf:</b>|<b>68.27%</b>|<b>95.45%</b>|<b>99.73%</b>|
<b>LgK</b>|<b>K</b>    |<b>1 RSE</b>|<b>2 RSE</b>|<b>3 RSE</b>|
9         |512         | 3.132%| 6.264%| 9.396%|
10        |1,024       | 2.214%| 4.427%| 6.641%|
11        |2,048       | 1.565%| 3.130%| 4.695%|
12        |4,096       | 1.106%| 2.213%| 3.319%|
13        |8,192       | 0.782%| 1.565%| 2.347%|
14        |16,384      | 0.553%| 1.106%| 1.659%|
15        |32,768      | 0.391%| 0.782%| 1.173%|
16        |65,536      | 0.277%| 0.553%| 0.830%|
17        |131,072     | 0.196%| 0.391%| 0.587%|
18        |262,144     | 0.138%| 0.277%| 0.415%|
19        |524,288     | 0.098%| 0.196%| 0.293%|
20        |1,048,576   | 0.069%| 0.138%| 0.207%|
21        |2,097,152   | 0.049%| 0.098%| 0.147%|
22        |4,194,304   | 0.035%| 0.069%| 0.104%|
23        |8,388,608   | 0.024%| 0.049%| 0.073%|
24        |16,777,216  | 0.017%| 0.035%| 0.052%|
25        |33,554,432  | 0.012%| 0.024%| 0.037%|
26        |67,108,864  | 0.009%| 0.017%| 0.026%|
