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

# Theta Sketch and Tuple Sketch Set Operation Corner Cases

The *TupleSketch* is an extension of the *ThetaSketch* and both are part of the *Theta Sketch Framework*<sup>1</sup>. In this document, the term *Theta* (upper case) when referencing sketches will refer to both the *ThetaSketch* and the *TupleSketch*.  This is not to be confused with the term *theta* (lower case), which refers to the sketch variable that tracks the sampling probability of the sketch.

Because Theta sketches provide the set operations of *intersection* and *difference* (*A and not B* or just *A not B*), a number of corner cases arise that require some analysis to determine how the code should handle them.

Theta sketches track three key variables in addition to retained data:

* *theta*: This is the current sampling probability of the sketch and mathematically expressed as a real number between 0 and 1 inclusive. In the code it is expressed as a double-precision (64-bit) floating-point value. However, internally in the sketch, this value is expressed as a 64-bit, signed, long integer (usually identified as *thetaLong* in the code), where the maximum positive value (*Long.MAX_VALUE*) is interpreted as the double 1.0. In this document we will only refer to the mathematical quantity *theta*.

* *retained entries* or *count*: This is the number of hash values currently retained in the sketch. It can never be less than zero.

* *empty*: 
    * By definition, if *empty = true*, the number of *retained entries* must be zero. The value of *theta* is irrelevant in this case, and can be assumed to be 1.0.
    * If *empty* = false, the *retained entries* can be zero or greater than zero, and *theta* can be 1.0 or less than 1.0.

We have developed a shorthand notation for these three variables to record their state as *{theta, retained entries, empty}*. When analyzing the corner cases of the set operations, we only need to know whether *theta* is 1.0 or less than 1.0, *retained entries* is zero or greater than zero, and *empty* is true or false. These are further abbreviated as

* *theta* can be *1.0* or *<1.0*
* *retained entries* can be either *0* or *>0*
* *empty* can be either *T* or *F*

Each of the above three states can be represented as a boolean variable. Thus, there are 8 possible combinations of the three variables.

---

<sup>1</sup> Anirban Dasgupta, Kevin J. Lang, Lee Rhodes, and Justin Thaler. A framework for estimating stream expression cardinalities. In *EDBT/ICDT Proceedings ‘16 *, pages 6:1–6:17, 2016.

## Valid States of a Sketch

Of the eight possible combinations of the three boolean variables and using the above notation, there are four valid states of a *Theta* sketch.

### Empty{1.0, 0, T}
When a new sketch is created, *theta* is set to 1.0, *retained entries* is set to zero, and *empty* is true. This state can also occur as the result of a set operation, where the operation creates a new sketch to potentially load result data into the sketch but there is no data to load into the sketch. So it effectively returns a new empty sketch that has been untouched and unaffected by the input arguments to the set operation.

### Exact{1.0, >0, F}
All of the *Theta* sketches have an internal buffer that is effectively a list of hash values of the items received by the sketch. If the number of distinct input items does not exceed the size of that buffer, the sketch is in *exact* mode. There is no probabilistic estimation involved so *theta = 1.0*, which indicates that all distinct values are in the buffer. *retained entries* is the count of those values in the buffer, and the sketch is not *empty*.

### Estimation{<1.0, >0, F}
Here, the number of distinct inputs to the sketch have exceeded the size of the buffer, so the sketch must start choosing what values to retain in the sketch and starts reducing the value of *theta* accordingly. *theta < 1.0*, *retained entries > 0*, and *empty = F*.

### Degenerate{<1.0, 0, F}
This requires some explanation. Imagine the intersection of two estimating sketches where the values retained in the two sketches are disjoint (i.e, no overlap). Since the two sketches chose their internal values at random, there remains some probability that there could be common values in an exactly computed intersection, but it just so happens that one of the two sketches did not select any of them in the random sampling process. Therefore, the *retained entries = 0*. 

Even though the *retained entries = 0* the upper bound of the estimated number of distinct values in the input domain, but missed by the sketch, can be computed statistically. It is too complex to discuss here, but the sketch code actually performs this estimation.

