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
## Example of using ThetaSketch in Spark

The key idea with respect to performance here is to arrange a two-phase process. In the first phase all input is partitioned by Spark and sent to executors. One sketch is created per partition (or per dimensional combination in that partition) and updated with all the input without serializing the sketch until the end of the phase. In the second phase the sketches from the first phase are merged. Therefore serialization would happen only between the phases to transfer the results of the first phase to the executors performing the second phase. In the code examples below we convert UpdateSketches to CompactSketches during serialization, which results in transferring less data, and also serves as a proof that no serialization is done during the first phase since the deserialized sketches cannot be updated, but only merged.

Building one sketch using old Spark API:

    import org.apache.spark.SparkContext;
    import org.apache.spark.SparkConf;
    import org.apache.spark.api.java.JavaSparkContext;
    import org.apache.spark.api.java.JavaRDD;
    import org.apache.spark.api.java.function.Function2;

    import org.apache.datasketches.theta.PairwiseSetOperations;
    import org.apache.datasketches.theta.CompactSketch;
    import org.apache.datasketches.theta.UpdateSketch;

    public class Aggregate {

      public static void main(final String[] args) {
        final SparkConf conf = new SparkConf();
        final JavaSparkContext context = new JavaSparkContext(conf);
        final JavaRDD<String> lines = context.textFile("words.txt"); // one word per line
        final ThetaSketchJavaSerializable initialValue = new ThetaSketchJavaSerializable();
        final ThetaSketchJavaSerializable sketch = lines.aggregate(initialValue, new Add(), new Combine());
        System.out.println("Unique count: " + String.format("%,f", sketch.getEstimate()));
      }

      static class Add implements Function2<ThetaSketchJavaSerializable, String, ThetaSketchJavaSerializable> {
        public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch, final String value) throws Exception {
          sketch.update(value);
          return sketch;
        }
      }

      static class Combine implements Function2<ThetaSketchJavaSerializable, ThetaSketchJavaSerializable, ThetaSketchJavaSerializable> {
        static final ThetaSketchJavaSerializable emptySketchWrapped = new ThetaSketchJavaSerializable(UpdateSketch.builder().build().compact());

        public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch1, final ThetaSketchJavaSerializable sketch2) throws Exception {
          if (sketch1.getSketch() == null && sketch2.getSketch() == null) return emptySketchWrapped;
          if (sketch1.getSketch() == null) return sketch2;
          if (sketch2.getSketch() == null) return sketch1;
          final CompactSketch compactSketch1 = sketch1.getCompactSketch();
          final CompactSketch compactSketch2 = sketch2.getCompactSketch();
          return new ThetaSketchJavaSerializable(PairwiseSetOperations.union(compactSketch1, compactSketch2));
        }
      }
    }

Wrapper to make ThetaSketch serializable:

    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.io.IOException;
    import java.io.Serializable;

    import org.apache.datasketches.theta.Sketch;
    import org.apache.datasketches.theta.UpdateSketch;
    import org.apache.datasketches.theta.CompactSketch;
    import org.apache.datasketches.theta.Sketches;

    import org.apache.datasketches.memory.Memory;

    public class ThetaSketchJavaSerializable implements Serializable {

      private Sketch sketch;

      public ThetaSketchJavaSerializable() {
      }

      public ThetaSketchJavaSerializable(final Sketch sketch) {
        this.sketch = sketch;
      }

      public Sketch getSketch() {
        return sketch;
      }

      public CompactSketch getCompactSketch() {
        if (sketch == null) return null;
        if (sketch instanceof UpdateSketch) return ((UpdateSketch) sketch).compact();
        return (CompactSketch) sketch;
      }

      public void update(final String value) {
        if (sketch == null) sketch = UpdateSketch.builder().build();
        if (sketch instanceof UpdateSketch) {
          ((UpdateSketch) sketch).update(value);
        } else {
          throw new RuntimeException("update() on read-only sketch");
        }
      }

      public double getEstimate() {
        if (sketch == null) return 0.0;
        return  sketch.getEstimate();
      }

      private void writeObject(final ObjectOutputStream out) throws IOException {
        if (sketch == null) {
          out.writeInt(0);
          return;
        }
        final byte[] serializedSketchBytes = ((UpdateSketch) sketch).compact().toByteArray();
        out.writeInt(serializedSketchBytes.length);
        out.write(serializedSketchBytes);
      }

      private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int length = in.readInt();
        if (length == 0) return;
        final byte[] serializedSketchBytes = new byte[length];
        in.readFully(serializedSketchBytes);
        sketch = Sketches.wrapSketch(Memory.wrap(serializedSketchBytes));
      }

    }


