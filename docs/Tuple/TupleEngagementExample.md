---
layout: doc_page
---

# Tuple Sketch Engagement Example


## The Challenge : Measuring Customer Engagement
When customers visit our websites, blogs, or stores it is very useful to understand how engaged they are with us and our products.  There are many ways to characterize customer engagement, but one common way is to understand how frequently our customers are returning to visit.

For example, let's study the following histogram:

<img class="doc-img-full" src="{{site.docs_img_dir}}/tuple/EngagementHistogram.png" alt="EngagementHistogram.png" />

The X-axis is the number of days that a specific customer (identified by some unique ID) visits our site in a 30 day period.

The Y-axis is the number of distinct visitors (customers) that have visited our site Y number of times during the 30 day period. 

Reading this histogram we can see that about 100 distinct visitors visited our site exactly one day out of the 30 day period. About 11 visitors visited our site on 5 different days of the 30 day period. And, it seems that we have one customer that visited our site every day of the 30 day period!  We certainly want to encourage more of these loyal customers.

Different businesses will have different overall time periods of interest and different resolutions for the repeat visit intervals. They can be over years, months, weeks or days, hours or even minutes.  It is up to the business to decide what time intervals are of interest to measure. What we show here is clearly a made-up example to convey the concept.

So how do we do this?  Especially, how can we do this efficiently, quickly, and suitable for near-real-time results?

Well, we have a sketching solution for that!

## The Input Stream
The input data we need to create the above histogram can be viewed as a stream of tuples, where each tuple as at least two components, a time-stamp and an unique identifier (ID) that is a proxy for a customer or visitor.  In real systems, the tuples may have many other attributes, but for our purposes here, we only need these two.  The stream of tuples might be a live stream flowing in a network, or data being streamed from storage.  It doesn't matter.  

What is important is that in order for these sketching algorithms to work properly is that a sketch must see all the relevant data for a particular day or domain that that particular sketch is assigned to represent.  Sketches are streaming algorithms, which means that every relevant item from a domain must be fed into the sketch one at a time.  Sketches are mergeable, thus parallelizable, which means the stream can be partitioned into many substreams feeding separate sketches. At any time, the sketches can be merged together into a single sketch to provide a snapshot-in-time analysis of the combined streams.

It is critical to emphasize that the input stream must not be pre-sampled (for example, a 10% random sample) as this will seriously impact the accuracy of any estimates derived from the sketches.  The input stream can be pre-filtered to remove robot traffic, for example, which will totally remove that traffic from the analysis.

## Duplicates
We want our customers to come back and visit us many times, which will create tuples with duplicate IDs in the stream.  This is a good thing, but for this analysis we need to handle duplicate ID's in two different ways that we separate by two different stages of the analysis.

### Stage 1: Fine-grain interval sketching
In our example our fine-grain interval is a day and the overall interval is 30 days.  In this stage we want to process all the tuples for one day in a way that ultimately results in a single sketch for that day.  This may mean many sketches operating in parallel to process all the records for one day, but they are ultimately merged down to a single sketch representing all the data for one day.  

Since we want to analyze data for 30 days, at the end of Stage 1, we will have 30 sketches representing each of the 30 days of the month.

In this stage we only want to count vists by any one customer once for a single day, even if a customer visits us multiple times during that day.  

### Stage 2: Merge across days sketching
Once we have all 30 days sketched with their individual sketches, we now want to merge all 30 sketches together into one final sketch. This time, however, we want to count the number of duplicates that occur for any single ID.  This will give us the number of days that ID appeared across all 30 days.

## The Tuple Sketch
Please refer to the [Tuple Overview](https://datasketches.github.io/docs/Tuple/TupleOverview.html) section on this website for a quick review of how the Tuple Sketch works. 

For our example we will use the IntegerSketch in the library which is a Tuple Sketch configured with a Summary field consisting of a single Integer.   


(To be continued!)



 
