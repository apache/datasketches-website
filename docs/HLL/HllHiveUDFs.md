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
## Hyper Log Log sketch Hive UDFs

This functionality appeared in sketches-hive-0.10.1. Depends on sketches-core-0.10.0 and memory-0.10.2.

### Building sketches, computing unions and getting estimates

    add jar sketches-hive-0.10.5-with-shaded-core.jar;

    create temporary function data2sketch as 'org.apache.datasketches.hive.hll.DataToSketchUDAF';
    create temporary function unionSketches as 'org.apache.datasketches.hive.hll.UnionSketchUDAF';
    create temporary function estimate as 'org.apache.datasketches.hive.hll.SketchToEstimateUDF';

    use <your-db-name-here>;

    -- prepare input data
    create temporary table sketch_input (id int, category char(1));
    insert into table sketch_input values
      (1, 'a'), (2, 'a'), (3, 'a'), (4, 'a'), (5, 'a'), (6, 'a'), (7, 'a'), (8, 'a'), (9, 'a'), (10, 'a'),
      (6, 'b'), (7, 'b'), (8, 'b'), (9, 'b'), (10, 'b'), (11, 'b'), (12, 'b'), (13, 'b'), (14, 'b'), (15, 'b');

    -- build sketches per category
    create temporary table sketch_intermediate (category char(1), sketch binary);
    insert into sketch_intermediate select category, data2sketch(id) from sketch_input group by category;

    -- get unique count estimates per category
    select category, estimate(sketch) from sketch_intermediate;

    Output:
    a	10.000000223517425
    b	10.000000223517425

    -- union sketches across categories and get overall unique count estimate
    select estimate(unionSketches(sketch)) from sketch_intermediate;

    Output:
    15.000000521540663

### Union two sketches

Notice the difference between UnionUDF in this example, which takes two sketches, and UnionUDAF in the previous example, which is an aggregate function taking a collection of sketches as one parameter.

    add jar sketches-hive-0.10.5-with-shaded-core.jar;

    create temporary function data2sketch as 'org.apache.datasketches.hive.hll.DataToSketchUDAF';
    create temporary function estimate as 'org.apache.datasketches.hive.hll.SketchToEstimateUDF';
    create temporary function unionTwoSketches as 'org.apache.datasketches.hive.hll.UnionSketchUDF';

    use <your-db-name-here>;

    -- prepare input data
    create temporary table sketch_input (id1 int, id2 int);
    insert into table sketch_input values (1, 2), (2, 4), (3, 6), (4, 8), (5, 10), (6, 12), (7, 14), (8, 16), (9, 18), (10, 20);

    -- build two sketches
    create temporary table sketch_intermediate (sketch1 binary, sketch2 binary);
    insert into sketch_intermediate select data2sketch(id1), data2sketch(id2) from sketch_input;

    -- get estimates from sketches and union
    select estimate(sketch1), estimate(sketch2), estimate(unionTwoSketches(sketch1, sketch2)) from sketch_intermediate;

    Output:
    10.000000223517425	10.000000223517425	15.000000521540663
