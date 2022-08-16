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

* The **natural rank** is a **natural number** from the set of one-based, natural numbers, &#8469;<sub>1</sub>, and is derived by enumerating an ordered set of values, starting with the value 1, up to *n*, the number of values in the set.

* The ***normalized rank*** is a number between 0.0 and 1.0 computed by dividing the *natural rank* by the total number of values in the set, *n*. Thus, for finite sets, any *normalized rank* is in the range (0, 1]. Normalized ranks are often written as a percent. But don't confuse percent with percentile! This will be explained below.
* A rank of 0, whether natural or normalized, represents the empty set.
 
In our sketch library and documentation, when we refer to *rank*, we imply *normalized rank*. However, in this tutorial, we will sometimes use *natural ranks* to simplify the examples.

### Rank and Mass

*Normalized rank* is closely associated with the concept of *mass*. The value associated with the rank 0.5 represents the median value, or the center of *mass* of the entire set, where half of the values are below the median and half are above. The concept of mass is important to understanding the Probability Mass Function (PMF) offered by all the quantile sketches in the library.

## What is a quantile?

### A ***quantile*** is a ***value*** that is associated with a particular ***rank***. 

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

Let's define the simple functions

### ***quantile(rank)*** or ***q(r)*** := return the quantile value ***q*** associated with a given ***rank, r***.

### ***rank(quantile)*** or ***r(q)*** := return the rank ***r*** associated with a given ***quantile, q***.  

Using an example from the table:

* Using natural ranks:
  * *q(3) = 30*
  * *r(30) = 3*
* Using normalized ranks:
  * *q(.6) = 30*
  * *r(30) = .6*


Because of the close, two-way relationship of quantiles and ranks,  
*r(q)* and *q(r)* form a *1:1 functional pair* if, and only if

* *q = q(r(q))*
* *r = r(q(r))*

And this is certainly true of the table above.

## The challenge of duplicates
With real data we often encounter duplicate values in the stream. Let's examine this next table.

| Quantile:    | 10  | 20  | 20  | 20  | 50  |
| ------------ | --- | --- | --- | --- | --- |
| Natural Rank | 1   | 2   | 3   | 4   | 5   |

As you can see *q(r)* is straightforward. But how about *r(q)*?  Which of the rank values 2, 3, or 4 should the function return given the value 20?  Given this data, and our definitions so far, 
the function *r(q)* is ambiguous. We will see how to resolve this shortly.
 

## The challenge of approximation
By definition, sketching algorithms are approximate, and they achieve their high performance by discarding  data.  Suppose you feed *n* items into a sketch that retains only *m < n* items. This means *n-m* values were discarded.  The sketch must track the value *n* used for computing the rank and quantile functions. When the sketch reconstructs the relationship between ranks and values *n-m* rank values are missing creating holes in the sequence of ranks. For example, examine the following tables.

The raw data might look like this, with its associated natural ranks.

| Quantile:    | 10  | 20  | 30  | 40  | 50  | 60  | 70  | 80  | 90  | 100 |
| ------------ | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Natural Rank | 1   | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   | 10  |

The sketch might discard the even values producing something like this:

| Quantile:    | 10  | 30  | 50  | 70  | 90  |
| ------------ | --- | --- | --- | --- | --- |
| Natural Rank | 2   | 4   | 6   | 8   | 10  |

When the sketch deletes values it adjusts the associated ranks by effectively increasing the "weight" of adjacent items so that they are positionally approximately correct and the top rank corresponds to *n*.

How do we resolve *q(3)* or *r(20)*?

## The need for inequality search
The quantile sketch algorithms discussed in the literature primarily differ by how they choose which values in the stream should be discarded. After the elimination process, all of the quantiles sketch implementations are left with the challenge of how to reconstruct the actual distribution, approximately and with good accuracy. 

Given the presence of duplicates and absence of values from the stream we must redefine the above quantile and rank functions as inequalities **while retaining the properties of 1:1 functions.**

One can find examples of the following definitions in the research literature.  All of our library quantile sketches allow the user to choose the searching criteria.  

These next examples use a small data set that mimics what could be the result of both duplication and sketch data deletion.

## The rank functions with inequalities

### ***rank(quantile, EXCLUSIVE)*** or ***r(q, LT)*** :=<br>Given *q*, return the rank, *r*, of the largest quantile that is strictly *Less Than* *q*.  


<b>Implementation:</b>
Given *q*, search the quantile array until we find the adjacent pair *{q1, q2}* where *q1 < q <= q2*. Return the rank, *r*, associated with *q1*, the first of the pair.

<b>Boundary Notes:</b>

* If the given *q* is larger than the largest quantile retained by the sketch, the sketch will return the rank of the largest retained quantile.
* If the given *q* is smaller than the smallest quantile retained by the sketch, the sketch will return a rank of zero.

