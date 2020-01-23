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
## Quantiles Sketch Hive UDFs

### DoublesSketch example

    add jar sketches-hive-0.10.5-with-shaded-core.jar;
    
    create temporary function data2sketch as 'org.apache.datasketches.hive.quantiles.DataToDoublesSketchUDAF';
    create temporary function unionSketches as 'org.apache.datasketches.hive.quantiles.UnionDoublesSketchUDAF';
    create temporary function getQuantile as 'org.apache.datasketches.hive.quantiles.GetQuantileFromDoublesSketchUDF';
    create temporary function getQuantiles as 'org.apache.datasketches.hive.quantiles.GetQuantilesFromDoublesSketchUDF';

    use <your-db-name-here>;
    
    create temporary table quantiles_input (value double, category char(1));
    insert into table quantiles_input values
      (1, 'a'), (2, 'a'), (3, 'a'), (4, 'a'), (5, 'a'), (6, 'a'), (7, 'a'), (8, 'a'), (9, 'a'), (10, 'a'),
      (11, 'b'), (12, 'b'), (13, 'b'), (14, 'b'), (15, 'b'), (16, 'b'), (17, 'b'), (18, 'b'), (19, 'b'), (20, 'b');
    
    create temporary table quantiles_intermediate (category char(1), sketch binary);
    insert into quantiles_intermediate select category, data2sketch(value) from quantiles_input group by category;
    
    -- get median value per category
    select category, getQuantile(sketch, 0.5) from quantiles_intermediate;

    Output:
    a	6.0
    b	16.0

    -- union across categories and get median
    select getQuantile(unionSketches(sketch), 0.5) from quantiles_intermediate;

    Output:
    11.0

    -- if you want several quantiles it is more efficient to use this function
    -- quantile of 0.0 is min value, 0.5 is median, and 1.0 is max value
    select getQuantiles(unionSketches(sketch), 0.0, 0.5, 1.0) from quantiles_intermediate;

    Output:
    [1.0,11.0,20.0]

    -- alternative way to get quantiles by specifying a number of evenly spaced fractions
    -- this is equivalent to giving fractions of 0.0, 0.5 and 1.0
    select getQuantiles(unionSketches(sketch), 3) from quantiles_intermediate;

    Output:
    [1.0,11.0,20.0]
