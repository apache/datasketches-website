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
# Running Characterization Remotely
 
The following is a short manual how to set up running [characterization tests](https://github.com/apache/datasketches-characterization) remotely on a server (with an access to Linux command line) so that the characterization code sees local changes in the implementation of the sketches --- here, we choose the [Java implemenation](https://github.com/apache/datasketches-java) to experiment with. This allows us, for example, to verify a bug fix or test effects of changing some internal parameters of the sketches.

As a first step, check that the server has Java version 1.8, using `java -version`, as the DataSketches code requires JDK8. If not, install (or ask administrators to install) OpenJDK 8. Furthermore, we will need [Maven](https://maven.apache.org/), which may also be installed locally.

Next, start with cloning the required repositories into the desired location, including [DataSketches Memory](https://github.com/apache/datasketches-memory) which is needed as a dependency. We will assume below that all repositories are in the same directory. 

    $ git clone https://github.com/apache/datasketches-java.git    
    $ git clone https://github.com/apache/datasketches-characterization.git
    $ git clone https://github.com/apache/datasketches-memory.git

Then go to the DataSketches Java component, possibly change the code as desired, and compile and install the Java component using Maven:

    $ mvn clean install
    
While the Java component gets compiled and installed in the `target/` subdirectory, the build may actually fail due to "Too many files with unapproved license" --- this error should not affect the experiments in any way. When installing the Java component after some experimental changes, tests may need to be skipped during the installation (unless we update them), using option `-DskipTests=true`.

Compile and install the Memory component in the same way, that is using `mvn clean install` in directory `datasketches-memory`.

The crucial step is to make the characterization components use the local compiled Java component and not the one in the public repository. For this, we need to modify file `pom.xml` in the datasketches-characterization repository, which contains two `dependency` tags with element `artifactID` set to `datasketches-java`: 

    <dependency>
        <groupId>org.apache.datasketches</groupId>
        <artifactId>datasketches-java</artifactId>
        <version>${datasketches-java.version}</version>
    </dependency>
    
    <!-- Dependency on Test code -->
    <dependency>
        <groupId>org.apache.datasketches</groupId>
        <artifactId>datasketches-java</artifactId>
        <version>${datasketches-java.version}</version>
        <type>test-jar</type>
    </dependency>

We modify both of them by adding the `scope` tag, set to `system`, and the `systemPath` tag as follows:

    <dependency>
        <groupId>org.apache.datasketches</groupId>
        <artifactId>datasketches-java</artifactId>
        <version>${datasketches-java.version}</version>
        **<scope>system</scope>**
        **<systemPath>${basedir}/../datasketches-java/target/datasketches-java-1.4.0-SNAPSHOT.jar</systemPath>**
    </dependency>
    
    <!-- Dependency on Test code -->
    <dependency>
        <groupId>org.apache.datasketches</groupId>
        <artifactId>datasketches-java</artifactId>
        <version>${datasketches-java.version}</version>
        <type>test-jar</type>
        **<scope>system</scope>**
        **<systemPath>${basedir}/../datasketches-java/target/datasketches-java-1.4.0-SNAPSHOT-tests.jar</systemPath>**
    </dependency>    

The particular version of the Java component needs to be checked (see the `target/` subdirectory). **Do not commit the modified pom.xml file.**

Having modified `pom.xml`, compile the characterization code using `mvn clean test` (no need to install this time). Some Maven warnings because of the modified `pom.xml` may appear, which we can nevertheless ignore.

Finally, choose a test to run, for example, `KllSketchAccuracyProfile`. After setting up the test configuration (in file `datasketches-characterization/src/main/resources/quantiles/KllSketchAccuracyJob.conf`) appropriately, it remains to run the test, for which we need to set the CLASSPATH enviroment variable before running `java` by executing the following in the datasketches-characterization directory (as above, the particular version of `datasketches-memory` and `datasketches-java` needs to be checked):

    $ export CLASSPATH="$CLASSPATH:$PWD/target/test-classes:$PWD/target/classes:$PWD/../datasketches-memory/target/datasketches-memory-1.3.0-SNAPSHOT.jar:$PWD/../datasketches-java/target/datasketches-java-1.4.0-SNAPSHOT.jar"
    $ java -ea -Dfile.encoding=UTF-8 org.apache.datasketches.Job src/main/resources/quantiles/KllSketchAccuracyJob.conf 
    
 Then the test should run and when finishes, it writes the test results into a file in directory `datasketches-characterization`. 