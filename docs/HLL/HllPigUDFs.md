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
## Hyper Log Log Sketch Pig UDFs

This functionality appeared in sketches-pig-0.10.0. Depends on sketches-core-0.10.0 and memory-0.10.2.

### Instructions

* get jar
* save the following script as hll.pig
* adjust jar versions and paths if necessary
* save the below data into a file called "data.txt"
* copy data to hdfs: "hdfs dfs -copyFromLocal data.txt"
* run pig script: "pig hll.pig"

### hll.pig script: build sketches, union sketches and get estimates

    register sketches-pig-0.10.0-with-shaded-core.jar;

    -- Parameters are optional. Default is 12
    define dataToSketch org.apache.datasketches.pig.hll.DataToSketch('10');
    define unionSketch org.apache.datasketches.pig.hll.UnionSketch('10');
 
    define getEstimate org.apache.datasketches.pig.hll.SketchToEstimate();
    define printSketch org.apache.datasketches.pig.hll.SketchToString();

    a = load 'data.txt' as (id, category);
    b = group a by category;
    c = foreach b generate flatten(group) as (category), dataToSketch(a.id) as sketch;
    -- Sketches can be stored at this point in binary format to be used later:
    -- store c into 'intermediate/$date' using BinStorage();
    -- The next two lines print the results in human readable form for the purpose of this example
    d = foreach c generate category, getEstimate(sketch);    
    dump d;

    -- This can be a separate query
    -- For example, the first part can produce a daily intermediate feed and store it,
    -- and this part can load several instances of this daily intermediate feed and union them
    -- across categories or days or both
    e = group c all;
    f = foreach e generate unionSketch(c.sketch) as sketch;
    g = foreach f generate getEstimate(sketch);  
    dump g;
    h = foreach f generate printSketch(sketch);
    dump h;

### [data.txt]({{site.docs_dir}}/Theta/data.txt) (tab separated)

The example input data has 2 fields: id and category.
There are 2 categories 'a' and 'b' with 50 unique IDs in each.
Most of the IDs in these categories overlap, so that there are 60 unique IDs in total.

Results:
From 'dump d' (unique count estimate per category):

    (a,50.00000608464168)
    (b,50.00000608464168)

From 'dump g' (union across categories):

    (60.00000879168661)

From 'dump h':

    (### HLL SKETCH SUMMARY: 
      Log Config K   : 10
      Hll Target     : HLL_4
      Current Mode   : SET
      LB             : 60.0
      Estimate       : 60.00000879168661
      UB             : 60.003004547162654
      OutOfOrder Flag: true
    )

### hll_union.pig script: union sketches from different columns

    register sketches-pig-0.10.0-with-shaded-core.jar;

    define dataToSketch org.apache.datasketches.pig.hll.DataToSketch();
    define unionSketch org.apache.datasketches.pig.hll.UnionSketch();
    define estimate org.apache.datasketches.pig.hll.SketchToEstimate();

    a = load 'setops_data.txt' as (id1, id2);
    b = group a all;
    c = foreach b generate
      dataToSketch(a.id1) as sketch1,
      dataToSketch(a.id2) as sketch2;
    d = foreach c generate
      sketch1,
      sketch2,
      unionSketch(TOBAG(sketch1, sketch2)) as a_union_b;
    e = foreach d generate
      estimate(sketch1),
      estimate(sketch2),
      estimate(a_union_b);
    dump e;

### [setops_data.txt]({{site.docs_dir}}/Theta/setops_data.txt) (tab separated)

Result:

    (10.000000223517425,12.000000327825557,18.00000075995926)
