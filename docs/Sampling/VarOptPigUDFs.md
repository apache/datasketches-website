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
## VarOpt Sampling Sketch Pig UDFs

### Instructions

* get jars
* save the following script as varopt_example.pig
* adjust jar versions and paths as necessary
* save the below data into a file called data.txt
* copy data to hdfs: "hadoop fs -copyFromLocal data.txt"
* run pig script: "pig varopt_example.pig"

### varopt_example.pig script

    register sketches-core-0.10.0.jar;
    register sketches-pig-0.10.0.jar;

    -- very small sketch just for the purpose of this tiny example
    DEFINE DataToSketch org.apache.datasketches.pig.sampling.DataToVarOptSketch('4', '0');
    DEFINE VarOptUnion org.apache.datasketches.pig.sampling.VarOptUnion('4');
    DEFINE GetVarOptSamples org.apache.datasketches.pig.sampling.GetVarOptSamples();

    raw_data = LOAD 'data.txt' USING PigStorage('\t') AS
        (weight: double, id: chararray);

    -- make a few independent sketches from the input data
    bytes = FOREACH
        (GROUP raw_data ALL)
    GENERATE
        DataToSketch(raw_data) AS sketch0,
        DataToSketch(raw_data) AS sketch1
        ;

    sketchBag = FOREACH
        bytes
    GENERATE
        TOBAG(sketch0,
              sketch1)) AS sketches
        ;

    unioned = FOREACH
        sketchBag
    GENERATE
        VarOptUnion(sketchBag.sketches) AS binSketch
        ;

    result = FOREACH
        unioned
    GENERATE
        FLATTEN(GetVarOptSamples(binSketch)) AS (vo_weight, record:(id, weight))
        ;

    DUMP result;
    DESCRIBE result;

The test data has 2 fields: weight and id. The first step of the query creates several varopt sketches from the input data. We merge the sketches into a bag in the next step, followed by unioning the set of independent sketches. Finally, the last step gets the final set of results.

Results:

From 'DUMP result':

    (30.0,(30.0,h))
    (30.0,(30.0,h))
    (28.0,(4.0,d))
    (28.0,(7.0,g))

By running this script repeatedly, we can obesrve that the heavy items
(h) will always be included, but that the remaining 2 items will
differ across runs, appearing in proportion to their weights. We can
also see that the output varopt weight on the non-heavy samples
represents an adjusetd weight, although by keeping the entire input
tuple the original weight value is also stored.

From 'DESCRIBE result':

    result: {vo_weight: double,record: (id: bytearray,weight: bytearray)}

### [data.txt]({{site.docs_dir}}/Sampling/data.txt) (tab separated)
    1.0	a
    2.0	b
    3.0	c
    4.0	d
    5.0	e
    6.0	f
    7.0	g
    30.0	h