Since there is a non-zero probability of a non-empty intersection, *empty = F*. This is also a degenerate case in the sense that *theta < 1.0* and *empty = F* like an estimating sketch, except that no actual values were found in the operation, so *retained entries = 0*.

### Summary Table of the Valid States of a Sketch
The *Has Seen Data* column is not an independent variable, but helps with the interpretation of the state.

We can assign a single octal digit ID to each state where

* *theta = 1.0 := 4, else 0*
* *retained entries >0 := 2, else 0*
* *empty = true := 1, else 0* 

The octal digit `ID = ((theta == 1.0) ? 4 : 0) | ((retainedEntries > 0) ? 2 : 0) | (empty ? 1 : 0);`

| Shorthand Notation                | theta | retained entries |    empty   | Has Seen Data | ID | Comments                        |
|:---------------------------------:|:-----:|:----------------:|:----------:|:-------------:|:--:|:-------------------------------:|
| Empty {1.0,0,T}                   |  1.0  |         0        |     T      |       F       |  5 | Empty Sketch                    |
| Exact {1.0,>0,F}                  |  1.0  |        >0        |     F      |       T       |  6 | Exact Mode                      |
| Estimation {<1.0,>0,F}            | <1.0  |        >0        |     F      |       T       |  2 | Estimation Mode                 |
| Degenerate {<1.0,0,F}<sup>3</sup> | <1.0  |         0        |     F      |       T       |  0 | Valid Intersect or AnotB result |

---

<sup>3</sup> *Degenerate*: Can appear as a result of a an Intersection or AnotB of certain combination of sketches.

## Invalid States of a Sketch
The remaining four combinations of the variables are invalid and should not occur.

The *Has Seen Data* column is not an independent variable, but helps with the interpretation of the state.

| Theta | Retained Entries | Empty Flag | Has Seen Data | Comments                                           |
|:-----:|:----------------:|:----------:|:-------------:|:--------------------------------------------------:|
|  1.0  |        0         |      F     |       T       | If it has seen data Empty = F; &there4; Theta cannot be = 1.0 AND Entries = 0    |
|  1.0  |       >0         |      T     |       F       | If it has not seen data Empty = T; &there4; Entries cannot be > 0                |
| <1.0  |       >0         |      T     |       F       | If it has not seen data, Empty = T; &there4; Theta cannot be < 1.0 OR Entries > 0|
| <1.0  |        0         |      T     |       F       | If it has not seen data, Empty = T; &there4; Theta cannot be < 1.0               |

## Combinations of States of Two Sketches
Each sketch can have four valid states, which means we can have 16 combinations of states of two sketches as expanded in the following table.
 
