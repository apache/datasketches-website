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

The *TupleSketch* is an extension of the *ThetaSketch* and both are part of the *Theta Sketch Framework*<sup>1</sup>. 
In this document, the term *Theta* (upper case) when referencing sketches will refer to both the *ThetaSketch* and the *TupleSketch*.  
This is not to be confused with the term *theta* (lower case), which refers to the sketch variable that tracks the sampling probability of the sketch.

Because Theta sketches provide the set operations of *intersection* and *difference* (*A and not B* or just *A not B*), a number of corner cases arise that require some analysis to determine how the code should handle them.

Theta sketches track three key variables in addition to retained data:

* *theta*: This is the current sampling probability of the sketch and mathematically expressed as a real number between 0 and 1 inclusive. In the code it is expressed as a double-precision (64-bit) floating-point value. However, internally in the sketch, this value is expressed as a 64-bit, signed, long integer (usually identified as *thetaLong* in the code), where the maximum positive value (*Long.MAX_VALUE*) is interpreted as the double 1.0. In this document we will only refer to the mathematical quantity *theta*.

* *retained entries* or *count*: This is the number of hash values currently retained in the sketch. It can never be less than zero.

* *empty*: 
    * By definition, if *empty = true*, the number of *retained entries* must be zero. The value of *theta* is irrelevant in this case, and can be assumed to be 1.0.
    * If *empty* = false, the *retained entries* can be zero or greater than zero, and *theta* can be 1.0 or less than 1.0.

We have developed a shorthand notation for these three variables to record their state as *{theta, retained entries, empty}*. 
When analyzing the corner cases of the set operations, we only need to know whether *theta* is 1.0 or less than 1.0, *retained entries* is zero or greater than zero, and *empty* is true or false. These are further abbreviated as

* *theta* can be *1.0* or *<1.0*
* *retained entries* can be either *0* or *>0*
* *empty* can be either *T* or *F*

Each of the above three states can be represented as a boolean variable. 
Thus, there are 8 possible combinations of the three variables.

---

<sup>1</sup> Anirban Dasgupta, Kevin J. Lang, Lee Rhodes, and Justin Thaler. A framework for estimating stream expression cardinalities. In *EDBT/ICDT Proceedings 2016*, pages 6:1–6:17, 2016.

## Valid States of a Sketch

Of the eight possible combinations of the three boolean variables and using the above notation, there are four valid states of a *Theta* sketch.

### Empty{1.0, 0, T}
When a new sketch is created, *theta* is set to 1.0, *retained entries* is set to zero, and *empty* is true. 
This state can also occur as the result of a set operation, where the operation creates a new sketch to potentially load result data into the sketch but there is no data to load into the sketch. 
So it effectively returns a new empty sketch that has been untouched and unaffected by the input arguments to the set operation.

### Exact{1.0, >0, F}
All of the *Theta* sketches have an internal buffer that is effectively a list of hash values of the items received by the sketch. 
If the number of distinct input items does not exceed the size of that buffer, the sketch is in *exact* mode. 
There is no probabilistic estimation involved so *theta = 1.0*, which indicates that all distinct values are in the buffer. 
*retained entries* is the count of those values in the buffer, and the sketch is not *empty*.

### Estimation{<1.0, >0, F}
Here, the number of distinct inputs to the sketch have exceeded the size of the buffer, so the sketch must start choosing what values to retain in the sketch and starts reducing the value of *theta* accordingly. *theta < 1.0*, *retained entries > 0*, and *empty = F*.

### Degenerate{<1.0, 0, F}<sup>2</sup>
This requires some explanation. 

Imagine we have two large data sets, A and B, with only a few items in common. 
The exact intersection of these two sets, *A&cap;B* would result in those few common items.

