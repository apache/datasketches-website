---
layout: doc_page
---
<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->
## Memory Package

### Introduction

The primary objective for the _Memory Package_ is to allow high-performance read-write access to Java "off-heap" memory 
(also referred to as _direct_, or _native_ memory). However, as documented below, this package has a rich set of other
capabilities as well.

#### Versioning
The _DataSketches_ memory package has its own repository and is released with its own jars in _Maven Central_ 
(groupId=org.apache.datasketches, artifactId=datasketches-memory).
This document applies to the memory package versions 0.10.0 and after. 

#### Naming Conventions
To avoid confusion in the documentation the capitalized _Memory_ refers to the code in the 
Java _org.apache.datasketches.memory_ package, and the uncapitalized _memory_ refers to computer memory in general. 
There is also a class _org.apache.datasketches.memory.Memory_ that should not be confused with the _org.apache.datasketches.memory_ package.
In the text, sometimes _Memory_ refers to the entire package and sometimes to the specific class, 
but it should be clear from the context.


For compatibility and ease-of-use the _Memory_ API can also be used to manage data structures that are 
contained in Java on-heap primitive arrays, memory mapped files, or _ByteBuffers_.

#### Driving Rationale: Large Java Systems Require "Off-Heap" Memory
The hardware systems used in big data environments can be quite large approaching a terabyte 
of RAM and 24 or more CPUs, each of which can manage two threads.
Most of that memory is usually dedicated to selected partitions of data, 
which can even be orders of magnitude larger. 
How the system designers select the partitions of the data to be in RAM over time is quite complex 
and varies considerably based on the specific objectives of the systems platform. 

It is in these very large data environments that managing how the data gets copied into RAM and 
when it is considered obsolete and can be written 
over by newer or different partitions of data is an important aspect of the systems design. 
Having the JVM manage these large chunks of memory is often problematic. 
For example, the Java specification requires that a new allocation of memory be cleared before
it can be used. When the allocations become large this alone can result in large pauses in a
running application, especially if the application does not require that the memory be cleared. 
Repeated allocation and deallocation of large memory blocks can also cause large garbage collection pauses,
which can have major impact on the performance of real-time systems. 
As a result, it is often the case that the system designers need to manage these large chunks of 
memory directly.  

The JVM has a very sophisticated heap management process and works very well for many 
general purpose programming tasks. 
However, for very large systems that have critical latency requirements, 
utilizing off-heap memory becomes a requirement. 

Java does not permit normal java processes direct access to off-heap memory. Nonetheless, 
in order to improve performance, many internal Java classes leverage a low-level, restricted
class called (unfortunately) "_Unsafe_", which does exactly that. The methods of _Unsafe_
are native methods that are initially compiled into C++ code.  The JIT compiler
replaces this C++ code with assembly language instructions called "intrinsics", which
can be just a single CPU instruction. This results in superior runtime performance that is
very close to what could be achieved if the application was written in C++.

The _Memory_ package is essentially an extension of _Unsafe_ and wraps most of the 
primitive get and put methods and a few specialized methods into a convenient API 
organized around an allocated block of native memory.

The only "official" alternative available to systems developers is to use the Java _ByteBuffer_ class 
that also allows access to off-heap memory.  However, the _ByteBuffer_ API is extremely limited
and contains serious defects in its design and traps that many users of the _ByteBuffer_ class unwittingly 
fall into, which results in corrupted data. This _Memory Package_ has been designed to be a 
replacement for the _ByteBuffer_ class.

Using the _Memory_ package cannot be taken lightly, as the systems developer must now be 
aware of the importance of memory allocation and deallocation and make sure these resources 
are managed properly. To the extent possible, this _Memory Package_ has been designed leveraging Java's own
_AutoCloseable_, and _Cleaner_ and also tracks when allocated memory has been freed and provides safety checks
against the dreaded "use-after-free" case even in a multi-threaded environment.

