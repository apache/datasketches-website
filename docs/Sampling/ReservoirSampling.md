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
    the user must also extend the <i>ArrayOfItemsSerDe</i> interface. Three examples of 
    extending this interface are included for <i>Long</i>s,
    <i>String</i>s, and <i>Number</i>s: <i>ArrayOfLongsSerDe</i>, <i>ArrayOfStringsSerDe</i>,
    and <i>ArrayOfNumbersSerDe</i>.

* ReservoirLongsSketch
    This provides a custom implementation for items of type <tt>long</tt>. Performance is nearly identical
    to that of the items sketch, but there is no need to provide an <i>ArrayOfItemsSerDe</i>.
    

### Space Usage

The reservoir is initialized with a <i>reservoirCapacity</i> indicating the maximum number of items 
that can be stored in the reservoir. To keep the header size small, we encode that value into a 16-bit
representation that guarantees at least <i>reservoirCapacity</i> items can be retained. Due to
quantizaiton error, the exact number of items may be up to 0.03% larger than what was requested. In
contrast to many other sketches in this library, the size does not need to be a power of 2.

When serialized, these sketches use 16 bytes of header data in addition to the serialized size of the
items in the reservoir.


### Updating the sketch with new items

Reservoir sampling does not maintain a hash list of items or associate additional metadata with them. As a
result, each item presented to the sketch is handled independently. As a result, the reservoir may have
duplicate items if the input stream contained duplicates.


### Unioning Reservoirs

As mentioned above, using a single reservoir to process a data stream ensures that the resulting reservoir contains all elements with equal probability, but that all permutations of the input are also equally probable (subject to the limits of the random number generator). That additional guarantee over permutations no longer applies to reservoir unions.

To union two reservoirs, we first compute a weight for the items in each reservoir based on both the reservoir size and the total number of items that have been presented to the reservoir. Because the items are themselves unweighted, the weight reflects the relatieve weight of each reservoir and affects the probability of taking items from each.

A union object is initiailzed with a <i>maxUnionCapacity</i>. Unlike the reservoir size, which can only grow until saturating at its capacity, the actual number of items stored in a union can both grow and shrinkm, depending on the sizes and weights of the input sketches. The only guarantee is that the union result will not grow beyond <i>maxUnionCapacity</i>