Now suppose we compute Sketch(A) and Sketch(B). 
Because sketches are approximate and the items from each set are chosen at random, there is some probability that one of the sketches may not contain any of the common items. 
As a result, the sketch intersection of these two sets, *Sketch(A)&cap;Sketch(B)*, which is also approximate, might contain zero retained entries. 
Even though the retained entries are zero, the upper bound of the estimated number of distinct values from the input domain is clearly greater than zero, but missed by the sketch intersection. 
This upper bound can be computed statistically. 
It is too complex to discuss further here, but the sketch code actually performs this estimation.

Where both input sketches are non-empty, there is a non-zero probability that the intersection will have zero entries, yet the statistics tell us that the result may
not be really empty, we may have been just unlucky.  
We indicate this by setting the result *empty = F*, and *retained entries = 0*. 
The resulting *theta = min(thetaA, thetaB)*. 
Calling *getUpperBound(...)* on the resulting intersection will reveal the best estimate of how many values might exist in the intersection of the raw data. 
The *getLowerBound(...)* will be zero because it is also possible that the two sets, A and B, were exactly disjoint.

---

<sup>2</sup>Note that this degenerate state can also result from an AnotB operation or the Union operation, which will be discussed below.

### Summary Table of the Valid States of a Sketch
The *Has Seen Data* column is not an independent variable, but helps with the interpretation of the state.

We can assign a single octal digit ID to each state where

* *theta = 1.0 := 4, else 0*
* *retained entries >0 := 2, else 0*
* *empty = true := 1, else 0* 

The octal digit `ID = ((theta == 1.0) ? 4 : 0) | ((retainedEntries > 0) ? 2 : 0) | (empty ? 1 : 0);`

| Shorthand<br>Notation                | Theta | Retained<br>Entries |    Empty   | Has Seen<br>Data | ID | Comments                                          |
|:------------------------------------:|:-----:|:-------------------:|:----------:|:----------------:|:--:|:--------------------------------------------------|
| Empty<br>{1.0,0,T}                   |  1.0  |         0           |     T      |       F          |  5 | Empty Sketch                                      |
| Exact<br>{1.0,>0,F}                  |  1.0  |        >0           |     F      |       T          |  6 | Exact Mode Sketch                                 |
| Estimation<br>{<1.0,>0,F}            | <1.0  |        >0           |     F      |       T          |  2 | Estimation Mode Sketch                            |
| Degenerate<br>{<1.0,0,F}<sup>3</sup> | <1.0  |         0           |     F      |       T          |  0 | Degenerate and valid<br>Intersect or AnotB result |

---

<sup>3</sup> *Degenerate*: This can occur as an estimating result of a an Intersection of two disjoint sets, 
an AnotB of two identical sets, or the Union of two *Degenerate* sets.

## Invalid States of a Sketch
The remaining four combinations of the variables are invalid and should not occur.

The *Has Seen Data* column is not an independent variable, but helps with the interpretation of the state.

| Theta | Retained<br>Entries | Empty<br>Flag | Has Seen<br>Data | Comments                                                                                       |
|:-----:|:-------------------:|:-------------:|:----------------:|:-----------------------------------------------------------------------------------------------|
|  1.0  |        0            |      F        |       T          | If it has seen data, Empty = F.<sup>4</sup> <br>&there4; Theta cannot be = 1.0 AND Entries = 0 |
|  1.0  |       >0            |      T        |       F          | If it has not seen data, Empty = T. <br>&there4; Entries cannot be > 0                         |
| <1.0  |       >0            |      T        |       F          | If it has not seen data, Empty = T. <br>&there4; Theta cannot be < 1.0 OR Entries > 0          |
| <1.0  |        0            |      T        |       F          | If it has not seen data, Empty = T.<sup>5</sup> <br>&there4; Theta cannot be < 1.0             |
---
<sup>4</sup>This can occur internally as the result from an intersection of two exact, disjoint sets, or AnotB of two exact, identical sets.
There is no probability distribution, so this is converted internally to EMPTY {1.0, 0, T}. A Union cannot produce this result.

