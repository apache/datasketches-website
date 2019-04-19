---
layout: doc_page
---

## Frequent Distinct Tuples Sketch
Given a multiset of tuples with dimensions <i>{d1,d2, d3, ..., dN}</i>, and a primary subset of
dimensions <i>M &lt; N</i>, the task is to identify the combinations of <i>M</i> subset dimensions
that have the most frequent number of distinct combinations of the <i>N-M</i> non-primary
dimensions.

We define a specific combination of the <i>M</i> primary dimensions as a <i>Primary Key</i>
and all combinations of the <i>M</i> primary dimensions as the set of <i>Primary Keys</i>.

We define the set of all combinations of <i>N-M</i> non-primary dimensions associated with a
single primary key as a <i>Group</i>.

For example, assume <i>N=3, M=2</i>, where the set of Primary Keys are defined by
<i>{d1, d2}</i>. After populating the sketch with a stream of tuples all of size <i>N</i>,
we wish to identify the Primary Keys that have the most frequent number of distinct occurrences
of <i>{d3}</i>. Equivalently, we want to identify the Primary Keys with the largest Groups.

Alternatively, if we choose the Primary Key as <i>{d1}</i>, then we can identify the
<i>{d1}</i>s that have the largest groups of <i>{d2, d3}</i>. The choice of
which dimensions to choose for the Primary Keys is performed in a post-processing phase
after the sketch has been populated. Thus, multiple queries can be performed against the
populated sketch with different selections of Primary Keys.

As a simple concrete example, let's assume <i>N = 2</i> and let <i>d1 := IP address</i>, and
<i>d2 := User ID</i>.
Let's choose <i>{d1}</i> as the Primary Keys, then the sketch allows the identification of the
<i>IP addresses</i> that have the largest populations of distinct <i>User IDs</i>. Conversely,
if we choose <i>{d2}</i> as the Primary Keys, the sketch allows the identification of the
<i>User IDs</i> with the largest populations of distinct <i>IP addresses</i>.

An important caveat is that if the distribution is too flat, there may not be any
"most frequent" combinations of the primary keys above the threshold. Also, if one primary key
is too dominant, the sketch may be able to only report on the single most frequent primary
key combination, which means the possible existance of false negatives.

In this implementation the input tuples presented to the sketch are string arrays.


