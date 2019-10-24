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
<b>Note that these instructions work on unix-based systems including macs.  Windows systems will
need something similar.</b>

<h2>Creating the command-line <i>sketch</i></h2>

<h3>Clone and install <i>sketches-core</i> and <i>sketches-misc</i></h3>
  * Clone sketches-core and sketches-misc repositories into separate directories on your system.
  * Run <i>mvn install -DskipTests -Dgpg.skip</i> on both directories.  This will download JAR 
  files into your local .m2/repository. Take note of the version numbers for the 
  sketches-core-X.Y.Z.jar and the sketches-misc-X.Y.Z.jar

<h3>Create <i>sketch</i> executable</h3>

Place the following in an empty text file called "sketch" and update the version numbers and the 
path to your local <i>.m2/repository</i> directory:
  
    #!/bin/bash
    # Update version numbers and the path to your local .m2/repository as necessary
    
    COREVER="0.5.2"
    MISCVER="0.1.0"
    M2PATH="/path/to/.m2/repository"
    
    COREPATH="$M2PATH/com/yahoo/datasketches/sketches-core/$COREVER/sketches-core-$COREVER.jar"
    MISCPATH="$M2PATH/com/yahoo/datasketches/sketches-misc/$MISCVER/sketches-misc-$MISCVER.jar"
    CLSPATH="$COREPATH:$MISCPATH"
    
    java -cp $CLSPATH org.apache.datasketches.cmd.CommandLine $@

Move this <i>sketch</i> file to a local system directory accessible from anywhere in your system, 
and make it executable.
  
    cp sketch /usr/local/bin/sketch
    chmod +x /usr/local/bin/sketch

Test your executable. You should see something like the following:
  
    sketch
    
    NAME
        sketch - sketch Uniques, Quantiles, Histograms, or Frequent Items.
    SYNOPSIS
        sketch (this help)
        sketch TYPE help
        sketch TYPE [SIZE] [FILE]
    DESCRIPTION
        Write a sketch(TYPE, SIZE) of FILE to standard output.
        TYPE is required.
        If SIZE is omitted, internal defaults are used.
        If FILE is omitted, Standard In is assumed.
    TYPE DESCRIPTION
        sketch uniq    : Sketch the unique string items of a stream.
        sketch rank    : Sketch the rank-value distribution of a numeric value stream.
        sketch hist    : Sketch the linear-axis value-frequency distribution of numeric value stream.
        sketch loghist : Sketch the log-axis value-frequency distribution of numeric value stream.
        sketch freq    : Sketch the Heavy Hitters of a string item stream.
    
    UNIQ SYNOPSIS
        sketch uniq help
        sketch uniq [SIZE] [FILE]
    
    RANK SYNOPSIS
        sketch rank help
        sketch rank [SIZE] [FILE]
    
    HIST SYNOPSIS
        sketch hist help
        sketch hist [SIZE] [FILE]
    
    LOGHIST SYNOPSIS
        sketch loghist help
        sketch loghist [SIZE] [FILE]
    
    FREQ SYNOPSIS
        sketch freq help
        sketch freq [SIZE] [FILE]

You can create a test data file, with duplicate values, like this:

    $ python -c "exec(\"import random\\nfor _ in range(10000000): print random.randint(1,10000000)\")" > manyNumbers.txt

Now you can do either something like this:

    $ cat manyNumbers.txt | sketch uniq
    or
    $ cat manyNumbers.txt | sketch uniq 16000

or like this:

    $ sketch uniq manyNumbers.txt
    or
    $ sketch uniq 16000 manyNumbers.txt

Providing the size allows you to tune the accuracy.

Be sure to compare the speed of the above to the conventional method:

    $ cat manyNumbers.txt | sort | uniq | wc -l

<h2>Creating the command-line <i>demo</i></h2>

If you haven't already, clone and install <i>sketches-core</i> and <i>sketches-misc</i> as in the 
previous example.

Create the <i>demo</i> executable with the same content as the <i>sketch</i> executable except
for the last line:

    java -cp $CLSPATH org.apache.datasketches.demo.ExactVsSketchDemo $@

Move this <i>demo</i> file to a local system directory accessible from anywhere in your system, 
and make it executable.
  
    cp demo /usr/local/bin/demo
    chmod +x /usr/local/bin/demo

When run, the output should look something like this:

    demo
    
    # COMPUTE DISTINCT COUNT EXACTLY:
    ## BUILD FILE:
    Time Min:Sec.mSec = 0:17.569
    Total Values: 100,000,000
    Build Rate: 175 nSec/Value
    Exact Uniques: 50,002,776
    File Size Bytes: 1,693,331,301

    ## SORT & REMOVE DUPLICATES
    Unix cmd: sort -u -o tmp/sorted.txt tmp/test.txt
    Time Min:Sec.mSec = 1:49.571

    ## LINE COUNT
    Unix cmd: wc -l tmp/sorted.txt
    Time Min:Sec.mSec = 0:00.900
    Output from wc command:
     50002776 tmp/sorted.txt

    Total Exact Time Min:Sec.mSec = 2:08.040


    # COMPUTE DISTINCT COUNT USING SKETCHES
    ## USING THETA SKETCH
    Time Min:Sec.mSec = 0:00.614
    Total Values: 100,000,000
    Build Rate: 6 nSec/Value
    Exact Uniques: 50,002,776
    ## SKETCH STATS
    Sketch Estimate of Uniques: 50,098,990
    Sketch Actual Relative Error: 0.192%
    Sketch 95%ile Error Bounds  : +/- 1.563%
    Max Sketch Size Bytes: 262,144
    Speedup Factor 208.5

    ## USING HLL SKETCH
    Time Min:Sec.mSec = 0:02.212
    Total Values: 100,000,000
    Build Rate: 22 nSec/Value
    Exact Uniques: 50,002,776
    ## SKETCH STATS
    Sketch Estimate of Uniques: 49,784,556
    Sketch Actual Relative Error: -0.436%
    Sketch 95%ile Error Bounds  : +/- 1.306%
    Max Sketch Size Bytes: 8,192
    Speedup Factor 57.9

The first part builds a file, separately timed, of 100M numbers with roughly 50% duplicates. 
The second part sorts and removes duplicates using the unix <i>sort -u</i> command and may take 
several minutes to run, so be patient. The third part does a line count using the unix <i>wc -l</i>
command. 

After that, two different sketch trials are run, one with a <i>Theta Sketch</i> and the
other with a compact implementation of Flajolet's <i>HLL Sketch</i>.  Sketches do not require a 
pre-built file. They run in true streaming mode with the random values generated on the fly.

Check out the statistics! 

Enjoy!
