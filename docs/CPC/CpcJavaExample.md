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
# CPC Sketch Java Example

    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import com.yahoo.memory.Memory;
    import com.yahoo.sketches.cpc.CpcSketch;
    import com.yahoo.sketches.cpc.CpcUnion;
    
    //simplified file operations and no error handling for clarity
    public class CpcExample {
    
      public static void main(String[] args) throws Exception {
        final int lgK = 10;
        // this section generates two sketches with some overlap and serializes them into files
        {
          // 100000 distinct keys
          CpcSketch sketch1 = new CpcSketch(lgK);
          for (int key = 0; key < 100000; key++) sketch1.update(key);
          FileOutputStream out1 = new FileOutputStream("CpcSketch1.bin");
          out1.write(sketch1.toByteArray());
          out1.close();
    
          // 100000 distinct keys
          CpcSketch sketch2 = new CpcSketch(lgK);
          for (int key = 50000; key < 150000; key++) sketch2.update(key);
          FileOutputStream out2 = new FileOutputStream("CpcSketch2.bin");
          out2.write(sketch2.toByteArray());
          out2.close();
        }
    
        // this section deserializes the sketches, produces a union and prints the result
        {
          FileInputStream in1 = new FileInputStream("CpcSketch1.bin");
          byte[] bytes1 = new byte[in1.available()];
          in1.read(bytes1);
          in1.close();
          CpcSketch sketch1 = CpcSketch.heapify(Memory.wrap(bytes1));
    
          FileInputStream in2 = new FileInputStream("CpcSketch2.bin");
          byte[] bytes2 = new byte[in2.available()];
          in2.read(bytes2);
          in2.close();
          CpcSketch sketch2 = CpcSketch.heapify(Memory.wrap(bytes2));
    
          CpcUnion union = new CpcUnion(lgK);
          union.update(sketch1);
          union.update(sketch2);
          CpcSketch result = union.getResult();
    
          // debug summary of the union result sketch
          System.out.println(result.toString());
    
          System.out.println("Distinct count estimate: " + result.getEstimate());
          System.out.println("Distinct count lower bound 95% confidence: " + result.getLowerBound(2));
          System.out.println("Distinct count upper bound 95% confidence: " + result.getUpperBound(2));
        }
      }
    
    }

    Output:
    ### CPD SKETCH - PREAMBLE:
      Flavor         : SLIDING
      LgK            : 10
      Merge Flag     : true
      Error Const    : 0.6931471805599453
      RSE            : 0.02166084939249829
      Seed Hash      : 93cc | 37836
      Num Coupons    : 7706
      Num Pairs (SV) : 27
      First Inter Col: 4
      Valid Window   : true
      Valid PairTable: true
      Window Offset  : 5
      KxP            : 1024.0
      HIP Accum      : 0.0
    ### END CPC SKETCH
    Distinct count estimate: 149796.50599220005
    Distinct count lower bound 95% confidence: 143416.2744812169
    Distinct count upper bound 95% confidence: 156397.0
