---
layout: doc_page
---

##Memory Package

###Introduction
The DataSketches library comes with a <i>Memory</i> package that enables sketches and set operations 
to be constructed outside the Java Heap in native memory, which is referred to as "off-heap". 

The hardware systems used in big data environments are often quite large with up to a terabyte 
of RAM and 24 CPUs that are capable of 48 hyperthreads. 
Most of that memory is dedicated to selected partitions of the raw data, which can be orders of 
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

###Architecture
The Memory package has 2 interfaces and 4 classes that will be described in this section.

####

###Swim Lanes

<img class="doc-img-full" src="{{site.docs_img_dir}}SwimLanes.png" alt="SwimLanes" />

