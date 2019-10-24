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
# Quantiles Sketch Java Example

    // simplified file operations and no error handling for clarity

    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.util.Arrays;
    import java.util.Random;

    import org.apache.datasketches.memory.Memory;

    import org.apache.datasketches.quantiles.DoublesSketch;
    import org.apache.datasketches.quantiles.DoublesUnion;
    import org.apache.datasketches.quantiles.UpdateDoublesSketch;

    // this section generates two sketches from random data and serializes them into files
    {
      Random rand = new Random();

      UpdateDoublesSketch sketch1 = DoublesSketch.builder().build(); // default k=128
      for (int i = 0; i < 10000; i++) {
        sketch1.update(rand.nextGaussian()); // mean=0, stddev=1
      }
      FileOutputStream out1 = new FileOutputStream("QuantilesDoublesSketch1.bin");
      out1.write(sketch1.toByteArray());
      out1.close();
    
      UpdateDoublesSketch sketch2 = DoublesSketch.builder().build(); // default k=128
      for (int i = 0; i < 10000; i++) {
        sketch2.update(rand.nextGaussian() + 1); // shift the mean for the second sketch
      }
      FileOutputStream out2 = new FileOutputStream("QuantilesDoublesSketch2.bin");
      out2.write(sketch2.toByteArray());
      out2.close();
    }

    // this section deserializes the sketches, produces a union and prints some results
    {
      FileInputStream in1 = new FileInputStream("QuantilesDoublesSketch1.bin");
      byte[] bytes1 = new byte[in1.available()];
      in1.read(bytes1);
      in1.close();
      DoublesSketch sketch1 = DoublesSketch.wrap(Memory.wrap(bytes1));

      FileInputStream in2 = new FileInputStream("QuantilesDoublesSketch2.bin");
      byte[] bytes2 = new byte[in2.available()];
      in2.read(bytes2);
      in2.close();
      DoublesSketch sketch2 = DoublesSketch.wrap(Memory.wrap(bytes2));

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
    ### Quantiles HeapUpdateDoublesSketch SUMMARY: 
    Empty                        : false
    Direct, Capacity bytes       : false, 
    Estimation Mode              : true
    K                            : 128
    N                            : 20,000
    Levels (Needed, Total, Valid): 7, 7, 4
    Level Bit Pattern            : 1001110
    BaseBufferCount              : 32
    Combined Buffer Capacity     : 1,152
    Retained Items               : 544
    Compact Storage Bytes        : 4,384
    Updatable Storage Bytes      : 9,248
    Normalized Rank Error        : 1.725%
    Min Value                    : -4.113
    Max Value                    : 4.363
    ### END SKETCH SUMMARY

    Min, Median, Max values
    [-4.113097775288085, 0.49496152841809893, 4.362712872544037]
    Probability Histogram: estimated probability mass in 4 bins: (-inf, -2), [-2, 0), [0, 2), [2, +inf)
    [0.01445, 0.3071, 0.58545, 0.093]
    Frequency Histogram: estimated number of original values in the same bins
    [289.0, 6142.0, 11709.0, 1860.0]
