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
# Quantiles and Ranks Definitions
Streaming quantiles algorithms, or quantiles sketches, enable us to analyze the distributions of massive data very quickly using only a small amout of space.  They allow us to extract values given a desired rank, or the reverse. Quantiles sketches enable us to plot the CDF, PMF or histogrms of a distribution. 

The goal of this short tutorial it to introduce to the reader some of the basic concepts of quantiles, ranks and their functions.

Before we can define *quantile*, we must first define what we mean by *rank*.

## What is a rank?
Given an ordered set of values the term rank can be defined two different ways. 

* The **natural rank** is a **natural number** from the set of one-based, natural numbers, &#8469;<sub>1</sub>, and is derived by enumerating an ordered set of values, starting with the value 1, up to *n*, the number of values in the set.


* The ***normalized rank*** is a number between 0 and 1 computed by dividing the *natural rank* by the total number of values in the set, *n*. Thus, for finite sets, any *normalized rank* is in the range (0, 1]. Normalized ranks are often written as a percent. But don't confuse percent with percentile! This will be explained below.
 
In our sketch library and documentation, when we refer to *rank*, we imply *normalized rank*. However, in this tutorial, we will sometimes use *natural ranks* to simplify the examples.

### Rank and Mass
*Normalized rank* is closely associated with the concept of *mass*. The value associated with the rank 0.5 represents the median value, or the center of *mass* of the entire set where half of the values are below the median and half are above. The concept of mass is important to understanding the Prabability Mass Function (PMF) offered by the quantile sketches in the library.
A rank of *0* means a mass of *0* or an empty set.

## What is a quantile?

> A ***quantile*** is a *value* associated with a ***rank***. 

*Quantile* is the general term that describes other terms that are also quantiles.
To wit:

* A percentile is a quantile where the rank domain is divided into hundredths, e.g., *q(0.95)*.
* A decile is a quantile where the rank domain is divided into tenths, e.g., *q(0.3)*.
* A quartile is a quantile where the rank domain is divided into forths, e.g., *q(0.25)*.
* The median is a quantile that splits the rank domain in half and equals *q(0.5)*.

## The quantile function
Because of the association of quantiles and ranks, we can define a *quantile function*, 
*value = q(r),* a monotonic function that translates a rank into its associated quantile or value.

## The rank function
The rank function,  *rank = r(q)* is the inverse of the quantile function, which, given a quantile (or value), we can compute its associated rank.

## The challenge of duplicates
The functions *q(r)* and *r(q)* would form a 1:1 functional pair if *q = q(r(q))* and *r = r(q(r))*.
However, duplicate values are quite common in real data so exact 1:1 functionality is not possible. As a result it is often the case that  *q != q(r(q))* and *r != r(q(r))*. Duplicate values also can make the rank function, *r(q)*, ambiguous.  If there are multiple adjacent ranks with the same value, which rank should the rank function return? 

## The challenge of approximation
By definiton, sketching algorithms are approximate, and they achieve their high performance by discarding a vast amount of the data.  Suppose you feed *n* items into a sketch that retains only *m* items. This means *n-m* values were discarded.  The sketch must track the value *n* used for computing the rank and quantile functions. When the sketch reconstructs the relationship between ranks and values *n-m* rank values are missing creating holes in the sequence of ranks.    

## The need for inequality search
The quantile sketch algorithms discussed in the literature primarily differ by how they choose which values in the stream should be discarded. After the elimination process, all of the quantiles sketch implementations are left with the challenge of how to reconstruct the actual distribution, approximately and with good accuracy. 

Given the presence of duplicates and absence of values from the stream we must redefine the above quantle and rank functions as inequalities. Let's start with a simple example.

## Two conventions used for searching for ranks
* The first convention, called the *Less-Than* (*LT*) criterion, finds the mass of a distribution, denoted by a rank, that is strictly less-than the given rank.
* The second convention, called the *Less-Than-or-Equal* (*LE*) criterion, finds the mass of a distribution, denoted by a rank, that is strictly less-than-or-equal to the given rank.

You will find both of these in the literature.  Our older *quantiles/DoublesSketch* and our *KLL* quantiles sketch use the *LT* criterion. Our newest *REQ* sketch allows the user to choose.