<sup>5</sup>This can occur internally as the initial state of an UpdateSketch if p was set to less than 1.0 by the user and the sketch has not seen any data.
There is no probability distribution because the sketch has not been offered any data, so this is converted internally to EMPTY {1.0, 0, T}.

## State Combinations of Two Sketches and Set Operation Results
Each sketch can have four valid states, which means we can have 16 combinations of states of two sketches as expanded in the following table.
 
| Sketch A<br>State         | Sketch B<br>State         | Pair<br>ID | Intersection<br>Action         | AnotB<br>Action                | Union<br>Action                | Action IDs  |
|:-------------------------:|:-------------------------:|:----------:|:------------------------------:|:------------------------------:|:------------------------------:|:-----------:|
| Empty<br>{1.0,0,T}        | Empty<br>{1.0,0,T}        | 55         | Empty<br>{1.0,0,T}=A=B         | Empty<br>{1.0,0,T}=A           | Empty<br>{1.0,0,T}=A=B         | E,E,E       |
| Empty<br>{1.0,0,T}        | Exact<br>{1.0,>0,F}       | 56         | Empty<br>{1.0,0,T}=A           | Empty<br>{1.0,0,T}=A           | Sketch B                       | E,E,B       |
| Empty<br>{1.0,0,T}        | Estimation<br>{<1.0,>0,F} | 52         | Empty<br>{1.0,0,T}=A           | Empty<br>{1.0,0,T}=A           | Sketch B                       | E,E,B       |
| Empty<br>{1.0,0,T}        | Degenerate<br>{<1.0,0,F}  | 50         | Empty<br>{1.0,0,T}=A           | Empty<br>{1.0,0,T}=A           | Degenerate<br>{ThetaB,0,F}=B   | E,E,DB      |
| Exact<br>{1.0,>0,F}       | Empty<br>{1.0,0,T}        | 65         | Empty<br>{1.0,0,T}=B           | Sketch A                       | Sketch A                       | E,A,A       |
| Exact<br>{1.0,>0,F}       | Exact<br>{1.0,>0,F}       | 66         | Full Intersect                 | Full AnotB                     | Full Union                     | I,N,U       |
| Exact<br>{1.0,>0,F}       | Estimation<br>{<1.0,>0,F} | 62         | Full Intersect                 | Full AnotB                     | Full Union                     | I,N,U       |
| Exact<br>{1.0,>0,F}       | Degenerate<br>{<1.0,0,F}  | 60         | Degenerate<br>{ThetaB,0,F}=B   | Trim A<br>by minTheta          | Trim A<br>by minTheta          | D,TA,TA     |
| Estimation<br>{<1.0,>0,F} | Empty<br>{1.0,0,T}        | 25         | Empty<br>{1.0,0,T}=B           | Sketch A                       | Sketch A                       | E,A,A       |
| Estimation<br>{<1.0,>0,F} | Exact<br>{1.0,>0,F}       | 26         | Full Intersect                 | Full AnotB                     | Full Union                     | I,N,U       |
| Estimation<br>{<1.0,>0,F} | Estimation<br>{<1.0,>0,F} | 22         | Full Intersect                 | Full AnotB                     | Full Union                     | I,N,U       |
| Estimation<br>{<1.0,>0,F} | Degenerate<br>{<1.0,0,F}  | 20         | Degenerate<br>{minTheta,0,F}   | Trim A<br>by minTheta          | Trim A<br>by minTheta          | D,TA,TA     |
| Degenerate<br>{<1.0,0,F}  | Empty<br>{1.0,0,T}        | 05         | Empty<br>{1.0,0,T}=B           | Degenerate<br>{ThetaA,0,F}=A   | Degenerate<br>{ThetaA,0,F}=A   | E,DA,DA     |
| Degenerate<br>{<1.0,0,F}  | Exact<br>{1.0,>0,F}       | 06         | Degenerate<br>{ThetaA,0,F}=A   | Degenerate<br>{ThetaA,0,F}=A   | Trim B<br>by minTheta          | DA,DA,TB    |
| Degenerate<br>{<1.0,0,F}  | Estimation<br>{<1.0,>0,F} | 02         | Degenerate<br>{minTheta,0,F}   | Degenerate<br>{minTheta,0,F}   | Trim B<br>by minTheta          | D,D,TB      |
| Degenerate<br>{<1.0,0,F}  | Degenerate<br>{<1.0,0,F}  | 00         | Degenerate<br>{minTheta,0,F}   | Degenerate<br>{minTheta,0,F}   | Degenerate<br>{minTheta,0,F}   | D,D,D       |

