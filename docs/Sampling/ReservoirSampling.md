---
layout: doc_page
---

## Reservoir Sampling

Reservoir sampling provides a way to construct a uniform random sample of size <i>k</i> from an unweighted stream of items, without knowing the final length of the stream in advance. As with all sketches in the library, reservoir sampling sketches can be efficiently unioned.

The Sketches Library provides 2 forms of reservoir sampling sketches:
* ReservoirItemsSketch&lt;T&gt;
    This sketch provides a random sample of items of type &lt;T&gt; from the item stream. Every
    item in the stream will have an equal probability of being included in the output reservoir.
    If processing the entire data set with a single instance of this class, all permutations of the
    input elements will be equally likely; if unioning multiplereservoirs, the guarantee is only
    that each element is equally likely to be selected.
    
    If the user needs to serialize and deserialize the resulting sketch for storage or transport, 
    the user must also extend the <i>ArrayOfItemsSerDe</i> interface. Two examples of 
    extending this interface are included for <i>Long</i>s and 
    <i>String</i>s: <i>ArrayOfLongsSerDe</i> and <i>ArrayOfStringsSerDe</i>.

* ReservoirLongsSketch
    This provides a custom implementation for items of type <tt>long</tt>. Performance is nearly identical
    to that of the items sketch, but there is no need to provide an <i>ArrayOfItemsSerDe</i>.
    
    
