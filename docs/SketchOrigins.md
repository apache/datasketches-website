---
layout: overview_page
---

##Sketch Origins

Sketching is a relatively recent development in the theoretical field of 
<a href="https://en.wikipedia.org/wiki/Streaming_algorithm"><i>Stochastic Streaming Algorithms</i></a>[1], which deals with algorithms that can extract information from a stream of data in a single pass (sometimes called "one-touch" processing) using various randomization techniques. 

Sketching is a synergistic blend of both theoretical mathematics, statistics and computer science, refers to a broad range of algorithms, 
and has experienced a great deal of interest and growth since the mid 1990's coinciding with the growth of the Internet and the need to process and analyze 
<a href="https://en.wikipedia.org/wiki/Big_data">Big Data</a>. The term <i>sketch</i>, with its allusion to an artist's sketch, has become the popular term to describe these algorithms and associated data structures that implement the theory. 

<img class="doc-img-full" src="{{site.docs_img_dir}}SketchOrigins.png" alt="SketchOrigins" />

<a href="https://en.wikipedia.org/wiki/Philippe_Flajolet">Philippe Flajolet</a> 
is often refered to as the father of sketching with his research in analytic combinatorics and analysis of algorithms. 
His 1985 paper 
<a href="http://db.cs.berkeley.edu/cs286/papers/flajoletmartin-jcss1985.pdf">
Probabilistic counting Algorithms for Data Base Applications</a> 
co-authored with G. Nigel Martin is one of the earliest papers that outlines the sketching concepts. 
The recent book, 
<a href="http://db.cs.berkeley.edu/cs286/papers/synopses-fntdb2012.pdf">
Synopses for Massive Data: Samples, Histograms, Wavelets, Sketches</a> by 
<a href="http://www2.warwick.ac.uk/fac/sci/dcs/people/graham_cormode/">Graham Cormode</a>, et al, 
is an excellent review of this field.

At this point it is useful to describe the <a href="/docs/SketchElements.html">sketch elements</a> of a common sub-class of sketching algorithms used for solving the 
<a href="https://en.wikipedia.org/wiki/Count-distinct_problem">count-distinct</a> problem.


[1] Also known as "Approximate Query Processing", see <a href="http://people.cs.umass.edu/~mcgregor/711S12/sketches1.pdf">Sketch Techniques for Approximate Query Processing</a>

