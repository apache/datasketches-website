---
layout: doc_page
---

## Frequent Distinct Tuples Sketch

### The Task
Given a multiset of tuples with <i>N</i> dimensions <i>{d1,d2, d3, ..., dN}</i>, and a primary subset of
dimensions <i>M &lt; N</i>, the task is to identify the combinations of <i>M</i> subset dimensions
that have the <i>most frequent</i> number of distinct combinations of the <i>N - M</i> non-primary
dimensions.

### Definitions

* Equal Distribution Threshold
Suppose we have a stream of 160 items where the stream consists of four item types: A, B, C, and D.
If the distribution of occurances was shared equally across the four items each would
occur exactly 40 times or 25% of the total distribution of 160 items. Thus the equally distributed
(or fair share) <i>threshold</i> would be 25% or as a fraction 0.25. 

* Most Frequent
We define <i>Most Frequent</i> items as those that consume more than the fair share threshold of the
total occurances (also called the <i>weight</i>) of the entire stream. 

Suppose we have a stream of 160 items where the stream consists of four item types: A, B, C, and D,
which have the following frequency distribution: 

 * A occurs 70 times
 * B occurs 50 times
 * C occurs 30 times
 * D occurs 10 times

We would declare that A is the most frequent and B is the next most frequent. We would not
declare C and D in a list of most frequent items since their respective frequencies are below 
the threshold of 40 or 25%. 

If all items occured with a frequency of 40, we could not declare 
any item as most frequent. Requesting a list of the "Top 4" items could be a list of the 4 items in any random
order, or a list of zero items, depending on policy.

* Relative Standard Error or RSE
When a stream becomes too large with too many different item types to efficiently process exacty we must turn 
to statistical methods to estimate the most frequent items. The returned list of "most frequent items" will
have an associated "estimated frequency" that has an error component that is a random variable. 
The RSE is a measure of the width of the error distribution of this component and computed similarly to how a 
Standard Deviation is computed. It is relative to the estimated frequency in that the standard
width of the error distribution can be computed as <i>estimate * (1 + RSE)</i> and <i>estimate * (1 - RSE)</i>.

* Primary Key
We define a specific combination of the <i>M</i> primary dimensions as a <i>Primary Key</i>
and all combinations of the <i>M</i> primary dimensions as the set of <i>Primary Keys</i>.

* Group
We define the set of all combinations of <i>N-M</i> non-primary dimensions associated with a
single primary key as a <i>Group</i>.

For example, assume <i>N = 3, M = 2</i>, where the set of Primary Keys are defined by
<i>{d1, d2}</i>. After populating the sketch with a stream of tuples all of size <i>N = 3</i>,
we wish to identify the Primary Keys that have the most frequent number of distinct occurrences
of <i>{d3}</i>. Equivalently, we want to identify the Primary Keys with the largest Groups.

Alternatively, if we choose the Primary Key as <i>{d1}</i>, then we can identify the
<i>{d1}</i>s that have the largest groups of <i>{d2, d3}</i>. The choice of
which dimensions to choose for the Primary Keys is performed in a post-processing phase
after the sketch has been populated. Thus, multiple queries can be performed against the
populated sketch with different selections of Primary Keys.

An important caveat is that if the distribution is too flat, there may not be any
"most frequent" combinations of the primary keys above the threshold. 
Also, if one primary key is too dominant, the sketch may be able to only report on the single 
most frequent primary key combination, which means the possible existance of false negatives.

In this implementation the input tuples presented to the sketch are string arrays.

### Using the FdtSketch

As a simple concrete example, let's assume <i>N = 2</i> and let <i>d1 := IP address</i>, and
<i>d2 := User ID</i>.

Let's choose <i>{d1}</i> as the Primary Keys, then the sketch will allow us to identify the
<i>IP addresses</i> that have the largest populations of distinct <i>User IDs</i>. 

Conversely, if we choose <i>{d2}</i> as the Primary Keys, the sketch will allow us to identify the
<i>User IDs</i> with the largest populations of distinct <i>IP addresses</i>.

Let's set the threshold to 0.1 (10%) and the RSE to 0.05 (5%). This is telling the sketch to size itself 
such that items with distinct frequency counts greater than 10% of the total distinct population will be 
detectable and have a distinct frequency estimation error of less than or equal to 5% with 86% confidence.

    //Construct the sketch
    FdtSketch sketch = new FdtSketch(0.1, 0.05);

Assume the incoming data is a stream of {IP address, User ID} pairs:

    //Populate
    while (inputStream.hasRemainingItems()) {
      String[] in = new String[] {Pair.IPaddress, Pair.userID};
      sketch(in);
    }

We are done populating the sketch, now we post process the data in the sketch:

    int[] priKeyIndices = new int[] {0}; //identifies the IP address as the primary key
    PostProcessor postP = sketch.getPostProcessor();
    List<Group> list = sketch.getResult()
    Iterator<Group> itr = list.iterator()
    while (itr.hasNext()) {
      System.out.println(itr.next())
    }

If we want the converse relation we assign the UserID as the primary key:

    int[] priKeyIndices = new int[] {1}; //identifies the User ID as the primary key
    PostProcessor postP = sketch.getPostProcessor();
    List<Group> list = sketch.getResult()
    Iterator<Group> itr = list.iterator()
    while (itr.hasNext()) {
      System.out.println(itr.next())
    }

Note that we did not have to repopulate the sketch!

