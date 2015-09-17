---
layout: doc_page
---
#Architecture

##Package Structure

###theta package

Class/Interface | Description
-------------------- | -----------
Sketch | Parent of Update Sketches and Compact Sketches
UpdateSketch extends Sketch | Parent of Direct and Heap Update Sketches
UpdateSketchBuilder | Builds Update Sketches
CompactSketch extends Sketch | Parent of Direct and Heap Compact Sketches
SetOperation | Parent of Heap and Direct SetOperations
SetOperationBuilder | Builds Set Operations
AnotB | Interface for AnotB operations
Union | Interface for Union operations
Intersection | Interface for Intersecton operations

###memory Package

Class/Interface | Description
-------------------- | -----------
Memory | Interface for primitive and array memory operations
NativeMemory | Creates and accesses on-heap Memory objects
AllocMemory | Allocates off-heap, native Memory objects
MemoryRequest | Request for allocation, frees an allocation
MemoryLink  | Links a freed Memory to a newly allocated Memory
MemoryRegion | Creates and accesses a region of a parent Memory object
MemoryUtil | Some useful Memory utilities

###hash Package

Class/Interface | Description
-------------------- | -----------
MurmurHash3 | core MurmurHash3 algorithm
MurmurHash3Adaptor | Wraps the MurmurHash3 class and extends it with other functions.

###pig.hash Package

Class/Interface | Description
-------------------- | -----------
MurmurHash3 | Pig adaptor to the MurmurHash3 functionality


###pig.theta Package

Class/Interface | Description
-------------------- | -----------
DataToSketch | Creates / builds sketches
Estimate | Obtains the estimate from a sketch
ErrorBounds | Obtains error bounds from a sketch
SketchToString | Pretty prints sketch summary
Merge | Performs iterative union operations on sketches
Intersect | Performs iterative intersection operations on sketches
AexcludeB | Performs an AnotB operation on two sketches

###hive.theta Package

Class/Interface | Description
-------------------- | -----------
Blah | blah

###hll Package

Class/Interface | Description
-------------------- | -----------
Blah | blah

##Class Hierarchy (simplified)

###theta package
* Sketch 
  * UpdateSketch
    * HeapAlphaSketch
    * HeapQuickSelectSketch
    * DirectQuickSelectSketch
  * CompactSketch
    * HeapCompactSketch
    * HeapCompactOrderedSketch
    * DirectCompactSketch
    * DirectCompactOrderedSketch
* UpdateSketchBuilder
* SetOperation
  * HeapUnion implements Union
  * DirectUnion implements Union
  * HeapIntersection implements Intersection
  * DirectIntersection implements Intersection
  * HeapAnotB implements AnotB
* SetOperationBuilder
* Sketches
* Union (interface)
* Intersection (interface)
* AnotB (interface)


  