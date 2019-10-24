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
# Reservoir Sampling Java Example

    // simplified file operations and no error handling for clarity

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;

    import org.apache.datasketches.memory.Memory;
    import org.apache.datasketches.ArrayOfLongsSerDe;
    import org.apache.datasketches.sampling.ReservoirItemsSketch;
    import org.apache.datasketches.sampling.ReservoirItemsUnion;

    // this section generates two sketches with some overlap
    // and serializes them into files in compact (not updatable) form
    {
      int k = 8192;

      // 100000 unique keys
      ReservoirItemsSketch<Long> sketch1 = ReservoirItemsSketch.newInstance(k);
      for (long key = 0; key < 100000; key++) { sketch1.update(key); }
      FileOutputStream out1 = new FileOutputStream(new File("Reservoir1.bin"));
      out1.write(sketch1.toByteArray(new ArrayOfLongsSerDe()));
      out1.close();

      // 100000 unique keys
      // the first 50000 unique keys overlap with sketch1
      ReservoirItemsSketch<Long> sketch2 = ReservoirItemsSketch.newInstance(k);
      for (long key = 50000; key < 150000; key++) { sketch2.update(key); }
      FileOutputStream out2 = new FileOutputStream(new File("Reservoir2.bin"));
      out2.write(sketch2.toByteArray(new ArrayOfLongsSerDe()));
      out2.close();
    }

    // this section deserializes the sketches, produces their union, and prints the results
    {
      FileInputStream in1 = new FileInputStream(new File("Reservoir1.bin"));
      byte[] bytes1 = new byte[in1.available()];
      in1.read(bytes1);
      in1.close();
      ReservoirItemsSketch<Long> sketch1 = ReservoirItemsSketch.heapify(Memory.wrap(bytes1), 
                                                                        new ArrayOfLongsSerDe());

      FileInputStream in2 = new FileInputStream(new File("Reservoir2.bin"));
      byte[] bytes2 = new byte[in2.available()];
      in2.read(bytes2);
      in2.close();
      ReservoirItemsSketch<Long> sketch2 = ReservoirItemsSketch.heapify(Memory.wrap(bytes2),
                                                                        new ArrayOfLongsSerDe());

      int k = sketch1.getK();
      ReservoirItemsUnion<Long> union = ReservoirItemsUnion.newInstance(k);
      union.update(sketch1);
      union.update(sketch2);
      ReservoirItemsSketch<Long> unionResult = union.getResult();

      // debug summary of the union result sketch
      System.out.println(unionResult.toString());
      System.out.println("First 10 results in union:");
      Long[] samples = unionResult.getSamples();
      for (int i = 0; i < 10; i++) {
          System.out.println(i + ": " + samples[i]);
      }
    }

Output:

    ### ReservoirItemsSketch SUMMARY:
       k            : 8192
       n            : 200000
       Current size : 8192
       Resize factor: X8
    ### END SKETCH SUMMARY
    
    First 10 results in union:
    0: 6843
    1: 1
    2: 18592
    3: 13470
    4: 24367
    5: 12686
    6: 73476
    7: 15003
    8: 68131
    9: 18649
    
