---
layout: overview_page
---

## The Challenge
Internet content, search and media companies like Yahoo, Google, Facebook, etc., collect many tens of billions of event records from the many millions of users to their web sites each day.  These events can be classified by many different dimensions, such as the page visited and user location and profile information.  Each event also contains some unique identifiers associated with the user, specific device (cell phone, tablet, or computer) and the web browser used.  


<img class="doc-img-full" src="{{site.docs_img_dir}}PeopleCloud.png" alt="PeopleCloud" />


These same unique identifiers will appear on every page that the user visits.  In order to measure the number of unique users on a page or across a number of different pages, it is necessary to discount the identifier duplicates.  Obtaining an exact answer to a <i>COUNT DISTINCT</i> query with large data is a difficult computational challenge.

However, there are many situations where an exact answer is not required.  If an approximate answer is acceptable there are much more efficient solutions using 
<a href="SketchOrigins.html">sketches</a>.  
