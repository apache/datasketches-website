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
# Sketching Quantiles and Ranks Tutorial
Streaming quantiles algorithms, or quantiles sketches, enable us to analyze the distributions 
of massive data very quickly using only a small amount of space.  
They allow us to compute quantile values given a desired rank, or compute a rank given
a quantile value. Quantile sketches enable us to plot the CDF, PMF or histograms of a distribution. 

The goal of this short tutorial it to introduce the reader to some of the basic concepts 
of quantiles, ranks and their functions.

## What is a rank?

### A ***rank*** identifies the numeric position of a specific value in an enumerated, ordered set of values.

The actual enumeration can be done in several ways, but for our use here we will define the two common ways that *rank* can be specified and that we will use. 

* The **natural rank** is a **natural number** from the set of one-based, natural numbers, &#8469;<sub>1</sub>, and is derived by enumerating an ordered set of values, starting with the value 1, up to *n*, the number of values in the original set.

* The ***normalized rank*** is a number between 0.0 and 1.0 computed by dividing the *natural rank* by the total number of values in the set, *n*. Thus, for finite sets, any *normalized rank* is in the range (0, 1]. Normalized ranks are often written as a percent. 
For example, the value associated with the normalized rank of 0.5 is the median of the distribution and is also the value at the 50th percentile of the distribution. 
* A rank of 0, whether natural or normalized, represents the empty set.
 
In our sketch library APIs and documentation, when we refer to *rank*, we imply *normalized rank*, because *normalized ranks* can be specified independent of *n*. However, in this tutorial, we will sometimes use *natural ranks* to simplify the examples.

### Rank and Mass

*Normalized rank* is closely associated with the concept of *mass*. The value associated with the *normalized rank* 0.5 represents the median value, or the center of *mass* of the entire set, where half of the values are below the median and half are above. The concept of mass is important to understanding the Probability Mass Function (PMF) offered by most of the quantile sketches in the library.

## What is a quantile?

### A ***quantile*** is a ***value*** that is associated with a particular ***rank***. 

When an ordered set of values is associated with their ranks it becomes enumerated and we refer to the set of values as *quantiles*.  In this context we can think of a quantile and its rank as a pair *{quantile, rank}* and an ordered set of these pairs as a *cumulative distribution*, or CD.  When we add the capability to search to this CD, it becomes a *cumulative distribution function*, or CDF. We can query this CDF by rank to find its associated quantile, *q = Q(r)*, or we can query this CDF by quantile to find its associated rank, *r = R(q)*.

*Quantile* is the general term that includes other terms that are also quantiles.
To wit:

* A percentile is a quantile where the rank domain is divided into hundredths. For example, "An SAT Math score of 740 is at the 95th percentile". The score of 740 is the quantile and .95 is the normalized rank.
* A decile is a quantile where the rank domain is divided into tenths. For example, "An SAT Math score of 690 is at the 9th decile (rank = 0.9).
* A quartile is a quantile where the rank domain is divided into fourths. For example, "An SAT Math score of 600 is at the third quartile (rank = 0.75).
* The median is a quantile that splits the rank domain in half. For example, "An SAT Math score of 520 is at the median (rank = 0.5).

## The simple quantile and rank functions
Let's examine the following table:

| Quantile:       | 10  | 20  | 30  | 40  | 50  |
| --------------- | --- | --- | --- | --- | --- |
| Natural Rank    | 1   | 2   | 3   | 4   | 5   |
| Normalized Rank | .2  | .4  | .6  | .8  | 1.0 |

### Note: 
The term "value" can be ambiguous because items that we stream into a sketch are values and numeric ranks are also values.  To avoid this ambiguity, we will use the term "quantiles" to refer to values that are streamed into a sketch even before they have been associated with a rank.  

Let's define the simple functions

### ***quantile(rank)*** or ***Q(r)*** := return the quantile ***q*** associated with a given ***rank, r***.

### ***rank(quantile)*** or ***R(q)*** := return the rank ***r*** associated with a given ***quantile, q***.  

Using an example from the table:

* Using natural ranks:
  * *Q(3) = 30*
  * *R(30) = 3*
* Using normalized ranks:
  * *Q(.6) = 30*
  * *R(30) = .6*


