---
layout: doc_page
---
#Architecture

##theta package

Class/Interface | Description
-------------------- | -----------
Sketch | Parent of all theta sketches
UpdateSketch extends Sketch | Creates Update Sketches
CompactSketch extends Sketch | Parent  of all Compact Sketches
SetOperation | Creates SetOperations
AnotB | Interface for AnotB operations
Union | Interface for Union operations
Intersection | Interface for Intersecton operations

##memory Package

Class/Interface | Description
-------------------- | -----------
Memory | Interface for primitive and array memory operations
NativeMemory | Creates and accesses on-heap Memory objects
AllocMemory | Allocates off-heap, native Memory objects
MemoryRegion | Creates and accesses a region of a parent Memory object
MemoryUtil | Some useful Memory utilities

##hash Package

Class/Interface | Description
-------------------- | -----------
MurmurHash3 | core MurmurHash3 algorithm
MurmurHash3Adaptor | Wraps the MurmurHash3 class and extends it with other functions.

##hll Package

Class/Interface | Description
-------------------- | -----------
Blah | blah

##pig.hash Package

Class/Interface | Description
-------------------- | -----------
MurmurHash3 | Pig adaptor to the MurmurHash3 functionality


##pig.theta Package

Class/Interface | Description
-------------------- | -----------
DataToSketch | Creates / builds sketches
Estimate | Obtains the estimate from a sketch
ErrorBounds | Obtains error bounds from a sketch
SketchToString | Pretty prints sketch summary
Merge | Performs iterative union operations on sketches
Intersect | Performs iterative intersection operations on sketches
AexcludeB | Performs an AnotB operation on two sketches

##hive.theta Package

Class/Interface | Description
-------------------- | -----------
Blah | blah