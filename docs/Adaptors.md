---
layout: doc_page
---

##Adaptors for Large-Scale Computing

###Hadoop Pig
See <a href="http://datasketches.github.io/api/pig/snapshot/apidocs/index.html">API Snapshot for Pig</a>.

This is a code snippet of how to build a sketch using Pig:

<pre>
REGISTER /.../sketches-pig.jar
REGISTER /.../sketches-core.jar
DEFINE dataToSketch com.yahoo.sketches.pig.theta.DataToSketch('4096');
DEFINE sketchToString com.yahoo.sketches.pig.theta.SketchToString();
rawData = LOAD '$INPUT' USING ...pig.Projector('ID1', 'ID2');

groupData = GROUP rawData ALL;

sketchData = FOREACH groupData GENERATE
    FLATTEN (dataToSketch) (rawData.ID1) as id1_sketch:bytearray,
    FLATTEN (dataToSketch) (rawData.ID2) as id2_sketch:bytearray;

sketch_result = FOREACH sketchData GENERATE
    sketchToString (id1_sketch),
    sketchToString (id2_sketch);

DUMP sketch_result;
</pre>
The above script configures a simple Update Sketch UDF called 'DataToSketch' that is configured with a nominal entries of 4096 values. This size sketch will produce estimates of +/- 3.1% with a confidence of 95%.  The second object defined is a simple string converter for summarizing the internal state of the sketch for human consumption.

This script reads two different identifiers from the raw data and builds a sketch for each.  At the end it converts the sketches into human readable output and then dumps the results to the console.

The console output would look something like the following.  The actual result is a sketch in a compact byte array form that can be used for further downstream set operations if required.

<pre>
(### Sketch SUMMARY: 
   Estimate             : 674661.7753259285
   Upper Bound, 95% conf: 696010.8331386537
   Lower Bound, 95% conf: 653967.5669031701
   EstMode?             : true
   Theta (double)       : 0.0060711902612553175
   Theta (long)         : c6f0d5cf5866aa
   Maximum Entries      : 4096
   Current Count        : 4096
   Read-Only            : true
### END SKETCH SUMMARY,

### Sketch SUMMARY: 
   Estimate             : 222611.59909981795
   Upper Bound, 95% conf: 229611.43732246646
   Lower Bound, 95% conf: 215825.15501691535
   EstMode?             : true
   Theta (double)       : 0.018399760015035755
   Theta (long)         : 25aec5fc268ec9a
   Maximum Entries      : 4096
   Current Count        : 4096
   Read-Only            : true
### END SKETCH SUMMARY)
</pre>

###Hadoop Hive
See <a href="http://datasketches.github.io/api/hive/snapshot/apidocs/index.html">API Snapshot for Hive</a>.

###Druid
See <a href="https://github.com/druid-io/druid/pull/1897">Druid Integration of DataSketches</a>
