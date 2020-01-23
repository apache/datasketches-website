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
# Hyper Log Log Sketch Java Example

    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import org.apache.datasketches.memory.Memory;
    import org.apache.datasketches.hll.HllSketch;
    import org.apache.datasketches.hll.TgtHllType;
    import org.apache.datasketches.hll.Union;

    // simplified file operations and no error handling for clarity
    public class HllExample {

      public static void main(String[] args) throws Exception {
        final int lgK = 10;

        // this section generates two sketches with some overlap and serializes them into files
        {
          // 100000 unique keys
          HllSketch sketch1 = new HllSketch(lgK);
          for (int key = 0; key < 100000; key++) sketch1.update(key);
          FileOutputStream out1 = new FileOutputStream("HllSketch1.bin");
          out1.write(sketch1.toCompactByteArray());
          out1.close();

          // 100000 unique keys
          HllSketch sketch2 = new HllSketch(lgK);
          for (int key = 50000; key < 150000; key++) sketch2.update(key);
          FileOutputStream out2 = new FileOutputStream("HllSketch2.bin");
          out2.write(sketch2.toCompactByteArray());
          out2.close();
        }

        // this section deserializes the sketches, produces union and prints the results
        {
          FileInputStream in1 = new FileInputStream("HllSketch1.bin");
          byte[] bytes1 = new byte[in1.available()];
          in1.read(bytes1);
          in1.close();
          HllSketch sketch1 = HllSketch.heapify(Memory.wrap(bytes1));

          FileInputStream in2 = new FileInputStream("HllSketch2.bin");
          byte[] bytes2 = new byte[in2.available()];
          in2.read(bytes2);
          in2.close();
          HllSketch sketch2 = HllSketch.heapify(Memory.wrap(bytes2));

          Union union = new Union(lgK);
          union.update(sketch1);
          union.update(sketch2);
          HllSketch unionResult = union.getResult(TgtHllType.HLL_4);

          // debug summary of the union result sketch
          System.out.println(unionResult.toString());

          System.out.println("Union unique count estimate: " + unionResult.getEstimate());
          System.out.println("Union unique count lower bound 95% confidence: " + unionResult.getLowerBound(2));
          System.out.println("Union unique count upper bound 95% confidence: " + unionResult.getUpperBound(2));
        }
      }
    }

    Output:
    ### HLL SKETCH SUMMARY: 
    Log Config K   : 10
    Hll Target     : HLL_4
    Current Mode   : HLL
    LB             : 146594.82219597755
    Estimate       : 151359.15391734682
    UB             : 156443.56994041015
    OutOfOrder Flag: true
    CurMin         : 5
    NumAtCurMin    : 12
    HipAccum       : 146853.05495683785
    
    Union unique count estimate: 151359.15391734682
    Union unique count lower bound 95% confidence: 142121.27128389373
    Union unique count upper bound 95% confidence: 161881.44803994312
