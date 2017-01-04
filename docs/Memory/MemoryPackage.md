---
layout: doc_page
---

## Memory Package

### Introduction
The DataSketches sketches-core repository consists of two sub-modules: <i>sketches</i> and <i>memory</i> 
each of which have their own POM and are released as separate sets of jars prefixed as <i>sketches-core</i>
and <i>memory</i>. The <i>sketches-core-X.Y.Z.jar</i> contains the Java <i>com.yahoo.sketches</i> package,
and the <i>memory-X.Y.Z.jar</i> contains the Java <i>com.yahoo.memory</i> package. 
To avoid confusion in the documentation the capitalized <i>Memory</i> refers to the code in the 
Java <i>com.yahoo.memory</i> package, and the uncapitalized <i>memory</i> refers to computer memory in general.

The <i>Memory</i> package allows the construction and primitive read-write capabilities of data structures in native computer memory, 
which is also referred to as "off-java-heap" or just "off-heap". 
For compatibility and ease-of-use the <i>Memory</i> API can also be used to manage data structures that are 
contained in Java on-heap arrays or ByteBuffers.

The hardware systems used in big data environments can be quite large approaching a terabyte 
of RAM and 24 or more CPUs, each of which can manage two threads.
Most of that memory is usually dedicated to selected partitions of the raw data, 
which can be orders of magnitude larger. 
How the system designers select the partitions of the data to be in RAM over time is quite complex 
and varies considerably based on the specific objectives of the systems platorm. 

It is in these very large data environments that managing how the data gets copied into RAM and 
when it is considered obsolete and can be written 
over by newer or different partitions of data is a significant portion of the systems design. 
Having the JVM manage these large chunks of memory would result in large garbage collection 
pauses and poor real-time performance. 
As a result, it is often the case that the system designers need to manage these large chunks of 
memory directly.  

The JVM has a very sophisticated heap management process and works very well for many 
general purpose programming tasks. 
However, for very large systems that have critical latency requirements, 
utilizing off-heap memory becomes a requirement.

Java does not permit normal java processes direct access to off-heap memory. Nonetheless, 
in order to improve performance, many internal Java classes leverage a low-level, restricted
class (unfortunately) called "Unsafe", which does exactly that. The methods of Unafe
are native methods that are initially compiled into C++ code.  The JIT compiler
replaces this C++ code with assembly language instructions called "intrinsics", which
are often just a single CPU instruction.

The <i>Memory</i> package is essentially an extension of Unsafe and wraps most of the 
primitive get and put methods and a few specialized methods into a convenient API 
organized around an allocated block of native memory.

Using the <i>Memory</i> package cannot be taken on lightly, as the systems programmer must now take on the 
responsibility of allocating and freeing off-heap memory very similar to what C and C++ 
programming environments require. 

### Architecture
The Memory package has 2 interfaces and a number of classes that will be described in this section.

#### Memory Interface
The Memory interface defines <i>get</i> and <i>put</i> methods for all Java primitive and 
primitive array types to/from a byte offset that is relative to the base address of some 
object or region of native memory defined by the implementing class.
The methods of this interface leverage the capabilities of the sun.misc.Unsafe class.

In contrast to the <i>java.nio.ByteBuffer</i> classes, which were designed for native 
streaming I/O and include concepts such as <i>position, limit, mark, flip,</i> and <i>rewind</i>, 
this interface specifically bypasses these concepts and instead provides a rich collection of 
primitive, bit, array and copy methods that access the data directly from a single byte offset. 

#### NativeMemory and NativeMemoryR Classes
The NativeMemory class implements the Memory interface and is used to access pre-allocated 
Java byte arrays, long arrays and ByteBuffers by presenting them as arguments to the 
constructors of this class.

    byte[] backingArray = new byte[16];
    Memory mem = new NativeMemory(backingArray);