**Column Descriptions:**

* Pair ID: two octal digits, the first digit represents the state of Sketch A, the second digit represents the state of Sketch B.
* Sketch A State
* Sketch B State
* Intersection Action
* AnotB Action
* Union Action
* Action Codes: Intersection, AnotB, Union. 

The action IDs are given by the following table along with description and where used:

| Action ID | Action<br>Description                    | Intersection | AnotB    | Union    |
|:---------:|:----------------------------------------:|:------------:|:--------:|:--------:|
|   A       | Sketch A                                 |              | &#10004; | &#10004; |
|   TA      | Trim Sketch A<br>by minTheta             |              | &#10004; | &#10004; |
|   B       | Sketch B                                 |              |          | &#10004; |
|   TB      | Trim Sketch B<br>by minTheta             |              |          | &#10004; |
|   D       | Degenerate<br>{minTheta,0,F}             | &#10004;     | &#10004; | &#10004; |
|   DA      | Degenerate<br>{ThetaA,0,F}<br>(optional) |              |          | &#10004; |
|   DB      | Degenerate<br>{ThetaB,0,F}<br>(optional) |              |          | &#10004; |
|   E       | Empty<br>{1.0,0,T}                       | &#10004;     | &#10004; | &#10004; |
|   I       | Full Intersect                           | &#10004;     |          |          |
|   N       | Full AnotB                               |              | &#10004; |          |
|   U       | Full Union                               |              |          | &#10004; |


Note that the results of *Full Intersect*, *Full AnotB*, or *Full Union* actions will require further interpretation of the resulting state. For example:

* If the resulting sketch is *{1.0,0,?}*, then an *Empty{1.0,0,T}* is returned. 
* If the resulting sketch is *{<1.0,0,?}* then a *Degenerate{<1.0,0,F}* is returned.  
* Otherwise, the sketch returned will be an estimating *{minTheta, >0, F}*, or exact *{1.0, >0, F}*.

## Testing
The above information is encoded as a model into the special class 
*[org.apache.datasketches.SetOperationsCornerCases](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.SetOperationCornerCases.java)*. 
This class is made up of enums and static methods to quickly determine for a sketch what actions to take based on the state of the input arguments. 
This model is independent of the implementation of the Theta Sketch, whether the set operation is performed as a Theta Sketch, or a Tuple Sketch and when translated can be used in other languages as well.  

Before this model was put to use an extensive set of tests was designed to test any potential implementation against this model. 
These tests are slightly different for the Tuple Sketch than the Theta Sketch because the Tuple Sketch has more combinations to test, but the model is the same.

The tests for the Theta Sketch can be found in the class 
*[org.apache.datasketches.theta.CornerCaseThetaSetOperationsTest](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.theta.CornerCaseThetaSetOperationsTest.java)*

The tests for the Tuple Sketch can be found in the class 
*[org.apache.datasketches.tuple.aninteger.CornerCaseTupleSetOperationsTest](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.tuple.aninteger.CornerCaseTupleSetOperationsTest.java)*

The details of how this model is used in run-time code can be found in the class *[org.apache.datasketches.tuple.AnotB.java](https://github.com/apache/datasketches-java/blob/master/src/main/java/org.apache.datasketches.tuple.AnotB.java)*.


