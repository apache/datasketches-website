---
layout: doc_page
---

## The Challenge: Fast, Approximate Analysis of Big Data<sup>1</sup>
In the analysis of big data<sup>2</sup> there are often problem queries that don’t scale because they require huge compute resources to generate exact results, or don’t parallelize well. Examples include <i>count distinct</i><sup>3</sup>, quantiles, most frequent items, joins, matrix computations, and graph analysis. Algorithms that can produce “good enough” approximate answers for these problem queries are a required toolkit for modern analysis systems that need to process massive amounts of data quickly. For interactive queries there may not be other viable alternatives, and in the case of real-time streams, these specialized algorithms, appropriately called streaming algorithms, or [sketches]({{site.docs_dir}}/SketchOrigins.html), are the only known solution. This technology has helped Yahoo successfully reduce data processing times from days to hours or minutes on a number of its internal platforms. This section provides a short introduction to some of the capabilities of this library, 

## [Theta Sketches]({{site.docs_dir}}/Theta/ThetaSketchFramework.html): Estimating Stream Expression Cardinalities
Internet content, search and media companies like Yahoo, Google, Facebook, etc., collect many tens of billions of event records from the many millions of users to their web sites each day.  These events can be classified by many different dimensions, such as the page visited and user location and profile information.  Each event also contains some unique identifiers associated with the user, specific device (cell phone, tablet, or computer) and the web browser used.  

<img class="doc-img-full" src="{{site.docs_img_dir}}/PeopleCloud.png" alt="PeopleCloud" />

These same unique identifiers will appear on every page that the user visits.  In order to measure the number of unique identifiers on a page or across a number of different pages, it is necessary to discount the identifier duplicates.  Obtaining an exact answer to a _COUNT DISTINCT_ query with massive data is a difficult computational challenge. It is even more challenging if it is necessary to compute arbitrary expressions across sets of unique identifiers. For example, if set _S_ is the set of user identifiers visiting the Sports page and set _F_ is the set of user identifiers visiting the Finance page, the intersection expression _S &#8745; F_ represents the user identifiers that visited both Sports and Finance.

Computing cardinalities with massive data requires lots of computer resources and time.
However, if an approximate answer to these problems is acceptable, [Theta Sketches]({{site.docs_dir}}/Theta/ThetaSketchFramework.html) can provide reasonable estimates, in a single pass, orders of magnitude faster, even fast enough for analysis in near-real time.

## [Quantiles Sketches]({{site.docs_dir}}/Quantiles/QuantilesOverview.html): Estimating Distributions from a Stream of Values
There are many situations where is valuable to understand the distribution of values in a stream. For example, from a stream of web-page time-spent values, it would be useful to know arbitrary quantiles of the distribution, such as the 25th percentile value, the median value and the 75th percentile value. The [Quantiles Sketches]({{site.docs_dir}}/Quantiles/QuantilesOverview.html) solve this problem and enable the inverse functions such as the Probability Mass Function (PMF) and the Cumulative Distribution Function (CDF) as well. It is relatively easy to produce frequency histograms such as the following diagram, which was produced from a stream of over 230 million time spent events. The space consumed by the sketch was about 43KB.

<img class="doc-img-full" src="{{site.docs_img_dir}}/TimeSpentHistogram.png" alt="TimeSpentHistogram" />

## [Tuple Sketches]({{site.docs_dir}}/Tuple/TupleOverview.html): Extending Theta Sketches to Perform Associative Analysis 
It is often not enough to perform stream expressions on sets of unique identifiers, it is very valuable to be able to associate additive data with these identifiers, such as impression counts or clicks.  Tuple Sketches are a recent addition to the library and can be extended with arbitrary "summary" data.  

## [Frequent Items Sketches]({{site.docs_dir}}/FrequentItems/FrequentItemsOverview.html): Finding the Heavy Hitter Objects from a Stream
It is very useful to be able to scan a stream of objects, such as song titles, and be able to quickly identify those items that occur most frequently.  The term <i>Heavy Hitter</i> is defined to be an item that occurs more frequently than some fractional share of the overall count of items
in the stream including duplicates.  Suppose you have a stream of 1M song titles, but in that stream there are only 100K song titles that are unique. If any single title consumes more than 10% of the stream elements it is a Heavy Hitter, and the 10% is a threshold parameter we call epsilon.

The accuracy of a Frequent Items Sketch is proportional to the configured size of the sketch, the larger the sketch, the smaller is the epsilon threshold that can detect Heavy Hitters. 



________________________
<sup>1</sup><small>See also: <https://yahooeng.tumblr.com/post/135390948446/data-sketches>, 17 Dec 2015.</small>

<sup>2</sup><small>The term "big data" is a popular term for truly massive data, and is somewhat ambiguous. For our usage here, it implies data (either in streams or stored) that is so massive that traditional analysis methods do not scale.</small>

<sup>3</sup><small><i>count distinct</i> is the formal term, borrowed from SQL that has an operator by that name, for the counting of just the distinct (or unique) items of a set ignoring all duplicates. For our usage here, it is reads more smoothly to just refer to distinct count or unique count.</small>

