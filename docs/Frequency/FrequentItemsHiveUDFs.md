---
layout: doc_page
---

## Frequent Items Sketch Hive UDFs

### DoublesSketch example

    add jar sketches-core-0.7.0.jar;
    add jar sketches-hive-0.7.0.jar;

    create temporary function data2sketch as 'com.yahoo.sketches.hive.frequencies.DataToStringsSketchUDAF';
    create temporary function union as 'com.yahoo.sketches.hive.frequencies.UnionStringsSketchUDAF';
    create temporary function get_items as 'com.yahoo.sketches.hive.frequencies.GetFrequentItemsFromStringsSketchUDTF';

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