Because of the close, two-way relationship of quantiles and ranks,  
*R(q)* and *Q(r)* form a *1:1 functional pair* if, and only if

* *q = Q(R(q))*
* *r = R(Q(r))*

And this is certainly true of the table above.


## NOTE The following part of this tutorial only applies to the Classic, KLL, and REQ quantile sketches. The T-Digest is an entirely different type of sketch.

## The challenge of duplicates
With real data we often encounter duplicate quantiles in the stream. Let's examine this next table.

| Quantile:    | 10  | 20  | 20  | 20  | 50  |
| ------------ | --- | --- | --- | --- | --- |
| Natural Rank | 1   | 2   | 3   | 4   | 5   |

As you can see *Q(r)* is straightforward. But how about *R(q)*?  Which of the ranks 2, 3, or 4 should the function return, given the quantile 20?  Given this data, and our definitions so far, the function *R(q)* is ambiguous. We will see how to resolve this shortly.
 

## The challenge of approximation
By definition, sketching algorithms are approximate, and they achieve their high performance by discarding data.  Suppose you feed *n* quantiles into a sketch that retains only *m < n* quantiles. This means *n-m* quantiles were discarded.  The sketch must track the quantity *n* used for computing the rank and quantile functions. When the sketch reconstructs the relationship between ranks and quantiles, *n-m* quantiles are missing creating holes in the ordered sequence. For example, examine the following tables.

The raw data might look like this, with its associated natural ranks.

| Quantile:    | 10  | 20  | 30  | 40  | 50  | 60  | 70  | 80  | 90  | 100 |
| ------------ | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Natural Rank | 1   | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   | 10  |

The sketch might discard the even numbered quantiles producing something like this:

| Quantile:    | 10  | 30  | 50  | 70  | 90  |
| ------------ | --- | --- | --- | --- | --- |
| Natural Rank | 2   | 4   | 6   | 8   | 10  |

When the sketch deletes quantiles it adjusts the associated ranks by effectively increasing the "weight" of retained quantiles so that they are approximately positionally correct and the top natural rank corresponds to *n*.

### NOTE: With the DataSketches Java library 6.0.0 and after, the min and max quantiles of the stream are reinserted in to the Sorted View representation of the stream if the sketch has deleted them.  Therefore, the second table above would look something like this:

| Quantile:    | 10  | 30  | 50  | 70  | 90  | 100 |
| ------------ | --- | --- | --- | --- | --- | --- |
| Natural Rank | 2   | 4   | 6   | 8   | 9   | 10  |

How do we resolve *Q(3)* or *R(20)*?

## The need for inequality search
The quantile sketch algorithms discussed in the literature primarily differ by how they choose which quantiles in the stream should be discarded. After the elimination process, all of the quantiles sketch implementations are left with the challenge of how to reconstruct the actual distribution of the stream, approximately and with good accuracy. 

Given the presence of duplicates and absence of values from the stream we must redefine the above quantile and rank functions as inequalities while retaining the properties of 1:1 functions as closely as possible.

One can find examples of the following definitions in the research literature.  All of our library quantile sketches allow the user to choose the searching criteria.  

These next examples use a small data set that mimics what could be the result of both input duplication and sketch data deletion.

## The rules for returned quantiles or ranks for Classic, KLL, and REQ sketches

* **Rule 1:** Every quantile that exists in the input stream or retained by the sketch has an associated rank when sorted.

* **Rule 2:** These quantile sketches retain and return only quantiles that exist in the actual input stream of quantiles. In other words, these quantile sketches do not retain or return interpolations beteen quantiles from the input stream.  (The T-Digest sketch does do interpolations.)   

* **Rule 3:** For the *getRank(quantile)* queries, all of our quantile sketches only return ranks that are associated with quantiles retained by the sketch. In other words, these sketches do not interpolate between ranks.

* **Rule 4:** The quantile algorithms of these sketches compensate for quantiles removed during the sketch compacton process by increasing the weights of the quantiles not selected for removal, such that:

     * The sum of the individual item weights of all quantiles retained by the sketch equals **n**, the total count of all quantiles given to the sketch.
     * And by corollary, the largest quantile, when sorted by cumulative rank, has a cumulative natural rank of **n**, or equivalently, a cumulative normalized rank of **1.0**.


## The rank functions with inequalities

