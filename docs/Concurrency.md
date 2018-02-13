---
layout: doc_page
---

## Concurrency

None of the sketches in this library have been designed for concurrent operation and should be considered __not thread safe__.

Most systems that incorporate sketches generally design a wrapper class that maps the required sketch API to the host system environment API.  This is the simplest place to encorporate thread synchronization.

Be aware that some systems (e.g. Spark) may assume thread safety of user modules especially during serialization and deserialization steps.  




