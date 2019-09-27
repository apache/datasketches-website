---
layout: doc_page
---

# Tuple Sketch Engagement Example


## The Challenge : Measuring Engagement
When customers visit our websites, blogs, or stores it is very useful to understand how engaged they are with us and our product.  There are many ways to characterize customer engagement, but one common way is to understand how frequently our customers are returning to visit.

For example, let's study the following histogram:

<img class="doc-img-full" src="{{site.docs_img_dir}}/tuple/EngagementHistogram.png" alt="EngagementHistogram.png" />

The X-axis is the number of days that a specific customer (identified by some unique ID) visits our site in a 30 day period.

The Y-axis is the number of distinct visitors (customers) that have visited our site Y number of times. 

Reading this histogram we can see that about 100 distinct visitors visited our site exactly one day out of the 30 day period. About 11 visitors visited our site on 5 different days of the 30 day period. And, it seems that we have one customer that visited our site every day of the 30 day period!  We certainly want to encourage more of these loyal customers.

Different businesses will have different overall time periods of interest and different resolutions for the repeat visit intervals. They can be over years, months, weeks or days, hours or even minutes.  It is up to the business to decide what time intervals are of interest to measure. What we show here is clearly a made-up example to convey the concept.

So how do we do this?  Especially, how can we do this efficiently, quickly, and suitable for near-real-time results?

Well, we have a sketching solution for that!

(To be continued!)



 
