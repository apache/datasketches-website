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
# Theta Sketch Java Example

    // simplified file operations and no error handling for clarity

    import java.io.FileInputStream;
    import java.io.FileOutputStream;

    import org.apache.datasketches.memory.Memory;
    import org.apache.datasketches.theta.Sketch;
    import org.apache.datasketches.theta.Sketches;
    import org.apache.datasketches.theta.UpdateSketch;
    import org.apache.datasketches.theta.Union;
    import org.apache.datasketches.theta.Intersection;
    import org.apache.datasketches.theta.SetOperation;

    // this section generates two sketches with some overlap
    // and serializes them into files in compact (not updatable) form
    {
      // 100000 unique keys
      UpdateSketch sketch1 = UpdateSketch.builder().build();
      for (int key = 0; key < 100000; key++) sketch1.update(key);
      FileOutputStream out1 = new FileOutputStream("ThetaSketch1.bin");
      out1.write(sketch1.compact().toByteArray());
      out1.close();

      // 100000 unique keys
      // the first 50000 unique keys overlap with sketch1
      UpdateSketch sketch2 = UpdateSketch.builder().build();
      for (int key = 50000; key < 150000; key++) sketch2.update(key);
      FileOutputStream out2 = new FileOutputStream("ThetaSketch2.bin");
      out2.write(sketch2.compact().toByteArray());
      out2.close();
    }

    // this section deserializes the sketches, produces union and intersection and prints the results
    {
      FileInputStream in1 = new FileInputStream("ThetaSketch1.bin");
      byte[] bytes1 = new byte[in1.available()];
      in1.read(bytes1);
      in1.close();
      Sketch sketch1 = Sketches.wrapSketch(Memory.wrap(bytes1));

      FileInputStream in2 = new FileInputStream("ThetaSketch2.bin");
      byte[] bytes2 = new byte[in2.available()];
      in2.read(bytes2);
      in2.close();
      Sketch sketch2 = Sketches.wrapSketch(Memory.wrap(bytes2));

      Union union = SetOperation.builder().buildUnion();
      union.update(sketch1);
      union.update(sketch2);
      Sketch unionResult = union.getResult();

      // debug summary of the union result sketch
      System.out.println(unionResult.toString());

      System.out.println("Union unique count estimate: " + unionResult.getEstimate());
      System.out.println("Union unique count lower bound 95% confidence: " + unionResult.getLowerBound(2));
      System.out.println("Union unique count upper bound 95% confidence: " + unionResult.getUpperBound(2));

      Intersection intersection = SetOperation.builder().buildIntersection();
      intersection.update(sketch1);
      intersection.update(sketch2);
      Sketch intersectionResult = intersection.getResult();

      // debug summary of the intersection result sketch
      System.out.println(intersectionResult.toString());

      System.out.println("Intersection unique count estimate: " + intersectionResult.getEstimate());
      System.out.println("Intersection unique count lower bound 95% confidence: " + intersectionResult.getLowerBound(2));
      System.out.println("Intersection unique count upper bound 95% confidence: " + intersectionResult.getUpperBound(2));
    }


    Output:
    ### HeapCompactOrderedSketch SUMMARY: 
       Estimate                : 149586.73149344584
       Upper Bound, 95% conf   : 154287.5017892762
       Lower Bound, 95% conf   : 145028.6046846571
       Theta (double)          : 0.027382107751846067
       Theta (long)            : 252555366948521403
       Theta (long) hex        : 038141c4a515c5bb
       EstMode?                : true
       Empty?                  : false
       Array Size Entries      : 4096
       Retained Entries        : 4096
       Seed Hash               : 93cc
    ### END SKETCH SUMMARY

    Union unique count estimate: 149586.73149344584
    Union unique count lower bound 95% confidence: 145028.6046846571
    Union unique count upper bound 95% confidence: 154287.5017892762

    ### HeapCompactOrderedSketch SUMMARY: 
       Estimate                : 48249.113729035394
       Upper Bound, 95% conf   : 50358.736970106176
       Lower Bound, 95% conf   : 46227.35737896924
       Theta (double)          : 0.04377282475820978
       Theta (long)            : 403733047849016500
       Theta (long) hex        : 059a591165205cb4
       EstMode?                : true
       Empty?                  : false
       Array Size Entries      : 2112
       Retained Entries        : 2112
       Seed Hash               : 93cc
    ### END SKETCH SUMMARY

    Intersection unique count estimate: 48249.113729035394
    Intersection unique count lower bound 95% confidence: 46227.35737896924
    Intersection unique count upper bound 95% confidence: 50358.736970106176
