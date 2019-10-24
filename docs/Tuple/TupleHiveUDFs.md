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
## Tuple Sketch Hive UDFs

    add jar sketches-hive-0.10.5-with-shaded-core.jar;

    create temporary function data2sketch as 'org.apache.datasketches.hive.tuple.DataToArrayOfDoublesSketchUDAF';
    create temporary function unionSketches as 'org.apache.datasketches.hive.tuple.UnionArrayOfDoublesSketchUDAF';
    create temporary function estimate as 'org.apache.datasketches.hive.tuple.ArrayOfDoublesSketchToEstimatesUDF';

    use <your-db-name-here>;

    create temporary table tuple_input (id int, category char(1), parameter double);
    insert into table tuple_input values
      (1, 'a', 1), (2, 'a', 1), (3, 'a', 1), (4, 'a', 1), (5, 'a', 1), (6, 'a', 1), (7, 'a', 1), (8, 'a', 1), (9, 'a', 1), (10, 'a', 1),
      (6, 'b', 1), (7, 'b', 1), (8, 'b', 1), (9, 'b', 1), (10, 'b', 1), (11, 'b', 1), (12, 'b', 1), (13, 'b', 1), (14, 'b', 1), (15, 'b', 1);

    create temporary table sketch_intermediate (category char(1), sketch binary);
    insert into sketch_intermediate select category, data2sketch(id, parameter) from tuple_input group by category;

    -- estimates per category
    select category, estimate(sketch) from sketch_intermediate;

    Output:
    a	[10.0,10.0]
    b	[10.0,10.0]

    -- union across categories and get estimates
    select estimate(unionSketches(sketch)) from sketch_intermediate;

    Output:
    [15.0,20.0]