Building one sketch using new Spark 2.x API and reading input from a Hive table:

    import org.apache.spark.sql.SparkSession;
    import org.apache.spark.sql.Dataset;
    import org.apache.spark.sql.Row;

    import org.apache.datasketches.theta.PairwiseSetOperations;
    import org.apache.datasketches.theta.Sketch;
    import org.apache.datasketches.theta.UpdateSketch;
    import org.apache.datasketches.theta.CompactSketch;

    import org.apache.spark.api.java.function.MapPartitionsFunction;
    import org.apache.spark.api.java.function.ReduceFunction;

    import org.apache.spark.sql.expressions.Aggregator;
    import org.apache.spark.sql.Encoder;
    import org.apache.spark.sql.Encoders;

    import scala.Tuple2;
    import java.util.Iterator;
    import java.util.Arrays;

    public class Spark2DatasetMapPartitionsReduceJavaSerialization {

      public static void main(String[] args) {
        final SparkSession spark = SparkSession
          .builder()
          .appName("Spark2Aggregate")
          .enableHiveSupport()
          .getOrCreate();
        final Dataset<Row> data = spark.sql("select userid from my_data where userid is not null");

        final Dataset<ThetaSketchJavaSerializable> mappedData = data.mapPartitions(new MapPartitionsFunction<Row, ThetaSketchJavaSerializable>() {
          @Override
          public Iterator<ThetaSketchJavaSerializable> call(Iterator<Row> it) {
            ThetaSketchJavaSerializable sketch = new ThetaSketchJavaSerializable();
            while (it.hasNext()) {
              sketch.update((String) it.next().get(0));
            }
            return Arrays.asList(sketch).iterator();
          }
        }, Encoders.javaSerialization(ThetaSketchJavaSerializable.class));

        final ThetaSketchJavaSerializable sketch = mappedData.reduce(new ReduceFunction<ThetaSketchJavaSerializable>() {
          @Override
          public ThetaSketchJavaSerializable call(ThetaSketchJavaSerializable sketch1, ThetaSketchJavaSerializable sketch2) throws Exception {
            if (sketch1.getSketch() == null && sketch2.getSketch() == null) return  new ThetaSketchJavaSerializable(UpdateSketch.builder().build().compact());
            if (sketch1.getSketch() == null) return sketch2;
            if (sketch2.getSketch() == null) return sketch1;
            final CompactSketch compactSketch1 = sketch1.getCompactSketch();
            final CompactSketch compactSketch2 = sketch2.getCompactSketch();
            return new ThetaSketchJavaSerializable(PairwiseSetOperations.union(compactSketch1, compactSketch2));
          }
        });

        System.out.println("Unique count: " + String.format("%,f", sketch.getEstimate()));
        spark.stop();
      }
    }


Building multiple sketches (one sketch per key or dimension):

    import org.apache.spark.SparkContext;
    import org.apache.spark.SparkConf;
    import org.apache.spark.api.java.JavaSparkContext;
    import org.apache.spark.api.java.JavaRDD;
    import org.apache.spark.api.java.JavaPairRDD;
    import org.apache.spark.api.java.function.Function2;
    import org.apache.spark.api.java.function.PairFlatMapFunction;

    import org.apache.datasketches.theta.PairwiseSetOperations;
    import org.apache.datasketches.theta.CompactSketch;
    import org.apache.datasketches.theta.UpdateSketch;

    import java.util.List;
    import java.util.ArrayList;
    import java.util.Map;
    import java.util.HashMap;
    import java.util.Iterator;
    import scala.Tuple2;

    public class MapPartitionsToPairReduceByKey {

      static final ThetaSketchJavaSerializable emptySketchWrapped = new ThetaSketchJavaSerializable(UpdateSketch.builder().build().compact());

      public static void main(final String[] args) {
        final SparkConf conf = new SparkConf();
        final JavaSparkContext context = new JavaSparkContext(conf);

        final JavaRDD<String> lines = context.textFile("agg-by-key-input.txt"); // format: key\tvalue

        final JavaPairRDD<String, ThetaSketchJavaSerializable> mappedSketches = lines.mapPartitionsToPair(
          new PairFlatMapFunction<Iterator<String>, String, ThetaSketchJavaSerializable>() {
            @Override
            public Iterator<Tuple2<String, ThetaSketchJavaSerializable>> call(final Iterator<String> input) {
              // This map might be too big if there are too many keys in the input data
              // One possible solution is to set a threshold on the number of entries
              // and flush the HashMap once the threshold is reached (not shown here).
              final Map<String, ThetaSketchJavaSerializable> map = new HashMap();
              while (input.hasNext()) {
                final String line = input.next();
                final String[] tokens = line.split("\t");
                ThetaSketchJavaSerializable sketch = map.get(tokens[0]);
                if (sketch == null) {
                  sketch = new ThetaSketchJavaSerializable();
                  map.put(tokens[0], sketch);
                }
                sketch.update(tokens[1]);
              }

              final List<Tuple2<String, ThetaSketchJavaSerializable>> list = new ArrayList();
              for (final Map.Entry<String, ThetaSketchJavaSerializable> entry: map.entrySet()) {
                list.add(new Tuple2(entry.getKey(), entry.getValue()));
              }
              return list.iterator();
            }
          }
        );

        final JavaPairRDD<String, ThetaSketchJavaSerializable> sketches = mappedSketches.reduceByKey(
          new Function2<ThetaSketchJavaSerializable, ThetaSketchJavaSerializable, ThetaSketchJavaSerializable>() {
            @Override
            public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch1, final ThetaSketchJavaSerializable sketch2) {
              if (sketch1.getSketch() == null && sketch2.getSketch() == null) return emptySketchWrapped;
              if (sketch1.getSketch() == null) return sketch2;
              if (sketch2.getSketch() == null) return sketch1;
              final CompactSketch compactSketch1 = sketch1.getCompactSketch();
              final CompactSketch compactSketch2 = sketch2.getCompactSketch();
              return new ThetaSketchJavaSerializable(PairwiseSetOperations.union(compactSketch1, compactSketch2));
            }
          }, 1 // number of output partitions
        );

        final Iterator<Tuple2<String, ThetaSketchJavaSerializable>> it = sketches.toLocalIterator();
        while (it.hasNext()) {
          final Tuple2<String, ThetaSketchJavaSerializable> pair = it.next();
          System.out.println("Pair: (" + pair._1 + ", " + pair._2.getEstimate() + ")");
        }
      }

    }

