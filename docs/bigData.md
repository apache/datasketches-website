---
layout: simple_page
title: DataSketches are Designed for Big Data
subtitle: 
---

##Hadoop Sketch Adaptors

Providing easy-to-use adaptors for Hadoop is essential for making the capabilities of sketching useful for a broad range of users.

###Hadoop Pig
This first set of adaptors is for Hadoop Pig and provides the following classes:

* <b>DataToSketchUDF</b> provides the means to build a sketch from raw data. Returns a sketch as a byte array.
* <b>EstimateUDF</b> Returns a double valued estimate from an input sketch.
* <b>UnionUDF</b> Perform iterative Union Set Operations on a sketch. Returns a sketch as a byte array.
* <b>IntersectionUDF</b> Perform iterative Intersection Set Operations on a sketch. Returns a sketch as a byte array.
* <b>AnotBUDF</b> Perform the A-and-not-B Set Operation on two sketches. Returns a sketch as a byte array.
* <b>SketchToString</b> Returns a human readable summary of the state of an input sketch.
* <b>ErrorBounds</b> Returns a double valued estimate and double valued upper and lower bounds based on a 95% confidence levels given the state of an input sketch.


This is a code snippet of how to build a sketch using Pig:

<pre>
REGISTER /.../sketches-pig.jar
REGISTER /.../sketches-core.jar
DEFINE dataToSketch com.yahoo.sketches.pig.theta.DataToSketchUDF('4096');
DEFINE sketchToString com.yahoo.sketches.pig.theta.SketchToStringUDF();
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
<b>TODO</b>

##Compact Binary Storage
Sketches can be instantiated in two primary forms.  Both of these forms can be instantiated either in the Java heap or in direct, off-heap memory using the Memory package.

* <b>Hash-Table (HT) Form</b>
The HT form is similar to how the sketch is instantiated by Java in the Java heap.  Typical of HT it consumes more space depending on how full the HT is.  However updating the sketch is much faster in this form and is the default for all the Update Sketches.

* <b>Compact Form</b>
Once updating a sketch is completed the HT is no longer needed, so the sketch can be stored in a compact form.  The size of this compact form is a simple function of the number of retained hash values (64-bits each) and a small preamble that varies from 8 to 24 bytes depending on the internal state of the sketch.  An empty sketch is represented by only 8 bytes.  The upper limit of the size of the sketch varies by the type of sketch but is in the range of <i>8*k to 16*k</i>, where <i>k</i> is the accuracy-size bound of the sketch, which is discussed in the tutorials.  

##Memory Package

##Sensitive Data Protection

##Up-Front Sampling, <i>p</i>

