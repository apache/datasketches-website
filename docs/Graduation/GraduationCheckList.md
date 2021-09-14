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
# Graduation Checklist
This is a compilation of a number of documents I could find on graduating an incubator project to a Top Level Project.

## References
* [Guide to Successful Graduation](https://incubator.apache.org/guides/graduation.html)
* [Guide to Successful Community Building](https://incubator.apache.org/guides/community.html)
* [DataSketches Board Reports](https://whimsy.apache.org/board/minutes/DataSketches.html)
* [Duties of the PMC Chair](http://www.apache.org/dev/pmc.html#chair)

## Initial Checks -- Done
* [Review the project Status](https://incubator.apache.org/projects/datasketches.html) : DONE
* [Review the Clutch Report](http://incubator.apache.org/clutch/datasketches.html): DONE
* [Review the Roster](https://whimsy.apache.org/roster/ppmc/datasketches):  DONE
* [Review Website checks](https://whimsy.apache.org/pods/project/datasketches): DONE
* [Successful Name Search](https://issues.apache.org/jira/browse/PODLINGNAMESEARCH-168) : DONE
* Demonstrate ability to create Apache Releases : DONE
  * See [Project Status](https://incubator.apache.org/projects/datasketches.html)

## Define a Charter - Done
```
Apache DataSketches consists of software related to
an open source, high-performance library of 
streaming algorithms commonly called "sketches" in the data sciences.
Sketches are small, stateful programs that process massive data as a
stream and can provide approximate answers, with mathematical
guarantees, to computationally difficult queries orders-of-magnitude
faster than traditional, exact methods.
```

## Demonstrate Community Readiness - Done, <br>(but always open for Feedback)
* Recruit users, developers, committers and PMCers
	* We have participated in a number of conferences
		* USPTO-2020 Tech Conference
		* Spark & AI 2020 Conference
		* ACM-KDD Conference
		* ApacheCon 2019 and 2020
		* FOSDEM 2020 Conference (Brussels)
		* See [Presentations](https://datasketches.apache.org/docs/Background/Presentations.html).
	* DataSketches is integrated into a number of systems, for example:
	   * Apache Druid
	   * Apache Impala (in process)
	   * Permutive.com
	   * GCHQ/Gaffer
	   * Splice Machine
	   * PostgreSQL
	   * Apache Hive
	   * Apache Pig
	   * Nielsen.com
	   * Amazon AWS (in process)
	* We recently added a new committer on August 17, 2020 and have two more in process.
* Take responsible collective action
	* We post major issues for disscussion and vote on our dev@ mailing list. 
* Demonstrate ability to disagree in public on technical matters without destroying personal relationships
	* We are all professionals and individuals with different points of view. Our ability to deal with diversity is what makes us strong.
* Create an open, positive and inclusive atmosphere on the mailing lists
	* We have received very positive feedback from our users about our responsiveness, willingness to help, and openness to invite new users into our discussions. 

## Complete Project Maturity Model - Done, <br>(But always open for feedback)
* [Complete Project Maturity Model](https://github.com/apache/incubator-datasketches-website/blob/master/docs/Graduation/Maturity.md)


## Prepare the Resolution -- Done
### Election of PMC Chair by the PPMC - Done
* [Duties of the PMC Chair](http://www.apache.org/dev/pmc.html#chair)
* PMC Chairs are Vice Presidents given charge of the proper operation of their projects.
* The PMC Chair serves at the direction of the Board of Directors as the chair 
    of the Apache Project, and has primary responsibility for management of 
    the project within the scope of responsibility of the Apache Project.
* Once the PMC Chair has been elected, the Resolution can be created from the [roster](https://whimsy.apache.org/roster/ppmc/datasketches)

By consensus, Lee Rhodes has been nominated to be the PMC Chair *(Thread-confidential)* *(Conclusion-confidential)*.

### [The Resolution](https://datasketches.apache.org/docs/Graduation/Resolution)


## DISCUSS Graduation on dev@datasketches.apache.org -- Done
* Ensure Mentors have no remaining issues
* [Discussion Thread](https://lists.apache.org/thread.html/r691d6394e78f7a9d321fddd040b14a8282e7c6f799fb327ee743beae%40%3Cdev.datasketches.apache.org%3E)


## VOTE on Graduation on dev@datasketches.apache.org -- Done
* Notify the IPMC general list that a community vote is in progress
    * [NOTICE Message](https://lists.apache.org/thread.html/rda589f9f881d18c5f61adefb4cda06c0fdf2f6a0f3f00bb0fde1a821%40%3Cgeneral.incubator.apache.org%3E)
* Achieve positive community graduation recommendation vote
    * [VOTE Thread](https://lists.apache.org/thread.html/r922e2a10e53b4eabdeb089336828c8c256277c9fd5ab80a3a13329d0%40%3Cdev.datasketches.apache.org%3E)

## DISCUSS Graduation on general@incubator.apache.org -- Done
* Ensure IPMC has no remaining issues
    * [DISCUSS Thread](https://lists.apache.org/thread.html/r65f2c40c15a35026f4bf2e270051cfffed1b47c78c5685e7dd353d2a%40%3Cgeneral.incubator.apache.org%3E) 


## VOTE Graduation on general@incubator.apache.org -- Done
* Achieve positive IPMC graduation recommendation vote
    * [VOTE Thread](https://lists.apache.org/thread.html/rf3eaebe1e2430aa85d0b69f3c91debc6c52e8490e59c41b1206dc4fb%40%3Cgeneral.incubator.apache.org%3E)

## Write Proposal Letter to Board -- Done
* <https://incubator.apache.org/guides/graduation.html>

---
To: board@apache.org<br>
CC: <datasketches-private@incubator.apache.org><br>
Subject: Proposed Resolution: Establish Apache DataSketches as TLP

Dear Apache Board,

Apache DataSketches is ready for graduation out of the incubator. So, please
consider the draft resolution below at your next meeting.

This has been an amazing journey and we have learned a great deal not only about
ASF and the *Apache Way*, but about ourselves as well and how to build an even
stronger community. 

Many thanks to the Mentors and IPMC members who have given us guidance and support
along the way.  I want to say special thank-yous to Dave Fisher and Justin Mclean
who have provided substantial coaching for me and the rest of our team, and to
Furkan Kamaci, who has been there for us for every code release (over 17) and vote
since the start of our Incubation.


Best Regards,

Lee Rhodes,<br>
PMC Chair Elect, Committer


References:

* Home: <https://datasketches.apache.org>
* Vote by [project DataSketches:](https://lists.apache.org/thread.html/r922e2a10e53b4eabdeb089336828c8c256277c9fd5ab80a3a13329d0%40%3Cdev.datasketches.apache.org%3E)
* Vote by [Incubator:](https://lists.apache.org/thread.html/rf3eaebe1e2430aa85d0b69f3c91debc6c52e8490e59c41b1206dc4fb%40%3Cgeneral.incubator.apache.org%3E)
    * Summary: 8 (+1 binding) votes, no 0 nor -1 votes
        * Dave Fisher +1 (binding)
        * Kevin Ratnasekera +1 (binding)
        * Ryan Blue +1 (binding)
        * Furkan Kamaci +1! (binding)
        * Liang Chen +1 (binding)
        * Justin Mclean +1 (binding)
        * Byung-Gon Chun +1 (binding)
        * Evans Ye +1 (binding)

Resolution draft:

```
    ESTABLISH THE APACHE DATASKETCHES PROJECT
    
    WHEREAS, the Board of Directors deems it to be in the best interests of
    the Foundation and consistent with the Foundation's purpose to 
    establish a Project Management Committee charged with the creation and 
    maintenance of open-source software, for distribution at no charge to 
    the public, related to an open source, high-performance library of
    streaming algorithms commonly called "sketches" in the data sciences.
    Sketches are small, stateful programs that process massive data as a
    stream and can provide approximate answers, with mathematical
    guarantees, to computationally difficult queries orders-of-magnitude
    faster than traditional, exact methods.
    
    NOW, THEREFORE, BE IT RESOLVED, that a Project Management Committee
    (PMC), to be known as the "Apache DataSketches Project", be and hereby
    is established pursuant to Bylaws of the Foundation; and be it further
    
    RESOLVED, that the Apache DataSketches Project be and hereby is
    responsible for the creation and maintenance of software related to an
    open source, high-performance library of streaming algorithms
    commonly called "sketches" in the data sciences. Sketches are small,
    stateful programs that process massive data as a stream and can provide
    approximate answers, with mathematical guarantees, to computationally
    difficult queries orders-of-magnitude faster than traditional, exact
    methods; and be it further
    
    RESOLVED, that the office of "Vice President, Apache DataSketches" be
    and hereby is created, the person holding such office to serve at the
    direction of the Board of Directors as the chair of the Apache
    DataSketches Project, and to have primary responsibility for management
    of the projects within the scope of responsibility of the Apache
    DataSketches Project; and be it further
    
    RESOLVED, that the persons listed immediately below be and hereby are
    appointed to serve as the initial members of the Apache DataSketches
    Project:
    
     * Alexander Saydakov <alsay@apache.org>
     * Dave Fisher        <wave@apache.org>
     * Edo Liberty        <edo@apache.org>
     * Eshcar Hillel      <eshcar@apache.org>
     * Evans Ye           <evansye@apache.org>
     * Furkan Kamaci      <kamaci@apache.org>
     * Jon Malkin         <jmalkin@apache.org>
     * Justin Thaler      <jthaler@apache.org>
     * Kenneth Knowles    <kenn@apache.org>
     * Lee Rhodes         <leerho@apache.org>
     * Liang Chen         <chenliang613@apache.org>
     * Roman Leventov     <leventov@apache.org>
    
    NOW, THEREFORE, BE IT FURTHER RESOLVED, that Lee Rhodes be appointed to
    the office of Vice President, Apache DataSketches, to serve in
    accordance with and subject to the direction of the Board of Directors
    and the Bylaws of the Foundation until death, resignation, retirement,
    removal or disqualification, or until a successor is appointed; and be
    it further
    
    RESOLVED, that the Apache DataSketches Project be and hereby is tasked
    with the migration and rationalization of the Apache Incubator
    DataSketches podling; and be it further
    
    RESOLVED, that all responsibilities pertaining to the Apache Incubator
    DataSketches podling encumbered upon the Apache Incubator PMC are
    hereafter discharged.
```


<PMC Chair e-mail sig, if you have one>

---

# The Following Items are TBD

## Acceptance of Resolution by the Board

## Press Release for new TLPs
* Notify [ASF Marketing & Publicity](mailto:press@apache.org).

## Tasks After Graduaton
* <https://incubator.apache.org/guides/transferring.html>

## PMC Guides
* <http://www.apache.org/dev/pmc.html>
* <http://www.apache.org/dev/#pmc>
* <http://www.apache.org/foundation/how-it-works.html#pmc>


