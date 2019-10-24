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
# VarOpt Sampling Java Example

This example was run using word counts from Shakespeare plays, namely
Romeo and Juliet and Hamlet. The scripts, available from various
sources including http://shakespeare.mit.edu/, were converted to
(word, count) files with the following Perl command:

    perl -lane 's/^\s+//; s/[;\.,!?:\x27\[\]&]//g; s/--//g; s/\s+/\n/g; print lc if length > 0' input.txt | sort | uniq -c | awk '{print $1 "\t" $2}' > output.txt

These were then used in the following example, slightly modified to remove error handling for clarity. Serialization and deserialization are completely parallel to the Reservoir Sampling sketch, and example code for that may be found in those Java examples.


    import java.io.BufferedReader;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.FileReader;

    import org.apache.datasketches.memory.Memory;
    import org.apache.datasketches.ArrayOfLongsSerDe;
    import org.apache.datasketches.sampling.SampleSubsetSummary;
    import org.apache.datasketches.sampling.VarOptItemsSamples;
    import org.apache.datasketches.sampling.VarOptItemsSketch;
    import org.apache.datasketches.sampling.VarOptItemsUnion;

    // load (token, count) data from file, build sketch of size k
    private static VarOptItemsSketch<String> loadFile(final String filename,
                                                      final int k) {
      try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        VarOptItemsSketch<String> vis = VarOptItemsSketch.newInstance(k);
        String line;
        while ((line = br.readLine()) != null) {
          String[] tokens = line.split("\\s+");
          if (tokens.length == 2) {
            vis.update(tokens[1], Double.parseDouble(tokens[0]));
          }
        }
        return vis;
      }
    }

    // this section loads two sketches from prepared text files, unions them
    // and demonstrates how to estimate subset sums for VarOpt sketches
    {
      final int k = 100;
      VarOptItemsSketch<String> sketch1 = loadFile("/path/to/romeo_juliet.tsv", k);
      VarOptItemsSketch<String> sketch2 = loadFile("/path/to/hamlet.tsv", k);
      VarOptItemsUnion<String> union = VarOptItemsUnion.newInstance(k);
      union.update(sketch1);
      union.update(sketch2);

      // get and iterate over samples
      VarOptItemsSamples<String> samples = union.getResult().getSketchSamples();
      for (VarOptItemsSamples<String>.WeightedSample ws : samples) {
        System.out.println(ws.getItem() + "\t" + ws.getWeight());
      }

      // apply predicate to estimate subset sums, here words of > 7 chars
      SampleSubsetSummary summary = union.getResult().estimateSubsetSum(s -> s.length() > 7);
      System.out.printf("[%f, %f, %f]\n",
            summary.getLowerBound(), summary.getEstimate(), summary.getUpperBound());
    }

Sample Output:

    i	567.0
    i	580.0
    to	737.0
    of	667.0
    and	716.0
    and	964.0
    the	1141.0
    the	681.0
    you	560.5978260869568
    message	560.5978260869568
    yon	560.5978260869568
    thy	560.5978260869568
    a	560.5978260869568
    ...
    [truncated]

Lower bound, estimate, upper bound:

    [594.395954, 2242.391304, 5611.681344]