## Two complementing conventions used for searching for quantiles
When searching for quantiles, we require that search to return a quantile, such that our given *rank ~ r(q(r))* as close a possible.

In order to do that we use two complementing criteria.

* To match the *LT* criterion for rank, we use the greater-than, *GT*, criterion for quantiles
* To match the *LE* criterion for rank, we use the greater-than-or-equal, *GE*, criterion for quantiles.

## Example
Given the ordered values *{10,20,20,20,30}*, we can construct the following table of raw ranks and values. For simplicity we will use natural ranks.

| Ranks, *r*  | 1  | 2  | 3  | 4  | 5  |
|:-----------:|:--:|:--:|:--:|:--:|:--:|
| Values, *q* | 10 | 20 | 20 | 20 | 30 |

Table 1: Raw data mapping of ranks to values

After processing the stream the actual representation inside the sketch might look like the following. This compresses out duplicate values and effectively skips over missing values. Note that the top rank will always be *n*.

| Ranks, *r*  | 1  | 4  | 5  |
|:-----------:|:--:|:--:|:--:|
| Values, *q* | 10 | 20 | 30 |

Table 1B: Raw data mapping compressed

We will use Table 1B for the following.

### Convention *LT*

#### The *LT* (less-than) criterion for finding ranks
Given a value, *V*, find an adjacent pair of values, *q1,q2*, where *q1 < V <= q2*. Return the rank of *q1*.

* Given *V=10*, *? < V <= 10*. Return 0. There is no value in the set < *10*.
* Given *V=20*, *10 < V <= 20*. Return 1.
* Given *V=30*, *20 < V <= 30*. Return 4.

Table 2 represents this mapping.

| Given *q*     | 10 | 20 | 30 |
|:-------------:|:--:|:--:|:--:|
| Find *r* (LT) | 0  | 1  | 4  |

Table 2: Using the *LT* criterion for finding ranks.

Obtaining the quantile value given the rank is going the opposite direction, so we use the *GT* (greater-than) criterion.

#### The *GT* (greater-than) criterion for finding quantiles.
Given a rank, *R*, find an adjacent pair of ranks, *r1,r2*, where *r1 <= R < r2*. Return *q(r2)*.
 
* Given *R=1, 2 or 3*, *1 <= R < 4*. Return *20*.
* Given *R=4*, *4 <= R < 5*. Return *30*
* Given *R=5*, *5 <= R < ?*. Return *30*. There is no rank > 5, but because it is at the top of the range we can safely return the top value.

| Given *r*     | 1  | 2  | 3  | 4  | 5  |
|:-------------:|:--:|:--:|:--:|:--:|:--:|
| Find *q* (GT) | 20 | 20 | 20 | 30 | 30 |

Table 3: Using the *GT* criterion for finding quantiles 

### Convention *LE*

#### The *LE* (less-than or equals) criterion for finding ranks
Given a value, *V*, find an adjacent pair of values, *q1,q2*, where *q1 <= V < q2*. Return the rank of *q1*.

* Given *V=10*, *10 <= V < 20*. Return 1.
* Given *V=20*, *20 <= V < 30*. Return 4.
* Given *V=30*, *30 <= V < ?*.  Return 5. 

| Given *q*     | 10 | 20 | 30 |
|:-------------:|:--:|:--:|:--:|
| Find *r* (LE) | 1  | 4  | 5  |

Table 4: The *LE* criterion for finding ranks.

Obtaining the quantile value given the rank is going the opposite direction, so we use the *GE* (greater-than-or-equals) criterion.

#### The *GE* (greater-than or equals) criterion for finding quantiles
Given a rank, *R*, find an adjacent pair of ranks, *r1,r2*, where *r1 < R <= r2*. Return *q(r2)*.

* Given *R=1*, *? < R <= 1*. Return *10*.
* Given *R=2, 3 or 4*, *1 < R <= 4*. Return *20*.
* Given *R=5*, *4 < R <= 5*. Return *30*.

| Given *r*     | 1  | 2  | 3  | 4  | 5  |
|:-------------:|:--:|:--:|:--:|:--:|:--:|
| Find *q* (GE) | 10 | 20 | 20 | 20 | 30 |

Table 5: The *GE* criterion for finding quantiles.