This allows the use of the Memory interface to access the backing array, similar to a C UNION, 
as just a bunch of bytes, __independent of data type__ and interpretation depends on 
__processor endianness!__. This isn't allowed in Java! So be careful! For example:

    mem.clear(); //sets the backing array to all zeros
    mem.putByte(1, (byte) 1);
    int v = mem.getInt(0);
    assert ( v == 256 );
    
    backingArray[9] = 3;
    long v2 = mem.getLong(8);
    assert ( v2 == 768L);

This allows tightly packing different data types into a data structure similar to a C _struct_.
However, you have to keep careful track of your own structure and the appropriate byte offsets.

The NativeMemory and MemoryRegion classes have a useful toHexString(...) method to assist you in debugging your Memory objects.

The NativeMemoryR is a read-only version of NativeMemory.

#### MemoryMappedFile Class
The MemoryMappedFile class extends NativeMemory and is used to memory map files (including those &gt; 2GB) off heap. 
It is the responsibility of the calling class to free the memory.

#### AllocMemory Class
The AllocMemory class extends MemoryMappedFile and is used to allocate direct, 
off-heap native memory, which is then accessed by the Memory interface methods. 
The AllocMemory class returns an instance of NativeMemory that "points" to a 
specific block of native memory with a specific size or capacity in bytes. 
Any memory allocated this way __must be properly freed using freeMemory()!__

    NativeMemory mem = new AllocMemory(<#bytes>);
    //do whatever, then when done...
    mem.freeMemory();

Note that the freeMemory() method is __not part of the Memory interface__.
Whoever owns the reference to the returned NativeMemory instance __owns__ the memory allocation and is responsible for freeing it. 
Casting to the Memory interface allows the owner to pass a Memory object to a downstream class to read and write, 
but the downstream class cannot free the allocated block of native memory, only the owning class can do this.

It is often a good idea to do your prototyping using backing arrays instad of off-heap native memory and to have asserts enabled via the JVM for testing (most testing environments do this automatically). 
If asserts are enabled bounds checking will be performed saving you much grief.  
Segment faults are nasty and hard to debug.

#### MemoryRegion and MemoryRegionR Classes
The MemoryRegion class implements the Memory interface and provides a means of 
hierarchically partitioning a large Memory allocation, into 
smaller Memory regions, each with their own "capacity" and offsets. 

    NativeMemory mem = new AllocMemory(1024);
    MemoryRegion region1 = new MemoryRegion(mem, 0, 512);
    MemoryRegion region2 = new MemoryRegion(mem, 512, 512);
    //hand off region1 and region2 to other classes that use Memory
    mem.freeMemory();

Now region1 and region2 can be passed to different classes where thier view of memory is only the
region they were handed.

The MemoryRegionR is a read-only version of MemoryRegion.

#### MemoryUtil Class
The MemoryUtil class has useful static utility methods such as Memory to Memory copy.

#### MemoryRequest Interface
The MemoryRequest is a callback interface that is accessible from the Memory interface and provides a means for a Memory object to request more memory from the calling class and to free Memory that is no longer needed. 

Once you start using native (off-heap) memory, you are responsible for managing your own memory 
allocation and disposal. The MemoryRequest interface is a simplistic mechanism for allowing a process that has been handed a chunk of memory to request from the MemoryManager (which you have to write!) a new allocation of memory. Refer to the <i>theta</i> package for examples of
how to do this.

### Swim Lanes

The Memory package enables systems with large RAM to allocate Memory "swim lanes" in native memory. 
Each swim lane could be larger than the largest byte array allocatable from Java, 
which is limited to 2GB. 
From within Java each swim lane can be allocated to a single dedicated thread, 
which allows the threads to work exclusively in their own memory space without interference 
from other threads.
This is illustrated in the following figure.

<img class="doc-img-full" src="{{site.docs_img_dir}}/SwimLanes.png" alt="SwimLanes" />

Within each swim lane, the controlling application can further "allocate" hierarchical 
MemoryRegions and assign them to sub-classes or operations operating within that thread. 

The MemoryUtil class has a Memory-to-Memory copy utility that enables copying across the swim lanes, 
however, such an operation must be performed inside synchronization barriers to avoid any concurrency conflicts.
These synchronization barriers must be provided by the using application.





