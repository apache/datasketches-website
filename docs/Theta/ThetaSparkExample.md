---
layout: doc_page
---

## Example of using ThetaSketch in Spark

The key idea with respect to performance here is to arrange a two-phase process. In the first phase all input is partitioned by Spark and sent to executors. One sketch is created per partition (or per dimensional combination in that partition) and updated with all the input without serializing the sketch until the end of the phase. In the second phase the sketches from the first phase are merged. Therefore serialization is happening only between the phases to transfer the results of the first phase to the executors performing the second phase. In the code examples below we convert the UpdateSketches to CompactSketches during serialization, which results in transferring less data, and also serves as proof that no serialization is done during the first phase since the deserialized sketches cannot be updated, but only merged.

Building one sketch using old Spark API:

    import org.apache.spark.SparkContext;
    import org.apache.spark.SparkConf;
    import org.apache.spark.api.java.JavaSparkContext;
    import org.apache.spark.api.java.JavaRDD;
    import org.apache.spark.api.java.function.Function2;

    import com.yahoo.sketches.theta.PairwiseSetOperations;
    import com.yahoo.sketches.theta.CompactSketch;
    import com.yahoo.sketches.theta.UpdateSketch;

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

Wrapper to make TetaSketch serializable:

    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.io.IOException;
    import java.io.Serializable;

    import com.yahoo.sketches.theta.Sketch;
    import com.yahoo.sketches.theta.UpdateSketch;
    import com.yahoo.sketches.theta.CompactSketch;
    import com.yahoo.sketches.theta.Sketches;

    import com.yahoo.memory.NativeMemory;

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
        sketch = Sketches.wrapSketch(new NativeMemory(serializedSketchBytes));
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

    import com.yahoo.sketches.theta.PairwiseSetOperations;
    import com.yahoo.sketches.theta.CompactSketch;
    import com.yahoo.sketches.theta.UpdateSketch;

    import java.util.List;
    import java.util.ArrayList;
    import java.util.Iterator;
    import scala.Tuple2;

    public class AggregateByKey {

      public static void main(final String[] args) {
        final SparkConf conf = new SparkConf();
        final JavaSparkContext context = new JavaSparkContext(conf);

        final JavaRDD<String> lines = context.textFile("agg-by-key-input.txt"); // format: key\tvalue
        final JavaPairRDD<String, String> pairs = lines.mapPartitionsToPair(new PairFlatMapFunction<Iterator<String>, String, String>() {
          public Iterator<Tuple2<String, String>> call(final Iterator<String> input) {
            final List<Tuple2<String, String>> list = new ArrayList();
            while (input.hasNext()) {
              final String line = input.next();
              final String[] tokens = line.split("\t");
              list.add(new Tuple2(tokens[0], tokens[1]));
            }
            return list.iterator();
          }
        });

        final JavaPairRDD<String, ThetaSketchJavaSerializable> sketches = pairs.aggregateByKey(
          new ThetaSketchJavaSerializable(),
          new SeqFunc(),
          new CombFunc()
        );
        final Iterator<Tuple2<String, ThetaSketchJavaSerializable>> it = sketches.toLocalIterator();
        while (it.hasNext()) {
          final Tuple2<String, ThetaSketchJavaSerializable> pair = it.next();
          System.out.println("Pair: (" + pair._1 + ", " + pair._2.getEstimate() + ")");
        }
      }

      static class SeqFunc implements Function2<ThetaSketchJavaSerializable, String, ThetaSketchJavaSerializable> {
        public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch, final String value) {
          sketch.update(value);
          return sketch;
        }
      }

      static class CombFunc implements Function2<ThetaSketchJavaSerializable, ThetaSketchJavaSerializable, ThetaSketchJavaSerializable> {
        static final ThetaSketchJavaSerializable emptySketchWrapped = new ThetaSketchJavaSerializable(UpdateSketch.builder().build().compact());

        public ThetaSketchJavaSerializable call(final ThetaSketchJavaSerializable sketch1, final ThetaSketchJavaSerializable sketch2) {
          if (sketch1.getSketch() == null && sketch2.getSketch() == null) return emptySketchWrapped;
          if (sketch1.getSketch() == null) return sketch2;
          if (sketch2.getSketch() == null) return sketch1;
          final CompactSketch compactSketch1 = sketch1.getCompactSketch();
          final CompactSketch compactSketch2 = sketch2.getCompactSketch();
          return new ThetaSketchJavaSerializable(PairwiseSetOperations.union(compactSketch1, compactSketch2));
        }
      }

    }

Building one sketch using new Spark 2.x API and reading input from a Hive table:

    import org.apache.spark.sql.SparkSession;
    import org.apache.spark.sql.Dataset;
    import org.apache.spark.sql.Row;

    import com.yahoo.sketches.theta.PairwiseSetOperations;
    import com.yahoo.sketches.theta.Sketch;
    import com.yahoo.sketches.theta.UpdateSketch;
    import com.yahoo.sketches.theta.CompactSketch;

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
