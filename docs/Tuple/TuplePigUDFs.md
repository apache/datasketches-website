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
## Tuple Sketch Pig UDFs

### Instructions

* get jars
* save the following script as tuple.pig
* adjust jar versions and paths if necessary
* save the below data into a file called "data.txt"
* copy data to hdfs: "hdfs dfs -copyFromLocal data.txt"
* run pig script: "pig tuple.pig"

### tuple.pig script

    register sketches-core-0.7.0.jar;
    register sketches-pig-0.7.0.jar;

    define dataToSketch org.apache.datasketches.pig.tuple.DataToDoubleSummarySketch('32');
    define unionSketch org.apache.datasketches.pig.tuple.UnionDoubleSummarySketch('32');
    define getEstimates org.apache.datasketches.pig.tuple.DoubleSummarySketchToEstimates();

    a = load 'data.txt' as (id, category, parameter:double);
    b = group a by category;
    c = foreach b {
      pair = foreach a generate id, parameter;
      generate flatten(group) as (category), flatten(dataToSketch(pair)) as (sketch);
    }
    -- Sketches can be stored at this point in binary format to be used later:
    -- store c into 'intermediate/$date' using BinStorage();
    -- The next two lines print the results in human readable form for the purpose of this example
    d = foreach c generate category, getEstimates(sketch);
    dump d;

    -- This can be a separate query.
    -- For example, the first part can produce a daily intermediate feed and store it.
    -- This part can load several instances of this daily intermediate feed and merge them
    -- c = load 'intermediate/$date1,intermediate/$date2' using BinStorage() as (category, sketch);
    e = group c all;
    f = foreach e generate flatten(unionSketch(c.sketch)) as (sketch);
    g = foreach f generate getEstimates(sketch);
    dump g;

The example input data has 3 fields: id, category and numeric parameter.
There are 2 categories 'a' and 'b' with 50 unique IDs in each.
Most of the IDs in these categories overlap, so that there are 60 unique IDs in total.
The numeric parameter has the sum of 500.0 per category, and 1000.0 in total.

Results:
From 'dump d':

    (a,(50.0,500.0))
    (b,(50.0,500.0))

From 'dump g' (merged across categories):

    ((50.415577215639736,846.0364051499544))

In this example the sketches have a small size of 32 nominal entries. From the first dump one can see that the sketches are still in the exact counting mode.
Merging across categories pushed the resulting sketch into the estimation mode. The expected exact result would be (60.0, 1000.0).
The estimates have high relative error because the sketch was configured with only 32 nominal entries.

### [data.txt]({{site.docs_dir}}/Tuple/data.txt) (tab separated)
    01	a	10.0
    02	a	10.0
    03	a	9.0
    04	a	11.0
    05	a	8.0
    06	a	12.0
    07	a	7.0
    08	a	13.0
    09	a	9.0
    10	a	11.0
    11	a	8.0
    12	a	12.0
    13	a	7.0
    14	a	13.0
    15	a	10.0
    16	a	10.0
    17	a	9.0
    18	a	11.0
    19	a	8.0
    20	a	12.0
    21	a	10.0
    22	a	10.0
    23	a	6.0
    24	a	14.0
    25	a	5.0
    26	a	15.0
    27	a	7.0
    28	a	13.0
    29	a	10.0
    30	a	10.0
    31	a	8.0
    32	a	12.0
    33	a	7.0
    34	a	13.0
    35	a	10.0
    36	a	10.0
    37	a	9.0
    38	a	11.0
    39	a	8.0
    40	a	12.0
    41	a	10.0
    42	a	10.0
    43	a	6.0
    44	a	14.0
    45	a	5.0
    46	a	15.0
    47	a	7.0
    48	a	13.0
    49	a	10.0
    50	a	10.0
    11	b	9.0
    12	b	11.0
    13	b	8.0
    14	b	12.0
    15	b	5.0
    16	b	15.0
    17	b	7.0
    18	b	13.0
    19	b	10.0
    20	b	10.0
    21	b	9.0
    22	b	11.0
    23	b	7.0
    24	b	13.0
    25	b	8.0
    26	b	12.0
    27	b	6.0
    28	b	14.0
    29	b	10.0
    30	b	10.0
    31	b	8.0
    32	b	12.0
    33	b	7.0
    34	b	13.0
    35	b	9.0
    36	b	11.0
    37	b	10.0
    38	b	10.0
    39	b	8.0
    40	b	12.0
    41	b	9.0
    42	b	11.0
    43	b	7.0
    44	b	13.0
    45	b	8.0
    46	b	12.0
    47	b	6.0
    48	b	14.0
    49	b	10.0
    50	b	10.0
    51	b	8.0
    52	b	12.0
    53	b	7.0
    54	b	13.0
    55	b	9.0
    56	b	11.0
    57	b	10.0
    58	b	10.0
    59	b	8.0
    60	b	12.0
