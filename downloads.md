---
title: Downloads
subtitle:
sectionid: download
layout: simple_page
---

Choose the most recent release version from <a href="http://search.maven.org/#search|ga|1|datasketches">The Central Repository</a>.

Or, clone or fork the current SNAPSHOT directly from the relevant repository.

### Version Numbers

The artifacts downloaded from <a href="http://search.maven.org/#search|ga|1|datasketches">The Central Repository</a> 
include a version number in the name, as in sketches-core-X.Y.Z.jar. 
This same number is also in the top section of the pom.xml file.

If jars are created using "mvn clean package" at the command line from a cloned or forked copy of
the repository, the jars will be named sketches-core-X.Y.Z-SNAPSHOT.jar and will also be 
in the top section of the pom.xml file.

#### Central Repository Jars

These are fully tested, production quality releases, and hopefully as bug-free as humanly possible. 
However, the code is continuously evolving and improvements in performance, documentation, additions 
to the API, and bug fixes do occur.  When enough of these build up, especially for bug fixes or 
performance improvements, a new release will be issued to the Central Repository and the <i>Z</i> 
digit will be incremented.  Generally, an incremented <i>Z</i> digit should be backward API 
compatible with the previous release. Although this might not be guaranteed for newly introduced
functionality. 

When a significant API change occurs that could affect backward compatibility, 
or major new functionality has been introduced, the <i>Y</i> digit will be incremented.

The <i>X</i> digit is being reserved for major refactorings of the library where backward API 
compatibility may not be guaranteed. 

For the repositories that depend on core, such as <i>sketches-hive</i>, 
the version number may be incremented just to be in sync with the core repository, 
and may not reflect any actual code changes other than a change in the pom.xml to reflect the new 
dependency.


#### SNAPSHOT Jars
If you want the latest and greatest version of the code, it is certainly OK for you to create your 
own snapshot jars from a clone or fork. 
The code is automatically tested using the current test suite, but you might catch the code in
transition to a new future release. Caveat Emptor.

