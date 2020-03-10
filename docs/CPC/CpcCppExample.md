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
# CPC Sketch C++ Example

    #include <iostream>
    #include <fstream>
    
    #include <cpc_sketch.hpp>
    #include <cpc_union.hpp>
    
    //simplified file operations and no error handling for clarity
    int main(int argc, char **argv) {
      const int lg_k = 10;
    
      // this section generates two sketches with some overlap and serializes them into files
      {
        // 100000 distinct keys
        datasketches::cpc_sketch sketch1(lg_k);
        for (int key = 0; key < 100000; key++) sketch1.update(key);
        std::ofstream os1("cpc_sketch1.bin");
        sketch1.serialize(os1);
    
        // 100000 distinct keys
        datasketches::cpc_sketch sketch2(lg_k);
        for (int key = 50000; key < 150000; key++) sketch2.update(key);
        std::ofstream os2("cpc_sketch2.bin");
        sketch2.serialize(os2);
      }
    
      // this section deserializes the sketches, produces union and prints the result
      {
        std::ifstream is1("cpc_sketch1.bin");
        datasketches::cpc_sketch sketch1 = datasketches::cpc_sketch::deserialize(is1);
    
        std::ifstream is2("cpc_sketch2.bin");
        datasketches::cpc_sketch sketch2 = datasketches::cpc_sketch::deserialize(is2);
    
        datasketches::cpc_union u(lg_k);
        u.update(sketch1);
        u.update(sketch2);
        datasketches::cpc_sketch sketch = u.get_result();
    
        // debug summary of the union result sketch
        sketch.to_stream(std::cout);

        std::cout << "Distinct count estimate: " << sketch.get_estimate() << std::endl;
        std::cout << "Distinct count lower bound 95% confidence: " << sketch.get_lower_bound(2) << std::endl;
        std::cout << "Distinct count upper bound 95% confidence: " << sketch.get_upper_bound(2) << std::endl;
      }
    
      return 0;
    }

    Output:
    ### CPC sketch summary:
       lg_k           : 10
       seed hash      : 93cc
       C              : 7706
       flavor         : 4
       merged         : true
       intresting col : 4
       table entries  : 27
       window         : allocated
       window offset  : 5
    ### End sketch summary
    Distinct count estimate: 149797
    Distinct count lower bound 95% confidence: 143416
    Distinct count upper bound 95% confidence: 156397
