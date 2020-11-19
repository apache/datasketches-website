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

By consensus, Lee Rhodes has been nominated to be the PMC Chair [Thread](https://lists.apache.org/thread.html/r1785eaa1535d1b08d9e802e70543dd69a6525cb90e13a06b86ea3373%40%3Cprivate.datasketches.apache.org%3E), [Conclusion](https://lists.apache.org/thread.html/r41a0738f849ca3e5d00914107dde539ab6bf344071a2b39a24063b3f%40%3Cprivate.datasketches.apache.org%3E).

### [The Resolution](https://datasketches.apache.org/docs/Graduation/Resolution)


## DISCUSS Graduation on dev@datasketches.apache.org -- Done
* Ensure Mentors have no remaining issues
* [Discussion Thread](https://lists.apache.org/thread.html/r691d6394e78f7a9d321fddd040b14a8282e7c6f799fb327ee743beae%40%3Cdev.datasketches.apache.org%3E)


## VOTE on Graduation on dev@datasketches.apache.org -- Done
* Notify the IPMC general list that a community vote is in progress
    * [NOTICE Message](https://lists.apache.org/thread.html/rda589f9f881d18c5f61adefb4cda06c0fdf2f6a0f3f00bb0fde1a821%40%3Cgeneral.incubator.apache.org%3E)
* Achieve positive community graduation recommendation vote
    * [VOTE Thread](https://lists.apache.org/thread.html/r922e2a10e53b4eabdeb089336828c8c256277c9fd5ab80a3a13329d0%40%3Cdev.datasketches.apache.org%3E)

## DISCUSS Graduation on general@incubator.apache.org -- IN PROCESS
* Ensure IPMC has no remaining issues
    * [DISCUSS Thread](https://lists.apache.org/thread.html/r65f2c40c15a35026f4bf2e270051cfffed1b47c78c5685e7dd353d2a%40%3Cgeneral.incubator.apache.org%3E) 

# The Following Items are TBD

## VOTE Graduation on general@incubator.apache.org
* Achieve positive IPMC graduation recommendation vote

## Write Proposal Letter to Board
* <https://incubator.apache.org/guides/graduation.html>

```
From: (PMC Chair)
To: board@apache.org
CC: <<project>-private _at_ incubator dot apache dot org>
Subject: Proposed Resolution: Establish Apache DataSketches as TLP

Dear Apache Board,

Apache DataSketches is ready for graduation out of the incubator. So, please
consider the draft resolution below at your next meeting.

<thank you, best regards, personal note if you wish, etc etc>

(PMC Chair)

--
References:

Home: <https://datasketches.apache.org>
Vote by project: <link to vote thread on project list>
Vote by incubator: <link to vote thread on general list>

Resolution draft:

<<resolution goes here, 72 characters wide, indent with 4 spaces>>

--
<PMC Chair e-mail sig, if you have one>
```

## Acceptance of Resolution by the Board

## Press Release for new TLPs
* Notify [ASF Marketing & Publicity](mailto:press@apache.org).

## Tasks After Graduaton
* <https://incubator.apache.org/guides/transferring.html>

## PMC Guides
* <http://www.apache.org/dev/pmc.html>
* <http://www.apache.org/dev/#pmc>
* <http://www.apache.org/foundation/how-it-works.html#pmc>