Building multiple sketches using SparkSession and reading input from a Hive table:

    import org.apache.spark.sql.SparkSession;
    import org.apache.spark.sql.Dataset;
    import org.apache.spark.sql.Row;
    import org.apache.spark.api.java.JavaPairRDD;
    import org.apache.spark.api.java.function.PairFlatMapFunction;
    import org.apache.spark.api.java.function.Function2;

    import org.apache.datasketches.theta.PairwiseSetOperations;
    import org.apache.datasketches.theta.UpdateSketch;
    import org.apache.datasketches.theta.CompactSketch;

    import scala.Tuple2;

    import java.util.List;
    import java.util.ArrayList;
    import java.util.Iterator;

    public class AggregateByKey2 {

      public static void main(String[] args) {
        SparkSession spark = SparkSession
          .builder()
          .appName("AggregateByKey2")
          .enableHiveSupport()
          .getOrCreate();

        Dataset<Row> data = spark.sql("select country, userid from my_data where userid is not null");
        final JavaPairRDD<String, String> pairs = data.javaRDD().mapPartitionsToPair(
          new PairFlatMapFunction<Iterator<Row>, String, String>() {
            @Override
            public Iterator<Tuple2<String, String>> call(final Iterator<Row> input) {
              final List<Tuple2<String, String>> list = new ArrayList();
              while (input.hasNext()) {
                final Row row = input.next();
                list.add(new Tuple2<String, String>((String) row.get(0), (String) row.get(1)));
              }
              return list.iterator();
            }
          }
        );

        final JavaPairRDD<String, ThetaSketchJavaSerializable> sketches = pairs.aggregateByKey(
          new ThetaSketchJavaSerializable(),
          1, // number of partitions
          new Add(),
          new Combine()
        );
        
        final Iterator<Tuple2<String, ThetaSketchJavaSerializable>> it = sketches.toLocalIterator();
        while (it.hasNext()) {
          final Tuple2<String, ThetaSketchJavaSerializable> pair = it.next();
          System.out.println("Pair: (" + pair._1 + ", " + pair._2.getEstimate() + ")");
        }
        
        spark.stop();
      }

      static class Add implements Function2<ThetaSketchJavaSerializable, String, ThetaSketchJavaSerializable> {
        @Override
        public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch, final String value) throws Exception {
          sketch.update(value);
          return sketch;
        }
      }

      static class Combine implements Function2<ThetaSketchJavaSerializable, ThetaSketchJavaSerializable, ThetaSketchJavaSerializable> {
        static final ThetaSketchJavaSerializable emptySketchWrapped = new ThetaSketchJavaSerializable(UpdateSketch.builder().build().compact());

        public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch1, final ThetaSketchJavaSerializable sketch2) throws Exception {
          if (sketch1.getSketch() == null && sketch2.getSketch() == null) return emptySketchWrapped;
          if (sketch1.getSketch() == null) return sketch2;
          if (sketch2.getSketch() == null) return sketch1;
          final CompactSketch compactSketch1 = sketch1.getCompactSketch();
          final CompactSketch compactSketch2 = sketch2.getCompactSketch();
          return new ThetaSketchJavaSerializable(PairwiseSetOperations.union(compactSketch1, compactSketch2));
        }
      }

    }
