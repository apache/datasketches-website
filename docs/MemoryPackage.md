---
layout: doc_page
---

## Memory Package

### Introduction
The DataSketches library comes with a <i>Memory</i> package that using applications can leverage 
to construct and manage data structures in native memory, which is referred to as "off-heap". 
For compatibility and ease-of-use the same API can be used to manage data structures that are 
Java on-heap arrays or ByteBuffers. 

This Memory package is used by sketches library for off-heap memory management. 

The hardware systems used in big data environments can be quite large with up to a terabyte 
of RAM and 24 CPUs that are capable of 48 hyperthreads. 
Most of that memory is usually dedicated to selected partitions of the raw data, which can be orders of 
magnitude larger, in order to optimize performance. 
How the system designers select the partitions of the data to be in RAM over time is quite complex 
and varies considerably based on the specific objectives of the systems platorm. 

It is in these very large data environments that managing how the data gets copied into RAM and 
when it is considered obsolete and can be written 
over by newer or different partitions of data is a significant portion of the systems design. 
It is often the case that the system designers need to manage these large chunks of memory directly, 
rather than depending on the Java Virtual Machine attempt to do this process automatically. 
The JVM has a very sophisticated heap management process and works very well for many general purpose programming tasks. 
But for very large systems that have critical latency requirements, managing the off-heap memory becomes a requirement.

This task cannot be taken on lightly, as the systems programmer must now take on the responsibility of 
allocating and freeing off-heap memory very similar to what C and C++ programming environments require. 
And having the code operate in Java while it is dynamically accessing off-heap memory requires special 
software to allow that to happen, since it is normally not allowed in a Java process.
In order to allocate, read, write, and free off-heap memory the Memory package leverages a restricted, 
low-level class called "Unsafe" that is used by the Java system code to access and manage its own Java Heap. 

### Architecture
The Memory package has 2 interfaces and 4 classes that will be described in this section.

#### Memory Interface
The Memory interface defines <i>get</i> and <i>put</i> methods for all Java primitive and 
primitive array types to/from a byte offset that is relative to the base address of some 
object or region of native memory defined by the implementing class.
The methods of this interface leverage the capabilities of the sun.misc.Unsafe class.

In contrast to the <i>java.nio.ByteBuffer</i> classes, which were designed for native 
streaming I/O and include concepts such as <i>position, limit, mark, flip,</i> and <i>rewind</i>, 
this interface specifically bypasses these concepts and instead provides a rich collection of 
primitive, bit, array and copy methods that access the data directly from a single byte offset. 

#### NativeMemory Class
The NativeMemory class implements the Memory interface and is used to access Java byte arrays, 
long arrays and ByteBuffers by presenting them as arguments to the constructors of this class.

#### AllocMemory Class
The AllocMemory class is a subclass of NativeMemory and is used to allocate direct, off-heap native memory, which is then 
accessed by the NativeMemory methods. 

#### MemoryRegion Class
The MemoryRegion class implements the Memory interface and provides a means of hierarchically partitioning a large block of native memory into 
smaller regions of memory, each with their own "capacity" and offsets. 

#### MemoryUtil Class
The MemoryUtil class has useful static utility methods such as Memory to Memory copy.

#### MemoryRequest Interface
The MemoryRequest is a callback interface that is accessible from the Memory interface and provides a means for a Memory object to request more memory from the calling class and to free Memory that is no longer needed. 

### Swim Lanes

The Memory package enables systems with large RAM to allocate Memory "swim lanes" in native memory. 
Each swim lane could be larger than the largest byte array allocatable from Java, which is limited to 2GB. 
From within Java each swim lane can be allocated to a single dedicated thread, 
which allows the threads to work exclusively in their own memory space without interference from other threads.
This is illustrated in the following figure.

<img class="doc-img-full" src="{{site.docs_img_dir}}SwimLanes.png" alt="SwimLanes" />

Within each swim lane, the controlling application can further "allocate" hierarchical MemoryRegions and 
assign them to sub-classes or operations operating within that thread. 

The MemoryUtil class has a Memory-to-Memory copy utility that enables copying across the swim lanes, 
however, such an operation must be performed inside synchronization barriers to avoid any concurrency conflicts.
These synchronization barriers must be provided by the using application.





