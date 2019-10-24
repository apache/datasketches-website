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
## Quantiles Sketch Pig UDFs

### Instructions

* get jars from maven.org (search for datasketches)
* save the following script as quantiles.pig
* adjust jar versions and paths if necessary
* save the below data into a file called "data.txt"
* copy data to hdfs: "hdfs dfs -copyFromLocal data.txt"
* run pig script: "pig quantiles.pig"

### quantiles.pig script

    register memory-0.11.0.jar;
    register sketches-core-0.11.1.jar;
    register sketches-pig-0.11.0.jar;

    define dataToSketch org.apache.datasketches.pig.quantiles.DataToDoublesSketch();
    define unionSketch org.apache.datasketches.pig.quantiles.UnionDoublesSketch();
    define getQuantile org.apache.datasketches.pig.quantiles.GetQuantileFromDoublesSketch();

    a = load 'data.txt' as (value:double, category);
    b = group a by category;
    c = foreach b generate flatten(group) as (category), flatten(dataToSketch(a.value)) as sketch;
    -- Sketches can be stored at this point in binary format to be used later:
    -- store c into 'intermediate/$date' using BinStorage();
    -- The next two lines print the results in human readable form for the purpose of this example
    d = foreach c generate category, getQuantile(sketch, 0.5); -- median value from the sketch
    dump d;

    -- This can be a separate query
    -- For example, the first part can produce a daily intermediate feed and store it,
    -- and this part can load several instances of this daily intermediate feed and union them
    -- c = load 'intermediate/$date1,intermediate/$date2' using BinStorage() as (category, sketch);
    e = group c all;
    f = foreach e generate flatten(unionSketch(c.sketch)) as (sketch);
    g = foreach f generate getQuantile(sketch, 0.5); -- median value from the sketch
    dump g;

The example input data has 2 fields: value and category. The first part of the query produces a QuantilesSketch per category, and the second part merges sketches across categories.

From 'dump d':

    (a,6.0)
    (b,16.0)

From 'dump g' (merged across categories):

    (11.0)

### [data.txt]({{site.docs_dir}}/Quantiles/data.txt) (tab separated)
    1	a
    2	a
    3	a
    4	a
    5	a
    6	a
    7	a
    8	a
    9	a
    10	a
    11	b
    12	b
    13	b
    14	b
    15	b
    16	b
    17	b
    18	b
    19	b
    20	b
