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
## Frequent Items Sketch Pig UDFs

### Instructions

* get jars
* save the following script as frequent_items.pig
* adjust jar versions and paths if necessary
* save the below data into a file called data.txt
* copy data to hdfs: "hdfs dfs -copyFromLocal data.txt"
* run pig script: "pig frequent_items.pig"

### frequent_items.pig script

    register sketches-core-0.7.0.jar;
    register sketches-pig-0.7.0.jar;

    -- very small sketch just for the purpose of this tiny example
    define dataToSketch org.apache.datasketches.pig.frequencies.DataToFrequentStringsSketch('8');
    define unionSketch org.apache.datasketches.pig.frequencies.UnionFrequentStringsSketch('8');
    define getEstimates org.apache.datasketches.pig.frequencies.FrequentStringsSketchToEstimates();

    a = load 'data.txt' as (item:chararray, category);
    b = group a by category;
    c = foreach b generate flatten(group) as (category), flatten(dataToSketch(a.item)) as (sketch);
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
    describe g;
    dump g;

The example input data has 2 fields: item and category. In the first part of the query the data is grouped by category with one FrequentItemsSketch&lt;String&gt; per category. In the second part of the query this intermediate result is merged across categories to produce one sketch. This way the usage of all 3 UDFs is demonstrated: DataToFrequentStringsSketch, UnionFrequentStringsSketch and FrequentStringsSketchToEstimates.

Results:

From 'dump d' (one sketch per category):

    (c1,{(a,7,7,7),(d,2,2,2),(b,1,1,1)})
    (c2,{(a,5,5,5),(d,2,2,2),(e,1,1,1),(c,1,1,1)})

From 'dump g' (merged across categories):

    ({(a,12,12,12),(d,4,4,4),(b,1,1,1),(e,1,1,1),(c,1,1,1)})

From 'describe g':

    g: {bag_of_item_tuples: {item_tuple: (item: chararray,estimate: long,lower_bound: long,upper_bound: long)}}

In this example the results are exact due to small input.

### [data.txt]({{site.docs_dir}}/FrequentItems/data.txt) (tab separated)
    a	c1
    a	c1
    a	c1
    a	c2
    a	c1
    b	c1
    c	c2
    d	c1
    e	c2
    a	c1
    a	c2
    a	c2
    a	c2
    d	c1
    d	c2
    a	c1
    a	c2
    a	c1
    d	c2
