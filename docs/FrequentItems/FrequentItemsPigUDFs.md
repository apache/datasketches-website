---
layout: doc_page
---

## Frequent Items Sketch Pig UDFs

### Instructions

* get jars
* save the following script as frequent_items.pig
* adjust jar versions and paths if necessary
* save the below data into a file called data.txt
* copy data to hdfs: "hdfs dfs -copyFromLocal data.txt"
* run pig script: "pig frequent_items.pig"

### frequent_items.pig script

    register sketches-core-0.5.2.jar;
    register sketches-pig-0.5.2.jar;

    DEFINE dataToSketch com.yahoo.sketches.pig.frequencies.DataToFrequentStringsSketch('8');
    DEFINE mergeSketch com.yahoo.sketches.pig.frequencies.MergeFrequentStringsSketch('8');
    DEFINE getResult com.yahoo.sketches.pig.frequencies.FrequentStringsSketchToEstimates();

    a = load 'data.txt' as (item:chararray, category);
    b = group a by category;
    c = foreach b generate flatten(group) as (category), flatten(dataToSketch(a.item)) as (sketch);
    -- Sketches can be stored at this point in binary format to be used later:
    -- store c into 'intermediate/$date' using BinStorage();
    -- The next two lines print the results in human readable form for the purpose of this example
    d = foreach c generate category, getResult(sketch);
    dump d;

    -- This can be a separate query.
    -- For example, the first part can produce a daily intermediate feed and store it.
    -- This part can load several instances of this daily intermediate feed and merge them
    -- c = load 'intermediate/$date1,intermediate/$date2' using BinStorage() as (category, sketch); 
    e = group c all;
    f = foreach e generate flatten(mergeSketch(c.sketch)) as (sketch);
    g = foreach f generate getResult(sketch);
    describe g;
    dump g;

The example input data has 2 fields: item and category. In the first part of the query the data is grouped by category with one FrequentItemsSketch&lt;String&gt; per category. In the second part of the query this intermediate result is merged across categories to produce one sketch. This way the usage of all 3 UDFs is demonstrated: DataToFrequentStringsSketch, MergeFrequentStringsSketch and FrequentStringsSketchToEstimates.

Results:

From 'dump d' (one sketch per category):

    (c1,{(a,7,7,7),(d,2,2,2),(b,1,1,1)})
    (c2,{(a,5,5,5),(d,2,2,2),(e,1,1,1),(c,1,1,1)})

From 'dump g' (merged across categories):

    ({(a,12,12,12),(d,4,4,4),(b,1,1,1),(e,1,1,1),(c,1,1,1)})

From 'describe g':

    g: {bag_of_item_tuples: {item_tuple: (item: chararray,estimate: long,upper_bound: long,lower_bound: long)}}

In this example the results are exact due to small input.

### [data.txt](data.txt) (tab separated)
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
