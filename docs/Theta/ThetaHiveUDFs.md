---
layout: doc_page
---

## Hadoop Hive UDFs

Depends on sketches-core.

To use Hive UDFs, you should do the following:

1. Register the JAR file with Hive:
  * hive> add jar sketches-hive-0.3.0-incDeps.jar
2. Register UDF functions with Hive:
  * hive> create temporary function estimate as &#39;com.yahoo.sketches.hive.theta.EstimateSketchUDF&#39;;
  * hive> create temporary function data_sketch as &#39;com.yahoo.sketches.hive.theta.DataToSketchUDAF&#39;;
  * hive> create temporary function merge_sketch as &#39;com.yahoo.sketches.hive.theta.MergeSketchUDAF&#39;;
3. Run a query: 
  * hive> select estimate(data_sketch(myCol, 16384, 1.0)) from myTable where color = blue;
  * hive> select estimate(merge_sketch(unbase64(ActiveUsersSketchByHour),4096)) from myTable;

Note: ActiveUsersSketchByHour is a Base64(sketch.toByteArray()).
  
See <a href="{{site.hive_readme}}">hive README.md</a>.

See <a href="{{site.hive_api_snapshot}}">API Snapshot for Hive</a>.

