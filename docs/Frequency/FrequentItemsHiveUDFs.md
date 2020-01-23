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
## Frequent Items Sketch Hive UDFs

    add jar sketches-core-0.7.0.jar;
    add jar sketches-hive-0.7.0.jar;

    create temporary function data2sketch as 'org.apache.datasketches.hive.frequencies.DataToStringsSketchUDAF';
    create temporary function union as 'org.apache.datasketches.hive.frequencies.UnionStringsSketchUDAF';
    create temporary function get_items as 'org.apache.datasketches.hive.frequencies.GetFrequentItemsFromStringsSketchUDTF';

    use <your-db-name-here>;

    create temporary table frequent_items_input (value string, category char(2));
    -- works with char or varchar types as well
    --create temporary table frequent_items_input (value char(2), category char(2));
    --create temporary table frequent_items_input (value varchar(2), category char(2));
    insert into table frequent_items_input values
      ('a', 'c1'), ('a', 'c1'), ('a', 'c1'), ('a', 'c2'), ('a', 'c1'), ('b', 'c1'), ('c', 'c1'), ('d', 'c1'), ('e', 'c2'), ('a', 'c1'),
      ('a', 'c2'), ('a', 'c2'), ('a', 'c2'), ('d', 'c1'), ('d', 'c2'), ('a', 'c1'), ('a', 'c2'), ('a', 'c1'), ('d', 'c2');

    create temporary table frequent_items_intermediate (category char(2), sketch binary);
    insert into frequent_items_intermediate select category, data2sketch(value, 16) from frequent_items_input group by category;

    select get_items(sketch, 'NO_FALSE_POSITIVES') from frequent_items_intermediate where category='c1';

    Output (item, frequency estimate, lower bound, upper bound):
    a	7	7	7
    d	2	2	2
    c	1	1	1
    b	1	1	1

    select get_items(sketch, 'NO_FALSE_POSITIVES') from frequent_items_intermediate where category='c2';

    Output:
    a	5	5	5
    d	2	2	2
    e	1	1	1

    -- NO_FALSE_POSITIVES is the default and was specified above just for completeness
    select get_items(union(sketch)) from frequent_items_intermediate;

    Output:
    a	12	12	12
    d	4	4	4
    c	1	1	1
    b	1	1	1
    e	1	1	1