### Architecture
The Memory package is designed around two major types of entities:

  * _Resources_: A _Resource_ is a collection of consecutive bytes. 
  * _APIs_: An API is a programming interface for reading and writing to a resource.

#### Resourses
The _Memory_ package defines 4 _Resources_, which at their most basic level can be viewed as a collection of consecutive bytes.

  * Primitive on-heap arrays: _boolean[], byte[], char[], short[], int[], long[], float[], double[]_.
  * Java _ByteBuffers_.
  * Off-heap memory. Also called "native" or "direct" memory.
  * Memory-mapped files. 

It should be noted at the outset that the off-heap memory and the memory-mapped file resources require special handling with respect to allocation and deallocation. 
The _Memory Package_ has been designed to access these resources leveraging the Java _AutoCloseable_ interface and the Java internal _Cleaner_ class, 
which also provides the JVM with mechanisms for tracking overall use of off-heap memory. 

#### APIs
The _Memory_ package defines 5 principal APIs for accessing the above resources.

  * _Memory_: Read-only access using byte offsets from the start of the resource.
  * _WritableMemory_: Read/write access using byte offsets from the start of the resource.
  * _BaseBuffer_: Positional API that supports _Buffer_ and _WritableBuffer_ using four key positional values:
  _start_, _position_, _end_, and _capacity_, and a rich set of methods to access them.
  * _Buffer_: Read-only access using the _BaseBuffer_ positional API.
  * _WritableBuffer_: Read-write access using the _BaseBuffer_ positional API.

These 5 principal APIs and the four Resources are then multiplexed into 32 API/Resource combinations as follows:

  * Resource: on-heap, _ByteBuffer_, off-heap, memory-mapped files.
  * Memory versus Buffer APIs
  * Read-only versus read-write APIs
  * Little-Endian versus Big-Endian APIs for multibyte primitives

#### Design Goals 
These are the major design goals for the _Memory Package_.

  * __Common API__. The APIs should be agnostic to the chosen resource, with only a few minor exceptions. 
  * __Performance is critical__. The architecture has been specifically designed to eliminate unnecessary object and interface redirection.
  This allows the JIT compiler to collapse abstract hierarchies down to a "base class" at runtime, eliminating all call overhead. 
  This is why the "APIs" are defined using abstract class hierarchies versus using interfaces, which would force the JIT compiler to create 
  virtual jump tables in the emitted code. This has been proven to provide substantial improvement in runtime performance. 
  * __Eliminate unnecessary copies__. This is also a performance goal. All the API access classes are essentially "views" into the underlying resource. 
  For example: switching from a "Buffer" API to a "Memory" API, or from a writable API to a read-only API, or from a big-endian to a little-endian
  view of the resource does not involve any copying or movement of the underlying data.
  * __Data type agnostic__.  Contrary to the Java specification, the underlying resource can be simultaneously viewed as a collection of bytes, ints, longs, etc, at arbitrary byte offsets. This is similar to the _union_ construct in C. The _ByteBuffer_ already allows this, but its implementation is limited and flawed.
  * __Efficient read-only vs read-write implementation__.  To eliminate duplicate code and unnecessary exceptions we have the writable API extend the read-only API. 
  This means that the read-only API has no writable methods, thus accidental writing from this API is not possible. Given a writable instance, 
  converting it to a read-only instance is a simple cast at compile time. It also means that a user could intentionally down-cast a read-only instance into a writable instance. It has been our experience, however, that this is very rare, and usually only used to obtain an attribute that would otherwise only be obtainable from the writable interface, such as a reference to the underlying array object. For example, this is used internally within our library to eliminate unnecessary data copies during serialization.   
  * __Endianness is immutable and remembered when switching views__.  This was an intentional design choice in response to the way the _ByteBuffer_ was designed, 
  which allows the user to change endianness dynamically. We have found the _ByteBuffer_ implementation to be a major source of data corruption problems that have proven to be nearly impossible to fix. 
  * __Provide both absolute offset addressing and relative positional addressing__. The _Memory_ hierarchy provides the absolute offset addressing API and the 
  _Buffer_ hierarchy provides the relative postional addressing API. These two addressing mechanisms can be switched back and forth without changing the fundamental connection to the
  underlying resource.
  * __Regional views__. Any resource can be subdivided into smaller _regions_. This is similar to the _ByteBuffer.slice()_ capability except it is more flexible.
  

