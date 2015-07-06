---
layout: doc_page
---
[Back](/docs/Features.html)

##System Merge Speed
The following graph illustrates the speed of merging millions of sketches in a large system environment.

<img class="ds-img" src="/docs/img/MergeSpeed.png" alt="MergeSpeed" />

###How this graph was generated

This system had already built millions of sketches in a "sketch mart" of millions of rows of data.  The goal of this measurement was to measure sketch merge performance in a realistic large system using thousands of different types of queries.  The "rows" represent many different combinations of dimensions and coordinates.  Every systems enviroment, data structures, and query profiles are different and this is no exception.  Nonetheless, as one can see from the graph, this system measured a maximum sketch merge rate of about 14.5 million sketches per second per processor thread.

###Measurement Systems
A cluster of typical data-center class systems.

[Back](/docs/Features.html)
