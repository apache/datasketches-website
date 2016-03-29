---
layout: doc_page
---

## Hadoop Hive UDFs

Depends on sketches-core.

To use Hive UDFs, you should do the following:

1. Register the JAR file with Hive: 
  * hive> add jar sketches-hive-0.1.0-incDeps.jar
2. Register UDF functions with Hive:
  * hive> create temporary function estimate as 'com.yahoo.sketches.hive.theta.EstimateSketchUDF';
  * hive> create temporary function dataToSketch as 'com.yahoo.sketches.hive.theta.DataToSketchUDAF';
3. Run a query: 
  * hive> select estimate(dataToSketch(myCol, 16384, 1.0)) from myTable where color = blue;
  
See <a href="{{site.hive_readme}}">hive README.md</a>.

See <a href="{{site.hive_api_snapshot}}">API Snapshot for Hive</a>.