| ID | Sketch A               | Sketch B               | Intersection Result       | AnotB Result              | Result Actions |
|:--:|:----------------------:|:----------------------:|:-------------------------:|:-------------------------:|:--------------:|
| 55 | Empty {1.0,0,T}        | Empty {1.0,0,T}        | Empty {1.0,0,T}           | Empty {1.0,0,T}           | 1,1            |
| 56 | Empty {1.0,0,T}        | Exact {1.0,>0,F}       | Empty {1.0,0,T}           | Empty {1.0,0,T}           | 1,1            |
| 52 | Empty {1.0,0,T}        | Estimation {<1.0,>0,F} | Empty {1.0,0,T}           | Empty {1.0,0,T}           | 1,1            |
| 50 | Empty {1.0,0,T}        | Degenerate {<1.0,0,F}  | Empty {1.0,0,T}           | Empty {1.0,0,T}           | 1,1            |
| 65 | Exact {1.0,>0,F}       | Empty {1.0,0,T}        | Empty {1.0,0,T}           | Sketch A                  | 1,5            |
| 66 | Exact {1.0,>0,F}       | Exact {1.0,>0,F}       | Full Intersect            | Full AnotB                | 6,7            |
| 62 | Exact {1.0,>0,F}       | Estimation {<1.0,>0,F} | Full Intersect            | Full AnotB                | 6,7            |
| 60 | Exact {1.0,>0,F}       | Degenerate {<1.0,0,F}  | Degenerate {minTheta,0,F} | Trim A by minTheta        | 2,4            |
| 25 | Estimation {<1.0,>0,F} | Empty {1.0,0,T}        | Empty {1.0,0,T}           | Sketch A                  | 1,5            |
| 26 | Estimation {<1.0,>0,F} | Exact {1.0,>0,F}       | Full Intersect            | Full AnotB                | 6,7            |
| 22 | Estimation {<1.0,>0,F} | Estimation {<1.0,>0,F} | Full Intersect            | Full AnotB                | 6,7            |
| 20 | Estimation {<1.0,>0,F} | Degenerate {<1.0,0,F}  | Degenerate {minTheta,0,F} | Trim A by minTheta        | 2,4            |
| 05 | Degenerate {<1.0,0,F}  | Empty {1.0,0,T}        | Empty {1.0,0,T}           | Degenerate {ThetaA,0,F}   | 1,3            |
| 06 | Degenerate {<1.0,0,F}  | Exact {1.0,>0,F}       | Degenerate {minTheta,0,F} | Degenerate {ThetaA,0,F}   | 2,3            |
| 02 | Degenerate {<1.0,0,F}  | Estimation {<1.0,>0,F} | Degenerate {minTheta,0,F} | Degenerate {minTheta,0,F} | 2,2            |
| 00 | Degenerate {<1.0,0,F}  | Degenerate {<1.0,0,F}  | Degenerate {minTheta,0,F} | Degenerate {minTheta,0,F} | 2,2            |

The description of each column:

* ID: two octal digits, the first digit represents the state of Sketch A, the second digit represents the state of Sketch B.
* Sketch A State
* Sketch B State
* Intersection Result
* AnotB Result
* The ID of the Intersection Result followed by the ID of the AnotB result. The result codes are given by the following table:

| Result Action                | Result ID | Used by Intersection | Used By AnotB |
|:----------------------------:|:-----------:|:--------------------:|:-------------:|
| Empty{1.0,0,T}               |     1       | Yes                  | Yes           |
| Degenerate{minTheta,0,F}     |     2       | Yes                  | Yes           |
| Degenerate{thetaA,0,F}       |     3       |                      | Yes           |
| Sketch A trimmed by minTheta |     4       |                      | Yes           |
| Sketch A                     |     5       |                      | Yes           |
| Full Intersect               |     6       | Yes                  |               |
| Full AnotB                   |     7       |                      | Yes           |

Note that the results of a *Full Intersect* or a *Full AnotB* will require further interpretation of the resulting state.
For example, if the resulting sketch is *{1.0,0,?}*, then an *Empty{1.0,0,T}* is returned. 
If the resulting sketch is *{<1.0,0,?}* then a *Degenerate{<1.0,0,F}* is returned.  
Otherwise, the sketch returned will be an estimating or exact *{theta, >0, F}*.

## Testing
The above information is encoded as a model into the special class *[org.apache.datasketches.SetOperationsCornerCases](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.SetOperationCornerCases.java)*. This class is made up of enums and static methods to quickly determine for a sketch what actions to take based on the state of the input arguments. This model is independent of the implementation of the Theta Sketch, whether the set operation is performed as a Theta Sketch, or a Tuple Sketch and when translated can be used in other languages as well.  

Before this model was put to use an extensive set of tests was designed to test any potential implementation against this model. These tests are slightly different for the Tuple Sketch than the Theta Sketch because the Tuple Sketch has more combinations to test, but the model is the same.

* The tests for the Theta Sketch can be found in the class *[org.apache.datasketches.theta.CornerCaseThetaSetOperationsTest](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.theta.CornerCaseThetaSetOperationsTest.java)*
* The tests for the Tuple Sketch can be found in the class *[org.apache.datasketches.tuple.aninteger.CornerCaseTupleSetOperationsTest](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.tuple.aninteger.CornerCaseTupleSetOperationsTest.java)*

The details of how this model is used in run-time code can be found in the class *[org.apache.datasketches.tuple.AnotB.java](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.tuple.AnotB.java)*.


