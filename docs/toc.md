---
layout: toc_page
---
<link rel="stylesheet" href="/css/toc.css">

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_overview">Overview</a></h2>
<div class="collapse" id="collapse_overview">

<!--# Overview -->
* [The Challenge]({{site.docs_dir}}/TheChallenge.html)
* [The Major Sketch Families]({{site.docs_dir}}/MajorSketchFamilies.html)
* [Sketch Origins]({{site.docs_dir}}/SketchOrigins.html)
* [Sketch Elements]({{site.docs_dir}}/SketchElements.html)
* [Key Features]({{site.docs_dir}}/KeyFeatures.html)
* [Large Scale Computing]({{site.docs_dir}}/LargeScale.html)
* [Architecture]({{site.docs_dir}}/Architecture.html)
* [Overview Slide Deck]({{site.docs_pdf_dir}}/DataSketches_deck_13Oct2016.pdf)
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_sections">Section Links</a></h2>
<div class="collapse" id="collapse_sections">

<!--# Section Links-->
* [Frequent Items Sketches](#frequent-items-sketches)
* [HLL Sketches](#hll-sketches)
* [Memory Package](#memory-package)
* [Quantiles Sketches](#quantiles-sketches)
* [Sampling Sketches](#sampling-sketches)
* [Theta Sketches](#theta-sketches)
* [Tuple Sketches](#tuple-sketches)
* [Other Information](#other-information)
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_frequent">Frequent Items Sketches</a></h2>
<div class="collapse" id="collapse_frequent">

<!--# Frequent Items Sketches-->
* [Frequent Items Overview]({{site.docs_dir}}/FrequentItems/FrequentItemsOverview.html)
* [Frequent Items Java Example]({{site.docs_dir}}/FrequentItems/FrequentItemsJavaExample.html)
* [Frequent Items Pig UDFs]({{site.docs_dir}}/FrequentItems/FrequentItemsPigUDFs.html)
* [Frequent Items Hive UDFs]({{site.docs_dir}}/FrequentItems/FrequentItemsHiveUDFs.html)
* [Frequent Items Error Table]({{site.docs_dir}}/FrequentItems/FrequentItemsErrorTable.html)
* [Frequent Items References]({{site.docs_dir}}/FrequentItems/FrequentItemsReferences.html)
</div>


<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_hll">HLL Sketches</a></h2>
<div class="collapse" id="collapse_hll">

<!--# HLL Sketches-->
* [HLL Sketch]({{site.docs_dir}}/HLL/HLL.html)
* [HLL Map Sketch]({{site.docs_dir}}/HLL/HllMap.html)
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_memory">Memory</a></h2>
<div class="collapse" id="collapse_memory">

<!--# Memory Package-->
* [Memory Package]({{site.docs_dir}}/Memory/MemoryPackage.html)
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_quantiles">Quantiles Sketches</a></h2>
<div class="collapse" id="collapse_quantiles">

<!--# Quantiles Sketches-->
* [Quantiles Overview]({{site.docs_dir}}/Quantiles/QuantilesOverview.html)
* [Quantiles Accuracy and Size]({{site.docs_dir}}/Quantiles/QuantilesAccuracy.html)
* [Quantiles Sketch Java Example]({{site.docs_dir}}/Quantiles/QuantilesJavaExample.html)
* [Quantiles Sketch Pig UDFs]({{site.docs_dir}}/Quantiles/QuantilesPigUDFs.html)
* [Quantiles Sketch Hive UDFs]({{site.docs_dir}}/Quantiles/QuantilesHiveUDFs.html)

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_quantilesTheory">Quantiles Sketch Theory</a></h3>
<div class="collapse" id="collapse_quantilesTheory">

<!--### Quantiles Sketch Theory-->
* [Optimal Quantile Approximation in Streams]({{site.docs_pdf_dir}}/Quantiles_KLL.pdf)
* [Quantiles References]({{site.docs_dir}}/Quantiles/QuantilesReferences.html)
</div>
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_sampline">Sampling Sketches</a></h2>
<div class="collapse" id="collapse_sampling">

<!--# Sampling Sketches-->
* [Reservoir Sampling]({{site.docs_dir}}/Sampling/ReservoirSampling.html)
* [Reservoir Sampling Performance]({{site.docs_dir}}/Sampling/ReservoirSamplingPerformance.html)
* [Reservoir Sampling Java Example]({{site.docs_dir}}/Sampling/ReservoirSamplingJava.html)
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_theta">Theta Sketches</a></h2>
<div class="collapse" id="collapse_theta">

<!--# Theta Sketches-->
* [Theta Sketch Framework]({{site.docs_dir}}/Theta/ThetaSketchFramework.html)
* [Theta Sketch Java Example]({{site.docs_dir}}/Theta/ThetaJavaExample.html)
* [Theta Sketch Spark Example]({{site.docs_dir}}/Theta/ThetaSparkExample.html)

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_kmv">KMV Tutorial</a></h3>
<div class="collapse" id="collapse_kmv">

<!--### KMV Tutorial-->
* [The Inverse Estimate]({{site.docs_dir}}/Theta/InverseEstimate.html)
* [Empty Sketch]({{site.docs_dir}}/Theta/KMVempty.html)
* [First Estimator]({{site.docs_dir}}/Theta/KMVfirstEst.html)
* [Better Estimator]({{site.docs_dir}}/Theta/KMVbetterEst.html)
* [Rejection Rules]({{site.docs_dir}}/Theta/KMVrejection.html)
* [Update V(kth) Rule]({{site.docs_dir}}/Theta/KMVupdateVkth.html)
</div>

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_set">Set Operations</a></h3>
<div class="collapse" id="collapse_set">

<!--### Set Operations-->
* [Set Operations]({{site.docs_dir}}/Theta/ThetaSketchSetOps.html)
</div>

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_accuracy">Accuracy</a></h3>
<div class="collapse" id="collapse_accuracy">

<!--### Accuracy-->
* [Basic Accuracy]({{site.docs_dir}}/Theta/ThetaAccuracy.html)
* [Accuracy Plots]({{site.docs_dir}}/Theta/ThetaAccuracyPlots.html)
* [Relative Error Table]({{site.docs_dir}}/Theta/ThetaErrorTable.html)
* [SetOp Accuracy]({{site.docs_dir}}/Theta/ThetaSketchSetOpsAccuracy.html)
* [Unions With Different k]({{site.docs_dir}}/Theta/AccuracyOfDifferentKUnions.html)
</div>

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_size">Size</a></h3>
<div class="collapse" id="collapse_size">

<!--### Size-->
* [Theta Sketch Size]({{site.docs_dir}}/Theta/ThetaSize.html)
</div>

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_speed">Speed</a></h3>
<div class="collapse" id="collapse_speed">

<!--### Speed-->
* [Update Speed]({{site.docs_dir}}/Theta/ThetaUpdateSpeed.html)
* [Merge Speed]({{site.docs_dir}}/Theta/ThetaMergeSpeed.html)
</div>

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_scale">Large Scale Computing</a></h3>
<div class="collapse" id="collapse_scale">

<!--### Large Scale Computing-->
* [Theta Sketch Pig UDFs]({{site.docs_dir}}/Theta/ThetaPigUDFs.html)
* [Theta Sketch Hive UDFs]({{site.docs_dir}}/Theta/ThetaHiveUDFs.html)
* [Integration with Druid]({{site.docs_dir}}/DruidIntegration.html)
* [Memory Package]({{site.docs_dir}}/Memory/MemoryPackage.html)
* [<i>p</i>-Sampling]({{site.docs_dir}}/Theta/ThetaPSampling.html)
</div>

<h3 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_thetaTheory">Theta Sketch Theory</a></h3>
<div class="collapse" id="collapse_thetaTheory">

<!--### Theta Sketch Theory-->
* [Theta Sketch Framework (PDF)]({{site.docs_pdf_dir}}/ThetaSketchFramework.pdf)
* [Sketch Equations (PDF)]({{site.docs_pdf_dir}}/SketchEquations.pdf)
* [DataSketches (PDF)]({{site.docs_pdf_dir}}/DataSketches.pdf)
* [Confidence Intervals Notes]({{site.docs_dir}}/Theta/ThetaConfidenceIntervals.html)
* [Merging Algorithm Notes]({{site.docs_dir}}/Theta/ThetaMergingAlgorithm.html)
* [Theta References]({{site.docs_dir}}/Theta/ThetaReferences.html)
</div>
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_tuple">Tuple Sketches</a></h2>
<div class="collapse" id="collapse_tuple">

<!--# Tuple Sketches-->
* [Tuple Sketch Overview]({{site.docs_dir}}/Tuple/TupleOverview.html)
* [Tuple Sketch Java Example]({{site.docs_dir}}/Tuple/TupleJavaExample.html)
* [Tuple Sketch Pig UDFs]({{site.docs_dir}}/Tuple/TuplePigUDFs.html)
* [Tuple Sketch Hive UDFs]({{site.docs_dir}}/Tuple/TupleHiveUDFs.html)
</div>

<h2 id="overview"><a data-toggle="collapse" class="menu collapsed" href="#collapse_other">Other Information</a></h2>
<div class="collapse" id="collapse_other">

<!--# Other Information-->
* [Creating Command Line Executables]({{site.docs_dir}}/CommandLine/CommandLine.html)
* [Who Uses]({{site.docs_dir}}/WhoUses.html)
* [License](/LICENSE.html)
<!-- * [Endorsements](endorsements.html) -->
</div>
