---
layout: doc_page
---

# Quantiles Sketch Java Example

    // simplified file operations and no error handling for clarity

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.util.Arrays;
    import java.util.Random;

    import com.yahoo.memory.Memory;
    import com.yahoo.memory.NativeMemory;

    import com.yahoo.sketches.quantiles.DoublesSketch;
    import com.yahoo.sketches.quantiles.DoublesUnion;

    // this section generates two sketches from random data and serializes them into files
    {
      Random rand = new Random();

      DoublesSketch sketch1 = DoublesSketch.builder().build(); // default k=128
      for (int i = 0; i < 10000; i++) {
        sketch1.update(rand.nextGaussian()); // mean=0, stddev=1
      }
      FileOutputStream out1 = new FileOutputStream(new File("QuantilesDoublesSketch1.bin"));
      out1.write(sketch1.toByteArray());
      out1.close();
    
      DoublesSketch sketch2 = DoublesSketch.builder().build(); // default k=128
      for (int i = 0; i < 10000; i++) {
        sketch2.update(rand.nextGaussian() + 1); // shift the mean for the second sketch
      }
      FileOutputStream out2 = new FileOutputStream(new File("QuantilesDoublesSketch2.bin"));
      out2.write(sketch1.toByteArray());
      out2.close();
    }

    // this section deserializes the sketches, produces a union and prints some results
    {
      FileInputStream in1 = new FileInputStream(new File("QuantilesDoublesSketch1.bin"));
      byte[] bytes1 = new byte[in1.available()];
      in1.read(bytes1);
      in1.close();
      DoublesSketch sketch1 = DoublesSketch.heapify(new NativeMemory(bytes1));

      FileInputStream in2 = new FileInputStream(new File("QuantilesDoublesSketch2.bin"));
      byte[] bytes2 = new byte[in2.available()];
      in2.read(bytes2);
      in2.close();
      DoublesSketch sketch2 = DoublesSketch.heapify(new NativeMemory(bytes2));

      DoublesUnion union = DoublesUnion.builder().build(); // default k=128
      union.update(sketch1);
      union.update(sketch2);
      DoublesSketch result = union.getResult();
      // Debug output from the sketch
      System.out.println(result.toString());

      System.out.println("Min, Median, Max values");
      System.out.println(Arrays.toString(result.getQuantiles(new double[] {0, 0.5, 1})));

      System.out.println("Probability Histogram: estimated probability mass in 4 bins: (-inf, -2), [-2, 0), [0, 2), [2, +inf)");
      System.out.println(Arrays.toString(result.getPMF(new double[] {-2, 0, 2})));

      System.out.println("Frequency Histogram: estimated number of original values in the same bins");
      double[] histogram = result.getPMF(new double[] {-2, 0, 2});
      for (int i = 0; i < histogram.length; i++) {
        histogram[i] *= result.getN(); // scale the fractions by the total count of values
      }
      System.out.println(Arrays.toString(histogram));
    }

    Output:
    ### HeapDoublesSketch SUMMARY: 
       K                            : 128
       N                            : 20,000
       Levels (Total, Valid)        : 7, 4
       Level Bit Pattern            : 1001110
       BaseBufferCount              : 32
       Retained Items               : 544
       Storage Bytes                : 4,384
       Normalized Rank Error        : 1.725%
       Min Value                    : -3.868
       Max Value                    : 4.251
    ### END SKETCH SUMMARY

    Min, Median, Max values
    [-3.86825047254591, -0.03652127374339926, 4.251444008979726]

    Probability Histogram: estimated probability mass in 4 bins: (-inf, -2), [-2, 0), [0, 2), [2, +inf)
    [0.0237, 0.4858, 0.4605, 0.03]

    Frequency Histogram: estimated number of original values in the same bins
    [474.0, 9716.0, 9210.0, 600.0]
