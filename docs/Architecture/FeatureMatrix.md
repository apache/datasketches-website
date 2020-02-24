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
# Sketch Feature Matrix

Use the following table to compare the capabilities of the different sketch families.

<div>
<table>
<tr style="font-weight:bold"><td colspan="2"></td><td colspan="3">Languages</td><td colspan="4">Set Operations</td><td colspan="4">System Integrations</td><td colspan="5">Misc.</td></tr>

<tr style="font-weight:bold"><td>Type</td><td>Sketch</td><td>Java</td><td>C++</td><td>Python</td><td>Union</td><td>Inter-section</td><td>Difference</td><td>Jaccard</td><td>Hive</td><td>Pig</td><td>Druid<sup>1</sup></td><td>Spark<sup>2</sup></td><td>Con-current</td><td>Compact</td><td>Java Generics</td><td>Off Java Heap</td><td>Error Bounds</td></tr>

<tr style="font-weight:bold"><td colspan="18">Major Sketches</td></tr>
<tr><td>Cardinality/FM85</td><td>CpcSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/FM85</td><td>HllSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Cardinality/Theta</td><td>Sketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>Sketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td>Y</td></tr>
<tr><td>Quantiles/Cormode</td><td>DoublesSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Quantiles/Cormode</td><td>ItemsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>
<tr><td>Quantiles/KLL</td><td>FloatsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Frequencies</td><td>LongsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Frequencies</td><td>ItemsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>
<tr><td>Sampling</td><td>ReservoirLongsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Sampling</td><td>ReserviorItemsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>
<tr><td>Sampling</td><td>VarOptItemsSketch</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td>Y</td></tr>

<tr style="font-weight:bold"><td colspan="18">Specialty Sketches</td></tr>

<tr><td>Cardinality/FM85</td><td>UniqueCountMap</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>FdtSketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>ArrayOfDoublesSketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td>Y</td><td></td><td>Y</td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>DoubleSketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>IntegerSketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>ArrayOfStringsSketch</td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
<tr><td>Cardinality/Tuple</td><td>EngagementTest<sup>3</sup></td><td>Y</td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
</table>
</div>

<sup>1</sup> Integrated into Druid<br>
<sup>2</sup> Example Code on website<br>
<sup>3</sup> Example Code in test/.../tuple/aninteger


## Definitions

### Type (a.k.a. Sketch Family)

See [Research]({{site.docs_dir}}/Community/Research.html) for references in [...]

* **Cardinality/FM85** Derivations of [FM85]. They include the popular HyperLogLog (HLL) Sketch as well as the Compressed Probabilistic Counting (CPC) Sketch, which has a completely different theoretical derivation and is superior to the HLL sketch in terms of accuracy per byte of storage. 
* **Cardinality/Theata** Derivations of [BJKST02].
* **Cardinality/Tuple** An Extension of the Theta family that adds attributes to each hash-key.
* **Quantiles/Cormode** Based on [AC+13]
* **Quantiles/KLL** Based on [KLL16].
* **Frequencies** Based on [ABL+17].
* **Sampling** Two families, The simple reservoir sketch is based on Knuth, algorithm R.  The VarOpt sketch is based on [CDKLT09].

