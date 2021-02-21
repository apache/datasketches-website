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
# KLL Sketch C++ Example

    #include <iostream>
    #include <fstream>
    #include <random>
    #include <chrono>

    #include <kll_sketch.hpp>

    //simplified file operations and no error handling for clarity
    int main(int argc, char **argv) {
      // this section generates two sketches from random data and serializes them into files
      {
        std::default_random_engine generator(std::chrono::system_clock::now().time_since_epoch().count());
        std::normal_distribution<float> nd(0, 1); // mean=0, stddev=1

        datasketches::kll_sketch<float> sketch1; // default k=200
        for (int i = 0; i < 10000; i++) {
          sketch1.update(nd(generator)); // mean=0, stddev=1
        }
        std::ofstream os1("kll_sketch_float1.bin");
        sketch1.serialize(os1);

        datasketches::kll_sketch<float> sketch2; // default k=200
        for (int i = 0; i < 10000; i++) {
          sketch2.update(nd(generator) + 1); // shift the mean for the second sketch
        }
        std::ofstream os2("kll_sketch_float2.bin");
        sketch2.serialize(os2);
      }

      // this section deserializes the sketches, produces a union and prints some results
      {
        std::ifstream is1("kll_sketch_float1.bin");
        auto sketch1 = datasketches::kll_sketch<float>::deserialize(is1);

        std::ifstream is2("kll_sketch_float2.bin");
        auto sketch2 = datasketches::kll_sketch<float>::deserialize(is2);

        // we could merge sketch2 into sketch1 or the other way around
        // this is an example of using a new sketch as a union and keeping the original sketches intact
        datasketches::kll_sketch<float> u; // default k=200
        u.merge(sketch1);
        u.merge(sketch2);

        // Debug output
        u.to_stream(std::cout);

        std::cout << "Min, Median, Max values" << std::endl;
        const double fractions[3] {0, 0.5, 1};
        auto quantiles = u.get_quantiles(fractions, 3);
        std::cout << quantiles[0] << ", " << quantiles[1] << ", " << quantiles[2] << std::endl;

        std::cout << "Probability Histogram: estimated probability mass in 4 bins: (-inf, -2), [-2, 0), [0, 2), [2, +inf)" << std::endl;
        const float split_points[] {-2, 0, 2};
        const int num_split_points = 3;
        auto pmf = u.get_PMF(split_points, num_split_points);
        std::cout << pmf[0] << ", " << pmf[1] << ", " << pmf[2] << ", " << pmf[3] << std::endl;

        std::cout << "Frequency Histogram: estimated number of original values in the same bins" << std::endl;
        const int num_bins = num_split_points + 1;
        int histogram[num_bins];
        for (int i = 0; i < num_bins; i++) {
          histogram[i] = pmf[i] * u.get_n(); // scale the fractions by the total count of values
        }
        std::cout << histogram[0] << ", " << histogram[1] << ", " << histogram[2] << ", " << histogram[3] << std::endl;
      }

      return 0;
    }

    Output (will be sligtly different every time due to random input):
    ### KLL sketch summary:
       K              : 200
       min K          : 200
       M              : 8
       N              : 20000
       Epsilon        : 1.33%
       Epsilon PMF    : 1.65%
       Empty          : false
       Estimation mode: true
       Levels         : 7
       Sorted         : false
       Capacity items : 565
       Retained items : 394
       Storage bytes  : 1632
       Min value      : -3.49
       Max value      : 4.52
    ### End sketch summary
    Min, Median, Max values
    -3.49, 0.51, 4.52
    Probability Histogram: estimated probability mass in 4 bins: (-inf, -2), [-2, 0), [0, 2), [2, +inf)
    0.0146, 0.313, 0.582, 0.0901
    Frequency Histogram: estimated number of original values in the same bins
    293, 6267, 11639, 1801
