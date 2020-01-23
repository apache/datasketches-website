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
[Next]({{site.docs_dir}}/Theta/KMVempty.html)

## The Inverse Estimate

One of the basic concepts that is used in Theta Sketches is that of the <i>Inverse Estimate</i>.  Once you become comfortable with it you will acquire a lot of the intuition for how sketches work. 

### The Concert Line, How Long?

You have just joined the end of a long line for a concert that you are excited to see. That is you, on the very left.  The line extends a full city block, which in your city is about one-fifth of a mile or roughly 1000 feet.
You would like to know how many people are ahead of you.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/ConcertLine1.png" alt="ConcertLine1" />

The thought process in your head goes something like this for your first estimate:

* You observe that the curb is 2 feet behind you.
* You assume that the spacing between all the people in line is also 2 feet.
* 1000 ft / 2 ft = 500 people.

But what if the block length was different? It might be convenient to normalize the block length to 1.0. 
The equivalent thought process would be:

* You observe that the curb is a distance <i>d</i> = 2 ft/1000 ft = .002 = 0.2% of a block behind you. 
* You assume that the spacing between all the people in line is <i>d</i>.
* 1 / <i>d</i> = 1 / .002 = 500 people.

This is a rather poor estimate as it has only a sample size of one, you!

Looking at the people just ahead you realize that not everyone is spaced the same distance apart.  Couples tend to stand much closer together, and then there are those "dreamers" that are not paying attention and leave big gaps ahead of them.

<img class="doc-img-full" src="{{site.docs_img_dir}}/theta/ConcertLine2.png" alt="ConcertLine2" />

A much better estimate could be obtained by averaging the spacing for more than just one person. 
You can see 10 evenly spaced cracks in the sidewalk ahead, spaced at 3 feet apart, and count 11 people standing in that distance of 30 feet. 
Now your calculation proceeds as follows:

* <i>d</i> = 30 ft/1000 ft = .03 = 3% of a block. 
* 11 * (1 / <i>d</i>) = 11 / .03 = 366 people.

This is a very different answer and, hopefully, more accurate because you have more samples (11) in your estimate.

You will encounter this <i>inverse estimation</i> equation form many times in this library:

<center><i>|S|(1/d) = |S|/d</i>.</center>

where \|S\| is the size of some set <i>S</i>, and <i>d</i> is a probability fraction between zero and one. 



[Next]({{site.docs_dir}}/Theta/KMVempty.html)
