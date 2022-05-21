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
# Sketch Features Matrix

Use the following table to compare the capabilities of the different sketch families.

All sketches have *a posteriori* error bounds methods.

<div style="text-align: center">
<table>
<tr style="font-weight:bold"><td colspan="2">Sketch</td><td colspan="3">Languages</td><td colspan="4">Set Operations</td><td colspan="5">System Integrations</td><td colspan="3">Misc.</td></tr>

<tr style="font-weight:bold"><td>Type</td><td>Class Name</td><td>Java</td><td>C++</td><td>Python<sup>7</sup></td><td>Union</td><td>Inter-section</td><td>Difference</td><td>Jaccard</td><td>Hive</td><td>Pig</td><td>Druid<sup>1</sup></td><td>Spark<sup>2</sup></td><td>PostgreSQL (C++)</td><td>Con-current</td><td>Compact</td><td>Off Java Heap</td></tr>

<tr style="font-weight:bold"><td colspan="19">Major Sketches</td></tr>
<tr><td>Cardinality/CPC</td>   <td>CpcSketch</td>                    <td>Y</td> <td>Y</td> <td>Y</td>            <td>Y</td> <td></td>  <td></td>  <td></td>              <td>Y</td> <td>Y</td> <td></td>  <td></td>  <td>Y</td>             <td></td>  <td>Y</td> <td></td></tr>
<tr><td>Cardinality/HLL</td>   <td>HllSketch</td>                    <td>Y</td> <td>Y</td> <td>Y</td>            <td>Y</td> <td></td>  <td></td>  <td></td>              <td>Y</td> <td>Y</td> <td>Y</td> <td></td>  <td>Y</td>             <td></td>  <td></td>  <td>Y</td></tr>
<tr><td>Cardinality/Theta</td> <td>Sketch</td>                       <td>Y</td> <td>Y</td> <td>Y</td>            <td>Y</td> <td>Y</td> <td>Y</td> <td>Y<sup>4</sup></td> <td>Y</td> <td>Y</td> <td>Y</td> <td>Y</td> <td>Y</td>             <td>Y</td> <td>Y</td> <td>Y</td></tr>
<tr><td>Cardinality/Tuple</td> <td>Sketch&lt;S&gt;</td>              <td>Y</td> <td>Y</td> <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td>Y</td> <td>Y</td></tr>
<tr><td>Quantiles/Cormode</td> <td>DoublesSketch</td>                <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td></td>  <td></td>  <td></td>              <td>Y</td> <td>Y</td> <td>Y</td> <td></td>  <td></td>              <td></td>  <td>Y</td> <td>Y</td></tr>
<tr><td>Quantiles/Cormode</td> <td>ItemsSketch&lt;T&gt;</td>         <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td></td>  <td></td>  <td></td>              <td>Y</td> <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Quantiles/KLL</td>     <td>KllDoublesSketch</td>             <td>Y</td> <td>Y</td> <td>Y<sup>6</sup></td><td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td>Y</td> <td></td>  <td>Y</td>             <td></td>  <td>Y</td> <td>Y</td></tr>
<tr><td>Quantiles/KLL</td>     <td>KllFloatsSketch</td>              <td>Y</td> <td>Y</td> <td>Y<sup>6</sup></td><td>Y</td> <td></td>  <td></td>  <td></td>              <td>Y</td> <td>Y</td> <td></td>  <td></td>  <td>Y</td>             <td></td>  <td>Y</td> <td>Y</td></tr>
<tr><td>Quantiles/KLL</td>     <td>KLLSketch&lt;T&gt;</td>           <td></td>  <td>Y</td> <td></td>             <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Quantiles/REQ</td>     <td>FloatsSketch</td>                 <td>Y</td> <td>Y</td> <td>Y<sup>6</sup></td><td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Frequencies</td>       <td>LongsSketch</td>                  <td>Y</td> <td>Y</td> <td>Y</td>            <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Frequencies</td>       <td>ItemsSketch&lt;T&gt;</td>         <td>Y</td> <td>Y</td> <td>Y</td>            <td>Y</td> <td></td>  <td></td>  <td></td>              <td>Y</td> <td>Y</td> <td></td>  <td></td>  <td>Y<sup>5</sup></td> <td></td>  <td></td>  <td></td></tr>
<tr><td>Sampling/Reservior</td><td>ReservoirLongsSketch</td>         <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Sampling/Reservoir</td><td>ReserviorItemsSketch&lt;T&gt;</td><td>Y</td> <td></td>  <td></td>             <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Sampling/VarOpt</td>   <td>VarOptItemsSketch&lt;T&gt;</td>   <td>Y</td> <td>Y</td> <td>Y</td>            <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td>Y</td> <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>

<tr style="font-weight:bold"><td colspan="19">Specialty Sketches</td></tr>

<tr><td>Cardinality/FM85</td>  <td>UniqueCountMap</td>               <td>Y</td> <td></td>  <td></td>             <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Cardinality/Tuple</td> <td>FdtSketch</td>                    <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Cardinality/Tuple</td> <td>ArrayOfDoublesSketch</td>         <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td>Y</td> <td>Y</td> <td>Y</td> <td></td>  <td></td>              <td></td>  <td>Y</td> <td>Y</td></tr>
<tr><td>Cardinality/Tuple</td> <td>DoubleSketch</td>                 <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Cardinality/Tuple</td> <td>IntegerSketch</td>                <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Cardinality/Tuple</td> <td>ArrayOfStringsSketch</td>         <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
<tr><td>Cardinality/Tuple</td> <td>EngagementTest<sup>3</sup></td>   <td>Y</td> <td></td>  <td></td>             <td>Y</td> <td>Y</td> <td>Y</td> <td></td>              <td></td>  <td></td>  <td></td>  <td></td>  <td></td>              <td></td>  <td></td>  <td></td></tr>
</table>
</div>


<sup>1</sup> Integrated into Druid.<br>
<sup>2</sup> [Spark Example Code]({{site.docs_dir}}/Theta/ThetaSparkExample.html) on website. Theta Sketch is the only one we have tried in Spark, it doesn't mean other sketches cannot be used.<br>
<sup>3</sup> Tuple Sketch: Example Code in test/.../tuple/aninteger.<br>
<sup>4</sup> Theta Sketch: C++/Python has no implementaion of the Jaccard, yet.<br>
<sup>5</sup> Frequent Items Sketch: PostgreSQL implemented for Strings only.<br>
<sup>6</sup> KLL & REQ Sketch: Python implemented for both just floats and ints.<br>
<sup>7</sup> See [Python Install Instructions](https://github.com/apache/datasketches-cpp/tree/master/python)<br>


## Definitions

### Type

See [Research/References]({{site.docs_dir}}/Community/Research.html) for references in [...]

* **Cardinality/CPC** Implementation and extension of [LAN17].
* **Cardinality/HLL** Derivation and extension of [FFGM07]  
* **Cardinality/Theta** Derivation and extension of [DLRT16].
* **Cardinality/Tuple** An Extension of the Theta family that adds attributes to each hash-key.
* **Quantiles/Cormode** Derivation and extension of [AC+13]
* **Quantiles/KLL** Derivation and extension of [KLL16].
* **Frequencies** Derivation and extension of [ABL+17].
* **Sampling/Reservior** Derivation and extension of [K98], Vol 2, Section 3.4.2, Algorithm R.  
* **Sampling/VarOpt** Derivation and extension of [CDKLT09].



