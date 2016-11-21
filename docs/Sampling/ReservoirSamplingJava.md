---
layout: doc_page
---

# Reservoir Sampling Java Example

    // simplified file operations and no error handling for clarity

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;

    import com.yahoo.memory.NativeMemory;
    import com.yahoo.sketches.ArrayOfLongsSerDe;
    import com.yahoo.sketches.sampling.ReservoirItemsSketch;
    import com.yahoo.sketches.sampling.ReservoirItemsUnion;

    // this section generates two sketches with some overlap
    // and serializes them into files in compact (not updatable) form
    {
      int k = 8192;

      // 100000 unique keys
      ReservoirItemsSketch<Long> sketch1 = ReservoirItemsSketch.getInstance(k);
      for (long key = 0; key < 100000; key++) { sketch1.update(key); }
      FileOutputStream out1 = new FileOutputStream(new File("Reservoir1.bin"));
      out1.write(sketch1.toByteArray(new ArrayOfLongsSerDe()));
      out1.close();

      // 100000 unique keys
      // the first 50000 unique keys overlap with sketch1
      ReservoirItemsSketch<Long> sketch2 = ReservoirItemsSketch.getInstance(k);
      for (long key = 0; key < 100000; key++) { sketch2.update(key); }
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
      ReservoirItemsSketch<Long> sketch1 = ReservoirItemsSketch.getInstance(new NativeMemory(bytes1), 
                                                                            new ArrayOfLongsSerDe());

      FileInputStream in2 = new FileInputStream(new File("Reservoir2.bin"));
      byte[] bytes2 = new byte[in2.available()];
      in2.read(bytes2);
      in2.close();
      ReservoirItemsSketch<Long> sketch2 = ReservoirItemsSketch.getInstance(new NativeMemory(bytes2),
                                                                            new ArrayOfLongsSerDe());

      int k = sketch1.getK();
      ReservoirItemsUnion<Long> union = ReservoirItemsUnion.getInstance(k);
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
