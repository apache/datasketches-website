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
## Frequency Sketches Overview

### Frequent Items
These sketches implement algorithms that are members of a class of "Heavy Hitters" algorithms that identify 
the "heaviest" or "most frequently occurring" items in a stream.  

Suppose you have a web-site store that sells songs and you wish to identify the most frequent song-titles 
that are being downloaded from your store.
 
This is a perfect use-case for the frequencies/ItemsSketch, which is a Generic class that can be configured to
count the number of occurrences of any arbitrary item. In this case our song-titles are strings. For example, 

    ItemsSketch<String> sketch = new ItemsSketch<String>();
    while (remainingItems) { sketch.update("songTitle"); }

This configures the sketch to track and count frequent occurrences of Strings. And in this case you would update the sketch
with the title of each song as it appears in the stream. Note that in this case we assume that each occurrence of a song
title carries with it a "weight" of one. After the sketch has been populated with the stream you query the sketch to get
a list of the "most frequently occurring" song titles with an approximate count of the actual number of occurences in the stream.

Now suppose your song titles are sold at different prices and you wish to identify the song titles that are generating the most
revenue. In this case each item can carry a different "weight" which is the price. We can use the same sketch as before, but
we update it using a "weight".

    ItemsSketch<String> sketch = new ItemsSketch<String>();
    while (remainingItems) { sketch.update("songTitle", priceCents); }

The sketch only accepts integral values for the weight, so we just multiply the price by 100 to make the weight integer cents
instead of fractional dollars.  

The Frequent Items Sketch is an "aggregating" sketch in that duplicate items in the stream can have different weights and the 
sketch properly tracks the total weight for each distinct item.

### Frequent Distinct Tuples Sketch

This is a very different algorithm that identifies the most frequent distinct occurrences associated with a specific key, and it is
called the <i>Frequent Distinct Tuples Sketch</i> or <i>FdtSketch</i>.  See the documentation for this sketch.


