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
# Apache Project Maturity Model Assessment for DataSketches (DRAFT)

## Overview
This is an assessment of the DataSketches podling’s maturity, meant to help inform the decision (of the mentors, community, Incubator PMC and ASF Board of Directors) to graduate it as a top-level Apache project.

It is based on the ASF project maturity model at [https://community.apache.org/apache-way/apache-project-maturity-model.html](https://community.apache.org/apache-way/apache-project-maturity-model.html).

## Status of this document 

There is a parallel *[DISCUSS] DataSketches Maturity* on the <dev@datasketches.apache.org> mail list to enable discussion of any issues. If there is disagreement on an issue, it will be marked here as under discussion.


## Code
### CD10

> The project produces Open Source software, for distribution to the public at no charge.

#### Yes.
  *  The project source code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

### CD20
> The project's code is easily discoverable and publicly accessible.

#### Yes.
  *  See [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html).
 
### CD30
> The code can be built in a reproducible way using widely available standard tools.

#### Yes.
  * See the README document on each of the projects repositories<sup>1</sup>. [For Example.](https://github.com/apache/incubator-datasketches-java/blob/master/README.md).

### CD40
> The full history of the project's code is available via a source code control system, in a way that allows any released version to be recreated.

#### Yes.
  * We use Git/GitHub for source code, documents, and the [website](https://datasketches.apache.org).
  * For all the Apache DataSketches repositories subject to being released<sup>1</sup>, releases are cut from the respective repository.
  * All releases are tagged and in separate, easy-to-locate branches.
 
### CD50
> The provenance of each line of code is established via the source code control system, 
in a reliable way based on strong authentication of the committer. 
When third-party contributions are committed, commit messages provide reliable 
information about the code provenance.

#### Yes.
  * The project uses Apache managed GitHub repositories, ensuring provenance of each line of code to a committer. 
  * Third party contributions are accepted in accordance with the [Apache Third-Party Licensing Policy](href="https://www.apache.org/legal/3party.html) only.


## Licenses and Copyright

### LC10
> The code is released under the Apache License, version 2.0.

#### Yes.
  * See for example, <a href="https://github.com/apache/incubator-datasketches-java/blob/master/LICENSE">LICENSE</a>, which has been accepted in multiple release cycles.
  * All source code files have license headers.
  * All releases pass the Apache *Release Audit Tool* according to the [ASF Source Header and Copyright Notice Policy](https://www.apache.org/legal/src-headers.html).

### LC20
> Libraries that are mandatory dependencies of the project's code 
do not create more restrictions than the Apache License does.

#### Yes.
  * All code dependencies have been reviewed to contain approved licenses only.

### LC30
> The libraries mentioned in LC20 are available as Open Source software.

#### Yes.
  * The references to the open-source libraries mentioned in LC20 can be found in the LICENSE file on each of the repositories subject to release<sup>1</sup>.

### LC40
> Committers are bound by an Individual Contributor Agreement 
(the [Apache ICLA](https://www.apache.org/licenses/icla.txt)) that 
defines which code they are allowed to commit and how they need to identify code that is not their own.

#### Yes.
  * The project uses GitHub repositories managed by Apache where write access requires an Apache account and an ICLA on file.

### LC50
> The copyright ownership of everything that the project produces is clearly defined and documented.

  * All files in the source repository have appropriate headers (See LC10).
  * Software Grant Agreements for the initial donations and Corporate CLAs have been filed.


## Releases
### RE10
> Releases consist of source code, distributed using standard and open archive 
formats that are expected to stay readable in the long term.

#### Yes.
  * Source releases are distributed via [Apache Download Mirrors](http://ws.apache.org/mirrors.cgi)
and linked from the website [Downloads](https://datasketches.apache.org/docs/Community/Downloads.html) page.

### RE20
> Releases are approved by the project's PMC (see CS10), in order to make them 
an act of the Foundation.

#### Yes.
  * All incubating releases have been approved by the DataSketches community, PPMC, and the IPMC, all with at least 3 IPMC votes.

### RE30
> Releases are signed and/or distributed along with digests that can be 
reliably used to validate the downloaded archives.

#### Yes.
  * All releases are signed, and KEYS files are provided on [dist.apache.org](https://dist.apache.org).

### RE40
> Convenience binaries can be distributed alongside source code but they are not 
Apache Releases -- they are just a convenience provided with no guarantee.

#### Yes.
  * We distribute Java jar file bundles via [Nexus Repository Manager](https://repository.apache.org). These jar files include source jars as well as compiled binaries of the source code.
  * However, we discovered that we need to have a copy of these jar files also on [dist.apache.org](https://dist.apache.org). This was an oversight. This has now been corrected on [dist.apache.org](https://dist.apache.org) for all current DataSketches releases where applicable as follows:
    * datasketches-java 1.3.0-incubating
    * datasketches-hive 1.1.0-incubating
    * datasketches-pig 1.0.0-incubating
    * datasketches-memory 1.2.0-incubating
  * This will also be corrected for all new releases going forward.
  * In the future, we may have needs for distributions through other venues, e.g., pgxn.org, pypi, and docker; some of these may be binaries.  Any such external distributions will have copies on [dist.apache.org](https://dist.apache.org).

### RE50
> The release process is documented and repeatable to the extent that
someone new to the project is able to independently generate the complete
set of artifacts required for a release.

#### Yes.
  * All committers have access to [detailed release scripts](https://dist.apache.org/repos/dist/dev/incubator/datasketches/scripts/).
  * As of October 24, 2020, we have successfully completed 17 Apache releases since the start of our incubation.
  * We have 3 committers that have qualified to be *release managers* and have successfully performed releases.


## Quality
### QU10
> The project is open and honest about the quality of its code. Various levels 
of quality and maturity for various modules are natural and acceptable 
as long as they are clearly communicated.

#### Yes.
  * Bugs, various deficiencies and documentation problems come to us from many different sources. Once these bugs are made known to us we record them using the GitHub issues lists of the relevant repository<sup>1</sup>.

### QU20
> The project puts a very high priority on producing secure software.

### Yes.
  * Security issues will be treated with the highest priority.
  * We will follow the guidelines proposed by [CVE Documents and Guidance](https://cve.mitre.org/about/documents.html) should these issues arise.

### QU30
> The project provides a well-documented, secure and private channel to report security issues, along with a documented way of responding to them.

#### Yes.
  * See [Community page, "Reporting Security Issues"](https://datasketches.apache.org/docs/Community/index.html).

### QU40
> The project puts a high priority on backwards compatibility and aims to document any 
incompatible changes and provide tools and documentation to help users transition to new features.

#### Yes.
  * We define two types of backward compatibility, API, and Binary:
     * **API:** To the greatest extent possible we try to maintain compatiblity with older APIs. However, some API changes are inevitable. In these cases we deprecate the older API alongside the newer recommended API for at least one major release cycle, after which the older API may be removed. This is standard policy for most industry code bases.
     * **Binary:** Our current codebase is able to read and process older binary representations of our sketches since about 2014. This is extremely important for our users and is very high priority as our code evolves.

### QU50
> The project strives to respond to documented bug reports in a timely manner.

#### Yes.
  *  We respond very quickly to bug and problem reports and have received excellent feedback from our users about our quick response.


## Community

### CO10
> The project has a well-known homepage that points to all the information 
required to operate according to this maturity model.

#### Yes.
  * See [datasketches.apache.org](https://datasketches.apache.org).

### CO20
> The community welcomes contributions from anyone who acts in good 
faith and in a respectful manner and adds value to the project.

#### Yes.
  * See [Community, Contributing topic](https://datasketches.apache.org/docs/Community/index.html).

### CO30
> Contributions include not only source code, but also documentation, constructive bug 
reports, constructive discussions, marketing and generally anything that adds value to the project.

#### Yes.
  * See [Community, Contributing topic](https://datasketches.apache.org/docs/Community/index.html).

### CO40
> The community strives to be meritocratic and over time aims to give more rights and 
responsibilities to contributors who add value to the project.

#### Yes.
  * We have elected three new committers and have more on the way. All of these are and will be meritocracy based.

### CO50
> The way in which contributors can be granted more rights such as commit 
access or decision power is clearly documented and is the same for all contributors.

#### Yes.
  * See [Community, Contributing topic](https://datasketches.apache.org/docs/Community/index.html).

### CO60
> The community operates based on consensus of its members (see CS10) who 
have decision power. Dictators, benevolent or not, are not welcome in 
Apache projects.

#### Yes.
  * We work hard to build consensus.

### CO70
> The project strives to answer user questions in a timely manner.

#### Yes.
  * We typically respond to issues within a few hours.  These issues come to our attention through many different channels including our dev@datasketches.apache.org, users@datasketches.apache.org, and GitHub issues lists as well as the Apache Slack channels.


## Consensus Building
### CS10
> The project maintains a public list of its contributors who have decision 
power -- the project's PMC (Project Management Committee) consists of 
those contributors.

#### Yes.
  * See [DataSketches Project Incubation Status](https://incubator.apache.org/projects/datasketches.html).

### CS20
> Decisions are made by consensus among PMC members
and are documented on the project's main communications channel. 
Community opinions are taken into account but the PMC has the final word, if needed.

#### Yes.
  * All major project decisions are documented via our <dev@datasketches.apache.org> mail list.

### CS30
> Documented voting rules are used to build consensus when discussion is not sufficient.

#### Yes.
  * The project uses the standard ASF voting rules. Voting rules are clearly stated before the voting starts for each individual vote.

### CS40
> In Apache projects, vetoes are only valid for code commits and are 
justified by a technical explanation, as per the Apache voting rules 
defined in CS30.

#### Yes.
  * We have had only one instance of a "-1" vote from a PPMC member on a code release. The issue was fixed and resubmitted. We support this policy.

### CS50
> All "important" discussions happen asynchronously in written form on the 
project's main communications channel. Offline, face-to-face or private discussions
that affect the project are also documented on that channel.

#### Yes.
  * The project has been making important decisions on the project mailing lists. Minor decisions may occasionally happen during code reviews, which are also asynchronous and in written form. Any synchronous discussions that result in major decisions for the project are documented on our project <dev@datasketches.apache.org> mailing list.


## Independence

### IN10
> The project is independent from any corporate or organizational influence.

#### Yes.
  * Our project has committers and contributors from Yahoo, Inc.; Hypercube, Inc.; Permutive, Inc. UK;
Tableau (Salesforce, Inc.); Georgetown University, Washington, D.C.; Warwick University, UK; 
UC Berkeley; Apache Druid, and other researchers and engineers from around the world.


### IN20
> Contributors act as themselves as opposed to representatives of a corporation or organization.

#### Yes. 
  * The committers and contributors act on their own initiative without representing a corporation or organization.



***
<sup>1</sup> List of Source Repositories that have Apache Releases:

*  [incubator-datasketches-java](https://github.com/apache/incubator-datasketches-java)
*  [incubator-datasketches-cpp](https://github.com/apache/incubator-datasketches-cpp)
*  [incubator-datasketches-memory](https://github.com/apache/incubator-datasketches-memory)
*  [incubator-datasketches-hive](https://github.com/apache/incubator-datasketches-hive)
*  [incubator-datasketches-pig](https://github.com/apache/incubator-datasketches-pig)
*  [incubator-datasketches-postgresql](https://github.com/apache/incubator-datasketches-postgresql)
