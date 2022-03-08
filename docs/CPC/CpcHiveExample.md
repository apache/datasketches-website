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
## CPC sketch Hive UDFs

### Building sketches, merging sketches and getting estimates

    add jar datasketches-memory-2.0.0.jar;
    add jar datasketches-java-3.1.0.jar;
    add jar datasketches-hive-1.2.0.jar;

    create temporary function data2sketch as 'org.apache.datasketches.hive.cpc.DataToSketchUDAF';
    create temporary function unionSketches as 'org.apache.datasketches.hive.cpc.UnionSketchUDAF';
    create temporary function estimate as 'org.apache.datasketches.hive.cpc.GetEstimateUDF';
    create temporary function estimateAndBounds as 'org.apache.datasketches.hive.cpc.GetEstimateAndErrorBoundsUDF';

    use <your-db-name-here>;

    create temporary table sketch_input (id int, category char(1));
    insert into table sketch_input values
      (1, 'a'), (2, 'a'), (3, 'a'), (4, 'a'), (5, 'a'), (6, 'a'), (7, 'a'), (8, 'a'), (9, 'a'), (10, 'a'),
      (6, 'b'), (7, 'b'), (8, 'b'), (9, 'b'), (10, 'b'), (11, 'b'), (12, 'b'), (13, 'b'), (14, 'b'), (15, 'b');

    create temporary table sketch_intermediate (category char(1), sketch binary);
    insert into sketch_intermediate select category, data2sketch(id) from sketch_input group by category;

    select category, estimate(sketch) from sketch_intermediate;

    Output:
    a	10.007331400971685
    b	10.007331400971685

    select estimate(unionSketches(sketch)) from sketch_intermediate;

    Output:
    15.017114660336853

    select estimateAndBounds(unionSketches(sketch)) from sketch_intermediate;

    Output:
    [15.017114660336853,15.0,16.0]

### Union of two sketches

Notice the difference between UnionUDF in this example, which takes two sketches, and UnionUDAF in the previous example, which is an aggregate function taking a collection of sketches as one parameter.

    add jar datasketches-memory-2.0.0.jar;
    add jar datasketches-java-3.1.0.jar;
    add jar datasketches-hive-1.2.0.jar;

    create temporary function data2sketch as 'org.apache.datasketches.hive.cpc.DataToSketchUDAF';
    create temporary function union2 as 'org.apache.datasketches.hive.cpc.UnionSketchUDF';
    create temporary function estimate as 'org.apache.datasketches.hive.cpc.GetEstimateUDF';

    use <your-db-nasme-here>;

    create temporary table sketch_input (id1 int, id2 int);
    insert into table sketch_input values
      (1, 2), (2, 4), (3, 6), (4, 8), (5, 10), (6, 12), (7, 14), (8, 16), (9, 18), (10, 20);

    create temporary table sketch_intermediate (sketch1 binary, sketch2 binary);

    insert into sketch_intermediate select data2sketch(id1), data2sketch(id2) from sketch_input;

    select
      estimate(sketch1),
      estimate(sketch2),
      estimate(union2(sketch1, sketch2))
    from sketch_intermediate;

    Output:
    10.007331400971685	10.007331400971685	15.017114660336853