<b>Examples using normalized ranks:</b>

* *r(55) = 1.0* 
* *r(5) = 0.0*
* *r(30) = .357* (Illustrated in table)

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Quantile input     |      |      |      | 30   | 30   | 30   |      |       |
| Qualifying pair    |      |      | q1   | q2   |      |      |      |       |
| Rank result        |      |      | .357 |      |      |      |      |       |

--------

### ***rank(quantile, INCLUSIVE)*** or ***r(q, LE)*** :=<br>Given *q*, return the rank, *r*, of the largest quantile that is less than or equal to *q*.

<b>Implementation:</b>
Given *q*, search the quantile array until we find the adjacent pair *{q1, q2}* where *q1 <= q < q2*. Return the rank, *r*, associated with *q1*, the first of the pair. 

<b>Boundary Notes:</b>

* If the given *q* is larger than the largest quantile retained by the sketch, the function will return the rank of the largest retained quantile.
* If the given *q* is smaller than the smallest quantile retained by the sketch, the function will return a rank of zero.

<b>Examples using normalized ranks:</b>

* *r(55) = 1.0*
* *r(5) = 0.0*
* *r(30) = .786* (Illustrated in table)

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Quantile input     |      |      |      | 30   | 30   | 30   |      |       |
| Qualifying pair    |      |      |      |      |      | q1   | q2   |       |
| Rank result        |      |      |      |      |      | .786 |      |       |


## The quantile functions with inequalities

### ***quantile(rank, EXCLUSIVE)*** or ***q(r, GT)*** :=<br>Given *r*, return the quantile, *q*, of the smallest rank that is strictly Greater Than *r*.

<b>Implementation:</b>
Given *r*, search the rank array until we find the adjacent pair *{r1, r2}* where *r1 <= r < r2*. Return the quantile associated with *r2*, the second of the pair.

<b>Boundary Notes:</b>

* If the given normalized rank, *r*, is equal to 1.0, there is no quantile that satisfies this criterion. However, for convenience, the function will return the largest quantile retained by the sketch.
* If the given normalized rank, *r*, is less than the smallest rank, the function will return the smallest quantile.

<b>Examples using normalized ranks:</b>

* *q(1.0) = 50*
* *q(0.0) = 10*
* *q(.357) = 30* (Illustrated in table)

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Rank input         |      |      | .357 |      |      |      |      |       |
| Qualifying pair    |      |      | r1   | r2   |      |      |      |       |
| Quantile result    |      |      |      | 30   |      |      |      |       |

--------

### ***quantile(rank, EXCLUSIVE_STRICT)*** or ***q(r, GT_STRICT)*** :=<br>Given *r*, return the quantile, *q*, of the smallest rank that is strictly Greater Than *r*.

In <b>STRICT</b> mode, the only difference is the following:

<b>Boundary Notes:</b>

* If the given normalized rank, *r*, is equal to 1.0, there is no quantile that satisfies this criterion. The function will return *NaN*.


--------

### ***quantile(rank, INCLUSIVE)*** or ***q(r, GE)*** :=<br>Given *r*, return the quantile, *q*, of the smallest rank that is strictly Greater than or Equal to *r*.

<b>Implementation:</b>
Given *r*, search the rank array until we find the adjacent pair *{r1, r2}* where *r1 < r <= r2*. Return the quantile, *q*, associated with *r2*, the second of the pair.

<b>Boundary Notes:</b>

* If the given normalized rank, *r*, is equal to 1.0, the function will return the largest quantile retained by the sketch.
* If the given normalized rank, *r*, is less than the smallest rank, the function will return the smallest quantile.

<b>Examples using normalized ranks:</b>

For example *q(.786) = 30*

| Quantile[]:        | 10   | 20   | 20   | 30   | 30   | 30   | 40   | 50    |
| ------------------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| Natural Rank[]:    | 1    | 3    | 5    | 7    | 9    | 11   | 13   | 14    |
| Normalized Rank[]: | .071 | .214 | .357 | .500 | .643 | .786 | .929 | 1.000 |
| Rank input         |      |      |      |      |      | .786 |      |       |
| Qualifying pair    |      |      |      |      | r1   | r2   |      |       |
| Quantile result    |      |      |      |      |      | 30   |      |       |


## These inequality functions maintain the 1:1 functional relationship

### The *exclusive* search for q(r) is the inverse of the *exclusive* search for r(q). 

##### Therefore, *q = q(r(q))* and *r = r(q(r))*.

### The *inclusive* search for q(r) is the inverse of the *inclusive* search for r(q). 

##### Therefore, *q = q(r(q))* and *r = r(q(r))*.


## Summary
The power of these inequality search algorithms is that they produce repeatable and accurate results, are insensitive to duplicates and sketch deletions, and maintain the property of 1:1 functions.