#### Diagram of the Core Hierarchy
This includes both package-private classes as well as public classes, but should help the user understand the inner workings of the _Memory Package_.

<img class="doc-img-full" src="{{site.docs_img_dir}}/memory/CoreHierarchy.png" alt="CoreHierarchy.png" />

### Mapping a Resource to an API
There are two different ways to map a resource to an API. 

  * The first uses methods for allocating on-heap arrays or heap or direct _ByteBuffers_.
  * The second way to map a resource to an API is for _AutoCloseable_ resources, such as off-heap memory and memory-mapped files. 
Special classes called "_Handles_" are used to manage the _AutoCloseable_ properties.

#### Examples for Accessing Primitive On-heap Array Resources
```java
    //use static methods to map a new array of 1024 bytes to the WritableMemory API
    WritableMemory wmem = WritableMemory.allocate(1024);
    
    //Or by wrapping an existing primitive array:
    byte[] array = new byte[] {1, 0, 0, 0, 2, 0, 0, 0};
    Memory mem = Memory.wrap(array);
    assert mem.getInt(0) == 1;
    assert mem.getInt(4) == 2;
```


The following illustrates that the underlying structure of the resource is _bytes_ but we can read it as
_ints, longs, char_, or whatever. This is similar to a C _UNION_, which allows multiple data types
to access the underlying bytes. This isn't allowed in Java! 
So you have to keep careful track of your own structure and the appropriate byte offsets. For example:

```java
    byte[] arr = new byte[16]
    WritableMemory wmem = WritableMemory.wrap(arr);
    wmem.putByte(1, (byte) 1);
    int v = wmem.getInt(0);
    assert ( v == 256 );
    
    arr[9] = 3; //you can also access the backing array directly
    long v2 = mem.getLong(8);
    assert ( v2 == 768L);
```


Reading and writing multibyte primitives requires an assumption about byte ordering or _endianness_. 
The default endianness is _ByteOrder.nativeOrder()_, which for most CPUs is _ByteOrder.LITTLE\_ENDIAN_.
Additional APIs are also available for reading and writing in non-native endianness.

All of the APIs provide a useful _toHexString(...)_ method to assist you in viewing the data in your resources.

#### Examples for Accessing ByteBuffers
Mapping a _ByteBuffer_ resource to the _WritableMemory_ API.  
Here we write the _WritableBuffer_ and read from both the _ByteBuffer_ and the _WritableBuffer_.

```java
    @Test
    public void simpleBBTest() {
      int n = 1024; //longs
      byte[] arr = new byte[n * 8];
      ByteBuffer bb = ByteBuffer.wrap(arr);
      bb.order(ByteOrder.nativeOrder());
      
      WritableBuffer wbuf = WritableBuffer.wrap(bb);
      for (int i = 0; i < n; i++) { //write to wbuf
        wbuf.putLong(i);
      }
      wbuf.resetPosition();
      for (int i = 0; i < n; i++) { //read from wbuf
        long v = wbuf.getLong();
        assertEquals(v, i);
      }
      for (int i = 0; i < n; i++) { //read from BB
        long v = bb.getLong();
        assertEquals(v, i);
      }
    }
```

#### Accessing _AutoCloseable_ Resources

The following diagram illustrates the relationships between the _Map_ and _Handle_ hierarchies. 
The _Map_ interfaces are not public, nonetheless this should help understand their function.

