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
# Frequent Items Sketch C++ Example

    #include <iostream>
    #include <fstream>

    #include <frequent_items_sketch.hpp>

    //simplified file operations and no error handling for clarity
    int main(int argc, char **argv) {
      typedef datasketches::frequent_items_sketch<std::string> frequent_strings_sketch;

      // this section generates two sketches and serializes them into files
      {
        frequent_strings_sketch sketch1(64);
        sketch1.update("a");
        sketch1.update("a");
        sketch1.update("b");
        sketch1.update("c");
        sketch1.update("a");
        sketch1.update("d");
        sketch1.update("a");
        std::ofstream os1("freq_str_sketch1.bin");
        sketch1.serialize(os1);

        frequent_strings_sketch sketch2(64);
        sketch2.update("e");
        sketch2.update("a");
        sketch2.update("f");
        sketch2.update("f");
        sketch2.update("f");
        sketch2.update("g");
        sketch2.update("a");
        sketch2.update("f");
        std::ofstream os2("freq_str_sketch2.bin");
        sketch2.serialize(os2);
      }

      // this section deserializes the sketches, produces a union and prints the result
      {
        std::ifstream is1("freq_str_sketch1.bin");
        frequent_strings_sketch sketch1 = frequent_strings_sketch::deserialize(is1);

        std::ifstream is2("freq_str_sketch2.bin");
        frequent_strings_sketch sketch2 = frequent_strings_sketch::deserialize(is2);

        // we could merge sketch2 into sketch1 or the other way around
        // this is an example of using a new sketch as a union and keeping the original sketches intact
        frequent_strings_sketch u(64);
        u.merge(sketch1);
        u.merge(sketch2);

        auto items = u.get_frequent_items(datasketches::NO_FALSE_POSITIVES);
        std::cout << "Frequent strings: " << items.size() << std::endl;
        std::cout << "Str\tEst\tLB\tUB" << std::endl;
        for (auto row: items) {
          std::cout << row.get_item() << "\t" << row.get_estimate() << "\t"
            << row.get_lower_bound() << "\t" << row.get_upper_bound() << std::endl;
        }
      }

      return 0;
    }

    Output:
    Frequent strings: 7
    Str	Est	LB	UB
    a	6	6	6
    f	4	4	4
    c	1	1	1
    d	1	1	1
    e	1	1	1
    b	1	1	1
    g	1	1	1