### ***rank(quantile, INCLUSIVE)*** or ***R(q, LE)*** :=<br>Given *q*, return the rank, *r*, of the largest quantile that is Less Than or Equal To *q*.

*Implementation: Normal Rule*

* Given *q*, search the quantile array until we find the adjacent pair *{q1, q2}* where *q1 <= q < q2*. 
* Return the rank, *r*, associated with *q1*, the first of the pair. 

*Boundary Exceptions:*

* **Boundary Rule RI-1:** If the given *q* is *>=* the quantile associated with the largest cumulative rank retained by the sketch, the function will return the largest cumulative rank, *1.0*.
* **Boundary Rule RI-2:** If the given *q* is *<* the quantile associated with the smallest cumulative rank retained by the sketch, the function will return a rank of *0.0*. There is no quantile that is <= *q*. Returning *0.0* is essentially the same as *Empty* or *null*.

*Examples using normalized ranks.  The given value is underlined.*

* *r(_10_) = .071* Normal rule applies: *10 <= _10_ < 20*, return *r(q1) = .071*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |
| Quantile input     | _10_ |      |      |      |      |      |      |       |
| Qualifying pair    |  q1  |  q2  |      |      |      |      |      |       |
| Rank result        |      |      |      |      |      |      |      |       |

* *r(_30_) = .786* Normal rule applies: *30 <= _30_ < 40*, return *r(q1) = .786*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |
| Quantile input     |      |      |      |      |      | _30_ |      |       |
| Qualifying pair    |      |      |      |      |      | q1   | q2   |       |
| Rank result        |      |      |      |      |      | .786 |      |       |

* *r(_50_) = 1.0* Use Boundary Rule RI-1: *50 <= _50_*, return *1.0*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |   ?   |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |       |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |       |
| Quantile input     |      |      |      |      |      |      |      | _50_  |       |
| Qualifying pair    |      |      |      |      |      |      |      |  q1   | (q2)  |
| Rank result        |      |      |      |      |      |      |      | 1.0   |       |

* *r(_55_) = 1.0* Use Boundary Rule RI-1: *50 <= _55_*, return *1.0*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |   ?   |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |       |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |       |
| Quantile input     |      |      |      |      |      |      |      | _55_  |       |
| Qualifying pair    |      |      |      |      |      |      |      |  q1   | (q2)  |
| Rank result        |      |      |      |      |      |      |      | 1.0   |       |


* *r(_5_) = 0.0* Use Boundary Rule RI-2: *_5_ < 10*, return *0.0*.

| Quantile[]:        |   ?  | 10   | 20   | 20   | 30   | 30   | 30   | 40    | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    |      | 1    | 3    | 5    | 7    | 9    | 11   | 13    | 14    |
| Normalized Rank[]: |      | .071 | .214 | .357 | .500 | .643 | .786 | .929  | 1.0   |
| Quantile input     |  _5_ |      |      |      |      |      |      |       |       |
| Qualifying pair    | (q1) |  q2  |      |      |      |      |      |       |       |
| Rank result        |   0  |      |      |      |      |      |      |       |       |


--------

### ***rank(quantile, EXCLUSIVE)*** or ***R(q, LT)*** :=<br>Given *q*, return the rank, *r*, of the largest quantile that is strictly Less Than *q*.  


*Implementation: Normal Rule*

* Given *q*, search the quantile array until we find the adjacent pair *{q1, q2}* where *q1 < q <= q2*. 
* Return the rank, *r*, associated with *q1*, the first of the pair.

*Boundary Exceptions:*

* **Boundary Rule RE-1:** If the given *q* is *>* the quantile associated with the largest cumulative rank retained by the sketch, the sketch will return the the largest cumulative rank, *1.0*.
* **Boundary Rule RE-2:** If the given *q* is *<=* the quantile associated with the smallest cumulative rank retained by the sketch, the sketch will return a rank of *0.0*.

*Examples using normalized ranks.  The given value is underlined.*

* *r(_30_) = .357* Normal rule applies: *20 < _30_ <= 30*, return *r(q1) = .357*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Quantile input     |      |      | _30_ |      |      |      |      |       |
| Qualifying pair    |      |      | q1   | q2   |      |      |      |       |
| Rank result        |      |      | .357 |      |      |      |      |       |