<img class="doc-img-full" src="{{site.docs_img_dir}}/memory/MapAndHandleHierarchy.png" alt="MapAndHandleHierarchy.png" />

##### Accessing Off-Heap Resources  
Direct allocation of off-heap resources requires that the resource be closed when finished.
This is accomplished using a _WritableDirectHandle_ that implements the Java _AutoCloseable_ interface. 
Note that this example leverages the try-with-resources statement to properly close the resource.

```java
    @Test
    public void simpleAllocateDirect() {
      int longs = 32;
      try (WritableDirectHandle wh = WritableMemory.allocateDirect(longs << 3)) {
        WritableMemory wMem1 = wh.get();
        for (int i = 0; i<longs; i++) {
          wMem1.putLong(i << 3, i);
          assertEquals(wMem1.getLong(i << 3), i);
        }
      }
    }
```

Note that these direct allocations can be larger than 2GB.

##### Memory Mapped File Resources
Memory-mapped files are resources that also must be closed when finished.
This is accomplished using a _MapHandle_ that implements the Java _AutoClosable_ interface.
In the src/test/resources directory of the memory-X.Y.Z-test-sources.jar there is a file called _GettysburgAddress.txt_.
Note that this example leverages the _try-with-resources_ statement to properly close the resource.
To print out Lincoln's Gettysburg Address:

```java
    @Test
    public void simpleMap() throws Exception {
      File file = new File(getClass().getClassLoader().getResource("GettysburgAddress.txt").getFile());
      try (MapHandle h = Memory.map(file)) {
        Memory mem = h.get();
        byte[] bytes = new byte[(int)mem.getCapacity()];
        mem.getByteArray(0, bytes, 0, bytes.length);
        String text = new String(bytes);
        System.out.println(text);
      }
    }
```

The following test does the following:

1. Creates a off-heap _WritableMemory_ and preloads it with 4GB of consecutive longs as a candidate source.
2. Creates an empty file, and maps it to a memory-mapped space also of 4GB as the destination.
3. Copies the source to the destination in a single operation. No extra copies required.

```java
    @Test
    public void copyOffHeapToMemoryMappedFile() throws Exception {
      long bytes = 1L << 32; //4GB
      long longs = bytes >>> 3;
  
      File file = new File("TestFile.bin");
      if (file.exists()) { file.delete(); }
      assert file.createNewFile();
      assert file.setWritable(true, false);
      assert file.isFile();
      file.deleteOnExit();  //comment out if you want to examine the file.
  
      try (
          WritableMapHandle dstHandle
            = WritableMemory.writableMap(file, 0, bytes, ByteOrder.nativeOrder());
          WritableDirectHandle srcHandle = WritableMemory.allocateDirect(bytes)) {
  
        WritableMemory dstMem = dstHandle.get();
        WritableMemory srcMem = srcHandle.get();
  
        for (long i = 0; i < (longs); i++) {
          srcMem.putLong(i << 3, i); //load source with consecutive longs
        }
  
        srcMem.copyTo(0, dstMem, 0, srcMem.getCapacity()); //off-heap to off-heap copy
  
        dstHandle.force(); //push any remaining to the file
  
        //check end value
        assertEquals(dstMem.getLong((longs - 1L) << 3), longs - 1L);
      }
    }
```

### Regions and WritableRegions
Similar to the _ByteBuffer slice()_, one can create a region or writable region, 
which is a view into the same underlying resource. 

```java
    @Test
    public void checkRORegions() {
      int n = 16;
      int n2 = n / 2;
      long[] arr = new long[n];
      for (int i = 0; i < n; i++) { arr[i] = i; }
      Memory mem = Memory.wrap(arr);
      Memory reg = mem.region(n2 * 8, n2 * 8);
      for (int i = 0; i < n2; i++) {
        assertEquals(reg.getLong(i * 8), i + n2);
      }
    }
```
