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
## CPC Sketch Pig UDFs

### Instructions

* get jars
* save the following script as cpc.pig
* adjust jar versions and paths if necessary
* save the below data into a file called "data.txt"
* copy data to hdfs: "hdfs dfs -copyFromLocal data.txt"
* run pig script: "pig cpc.pig"

### cpc.pig script: building sketches, merging sketches and getting estimates

    register datasketches-memory-2.0.0.jar;
    register datasketches-java-3.1.0.jar;
    register datasketches-pig-1.1.0.jar;

    define dataToSketch org.apache.datasketches.pig.cpc.DataToSketch('12');       
    define unionSketch org.apache.datasketches.pig.cpc.UnionSketch('12');       
    define getEstimate org.apache.datasketches.pig.cpc.GetEstimate();     
    define getEstimateAndBounds org.apache.datasketches.pig.cpc.GetEstimateAndErrorBounds('3');       
    define toString org.apache.datasketches.pig.cpc.SketchToString();              

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
    -- and this part can load several instances of this daily intermediate feed and merge them
    e = group c all;
    f = foreach e generate unionSketch(c.sketch) as sketch;
    g = foreach f generate getEstimate(sketch);
    dump g;

    h = foreach f generate flatten(getEstimateAndBounds(sketch)) as (estimate, lb, ub);
    dump h;

### [data.txt]({{site.docs_dir}}/Theta/data.txt) (tab separated)

The example input data has 2 fields: id and category.
There are 2 categories 'a' and 'b' with 50 unique IDs in each.
Most of the IDs in these categories overlap, so that there are 60 unique IDs in total.

Results:
From 'dump d':

    (a,50.09992602861082)
    (b,50.09992602861082)

From 'dump g' (merged across categories):

    (60.14445031168714)

From 'dump h' (with error bounds, 99% confidence interval):

    (60.14445031168714,60.0,63.0)