* *r(_50_) = .929*  Normal rule applies: *40 < _50_ <= 50*, return *r(q1) = .929*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |
| Quantile input     |      |      |      |      |      |      | _55_ |       |
| Qualifying pair    |      |      |      |      |      |      |  q1  | q2    |
| Rank result        |      |      |      |      |      |      | .929 |       |

* *r(_55_) = 1.0* Use Boundary Rule RE-1: *50 < _55_*, return *1.0*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |   ?   |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |       |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |       |
| Quantile input     |      |      |      |      |      |      |      | _55_  |       |
| Qualifying pair    |      |      |      |      |      |      |      |  q1   | (q2)  |
| Rank result        |      |      |      |      |      |      |      | 1.000 |       |

* *r(_10_) = 0.0* Use Boundary Rule RE-2: *_10_ <= 10*, return *0*.

| Quantile[]:        |   ?  | 10   | 20   | 20   | 30   | 30   | 30   | 40    | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    |      | 1    | 3    | 5    | 7    | 9    | 11   | 13    | 14    |
| Normalized Rank[]: |      | .071 | .214 | .357 | .500 | .643 | .786 | .929  | 1.0   |
| Quantile input     | _0.0_|      |      |      |      |      |      |       |       |
| Qualifying pair    | (q1) |  q2  |      |      |      |      |      |       |       |
| Rank result        |   0  |      |      |      |      |      |      |       |       |

* *r(_5_) = 0.0* Use Boundary Rule RE-2: *_5_ <= 10*, return *0*.

| Quantile[]:        |   ?  | 10   | 20   | 20   | 30   | 30   | 30   | 40    | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    |      | 1    | 3    | 5    | 7    | 9    | 11   | 13    | 14    |
| Normalized Rank[]: |      | .071 | .214 | .357 | .500 | .643 | .786 | .929  | 1.0   |
| Quantile input     |  _5_ |      |      |      |      |      |      |       |       |
| Qualifying pair    | (q1) |  q2  |      |      |      |      |      |       |       |
| Rank result        |   0  |      |      |      |      |      |      |       |       |



## The quantile functions with inequalities

### ***quantile(rank, INCLUSIVE)*** or ***Q(r, GE)*** :=<br>Given *r*, return the quantile, *q*, of the smallest rank that is strictly Greater Than or Equal To *r*.

*Implementation: Normal Rule*

* Given *r*, search the rank array until we find the adjacent pair *{r1, r2}* where *r1 < r <= r2*. 
* Return the quantile, *q*, associated with *r2*, the second of the pair.

*Boundary Exceptions:*

* **Boundary Rule QI-1:** If the given normalized rank, *r*, is *<=* the smallest rank, the function will return the **quantile** associated with the smallest cumulative rank.

*Examples using normalized ranks.  The given value is underlined.*

* *q(_.786_) = 30* Normal rule applies: *.643 < _.786_ <= .786*, return *q(r2) = 30*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Rank input         |      |      |      |      |      |_.786_|      |       |
| Qualifying pair    |      |      |      |      | r1   | r2   |      |       |
| Quantile result    |      |      |      |      |      | 30   |      |       |

* *q(_1.0_) = 50* Normal rule applies: *.929 < _1.0_ <= 1.0*, return *q(r2) = 50*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |
| Rank input         |      |      |      |      |      |      |      | _1.0_ |
| Qualifying pair    |      |      |      |      |      |      |  r1  |  r2   |
| Quantile result    |      |      |      |      |      |      |      | 50    |

* *q(_0.0_ <= .071) = 10* Use Boundary Rule QI-1: *_0.0_ <= .071*, return *10*.

| Quantile[]:        |   ?  | 10   | 20   | 20   | 30   | 30   | 30   | 40    | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    |      | 1    | 3    | 5    | 7    | 9    | 11   | 13    | 14    |
| Normalized Rank[]: |      | .071 | .214 | .357 | .500 | .643 | .786 | .929  | 1.0   |
| Rank input         | _0.0_|      |      |      |      |      |      |       |       |
| Qualifying pair    | (r1) |  r2  |      |      |      |      |      |       |       |
| Rank result        |      | 10   |      |      |      |      |      |       |       |


--------

