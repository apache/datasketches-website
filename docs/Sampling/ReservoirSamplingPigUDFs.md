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
## Reservoir Sampling Sketch Pig UDFs

### Instructions

* get jars
* save the following script as varopt_example.pig
* adjust jar versions and paths as necessary
* save the below data into a file called data.txt
* copy data to hdfs: "hadoop fs -copyFromLocal data.txt"
* run pig script: "pig reservoir_example.pig"

### reservoir_example.pig script

    register sketches-core-0.10.0.jar;
    register sketches-pig-0.10.0.jar;

    -- very small sketch just for the purpose of this tiny example
    DEFINE ReservoirSampling org.apache.datasketches.pig.sampling.ReservoirSampling('4');
    DEFINE ReservoirUnion org.apache.datasketches.pig.sampling.ReesrvoirUnion('4');

    raw_data = LOAD 'data.txt' USING PigStorage('\t') AS
        (scale: double, label: chararray);

    -- make a few independent sketches from the input data
    sketches = FOREACH
        (GROUP raw_data ALL)
    GENERATE
        DataToSketch(raw_data) AS sketch0,
        DataToSketch(raw_data) AS sketch1,
        DataToSketch(raw_data) AS sketch2
        ;

    sketchBag = FOREACH
        sketches
    GENERATE
        TOBAG(sketch0,
              sketch1,
              sketch2))
        ;

    result = FOREACH
        sketchBag
    GENERATE
        FLATTEN(ReservoirUnion(*)) AS (n, k, samples:{(scale, label)})
        ;

    DUMP result;
    DESCRIBE result;

The test data has 2 fields: scale and label. The first step of the query creates several reservoir samples from the input data. We merge the sketches into a bag in the next step, and then union the independent sketches and dump the results.

Results:

From 'DUMP result':

    (24,4,{(30.0,h),(7.0,g),(6.0,f),(5.0,e)})

Running this script many, we will see each element appear with equal probability.

From 'DESCRIBE result':

    result: {n: long,k: int,samples: {(scale: double,label: chararray)}}

### [data.txt]({{site.docs_dir}}/Sampling/data.txt) (tab separated)
    1.0	a
    2.0	b
    3.0	c
    4.0	d
    5.0	e
    6.0	f
    7.0	g
    30.0	h