### ***quantile(rank, EXCLUSIVE)*** or ***Q(r, GT)*** :=<br>Given *r*, return the quantile, *q*, of the smallest rank that is strictly Greater Than *r*.

*Implementation: Normal Rule*

* Given *r*, search the rank array until we find the adjacent pair *{r1, r2}* where *r1 <= r < r2*. 
* Return the quantile, *q*, associated with *r2*, the second of the pair.

*Boundary Exceptions:*

* **Boundary Rule QE-1:** If the given normalized rank, *r*, is equal to 1.0, there is no rank greater than 1.0, nor a corresponding quantile that satisfies this criterion. However, for convenience, the function will return the maximum item of the input stream.


*Examples using normalized ranks.  The given value is underlined.*

* *q(_.357_) = 30* Normal rule applies: *.357 <= _.357_ < .500*, return *Q(r2) = 30*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Rank input         |      |      |_.357_|      |      |      |      |       |
| Qualifying pair    |      |      | r1   | r2   |      |      |      |       |
| Quantile result    |      |      |      | 30   |      |      |      |       |

* *q(_1.0_) = 50* Use Boundary Rule QE-1: *_1.0_ = 1.0*, return *50*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |   ?   |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |       |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |       |
| Rank input         |      |      |      |      |      |      |      | _1.0_ |       |
| Qualifying pair    |      |      |      |      |      |      |      |  r1   | (r2)  |
| Quantile result    |      |      |      |      |      |      |      |       |  50   |

* *q(_0.99_) = 50* Normal rule applies: *.929 <= _.99_ < 1.0*, return *Q(r2) = 50*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |
| Rank input         |      |      |      |      |      |      | _.99_|       |
| Qualifying pair    |      |      |      |      |      |      |  r1  | r2    |
| Quantile result    |      |      |      |      |      |      |      | 50    |


* *q(_0.0_) = 10* Normal rule applies: *0.0 <= _0.0_ < .071*, return *10*.

| Quantile[]:        |   ?  | 10   | 20   | 20   | 30   | 30   | 30   | 40    | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    |      | 1    | 3    | 5    | 7    | 9    | 11   | 13    | 14    |
| Normalized Rank[]: |      | .071 | .214 | .357 | .500 | .643 | .786 | .929  | 1.0   |
| Rank input         | _0.0_|      |      |      |      |      |      |       |       |
| Qualifying pair    | (r1) |  r2  |      |      |      |      |      |       |       |
| Rank result        |      | 10   |      |      |      |      |      |       |       |


--------

### ***quantile(rank, EXCLUSIVE_STRICT)*** or ***q(r, GT_STRICT)*** :=<br>Given *r*, return the quantile, *q*, of the smallest rank that is strictly Greater Than *r*.

### Note: This rule is marginal in its usefulness so it is not currently implemented. 

*Implementation: Normal Rule*

* Given *r*, search the rank array until we find the adjacent pair *{r1, r2}* where *r1 <= r < r2*. 
* Return the quantile, *q*, associated with *r2*, the second of the pair.

*Boundary Exceptions:*

* **Boundary Rule QES-1:** If the given normalized rank, *r*, is equal to *1.0*, there is no quantile that satisfies this criterion. Return *NaN* or *null*.

*Examples using normalized ranks.  The given value is underlined.*

*There is only one case where this strict case would apply:*

* *q(_1.0_) = Nan or null* Use Boundary Rule QES-1: *_1.0_ = 1.0*, return *NaN or null*.

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |   ?   |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |       |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.0   |       |
| Rank input         |      |      |      |      |      |      |      | _1.0_ |       |
| Qualifying pair    |      |      |      |      |      |      |      |  r1   | (r2)  |
| Quantile result    |      |      |      |      |      |      |      |       |  NaN or null |


## These inequality functions maintain the 1:1 functional relationship, approximately. 

### The *exclusive* search for Q(r) is the inverse of the *exclusive* search for R(q). 

##### Therefore, *q = q(r(q))* and *r = r(q(r))*.

### The *inclusive* search for Q(r) is the inverse of the *inclusive* search for R(q). 

##### Therefore, *q = q(r(q))* and *r = r(q(r))*.


## Summary
The power of these inequality search algorithms is that they produce repeatable and accurate results, are insensitive to duplicates and sketch deletions, and maintain the property of 1:1 functions approximately.
