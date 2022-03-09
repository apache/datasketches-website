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
# New Committer Process

* Reference: [New Committer Process](https://community.apache.org/newcommitter.html#new-committer-process)

## Create Proposal for New Candidate
* ***NOTE*** On private datasketches mail list ONLY!

Here are some topics to consider when writing your proposal about the candidate. Any one of which may be sufficient to invite someone to join us as a new committer.

### Interpersonal Skills
 
* Ability to work cooperatively with peers
  * Is this person willing to help peers to balance workload?
  * Does this person respond positively to criticism?
  * Does this person participate in the group decision-making processes?

* Ability and willingness to guide and mentor others
  * How clearly does this person communicate?
  * Does this person take pains to explain elementary concepts?
  * Does this person demonstrate patience and respect when answering user queries?

### Product Contributions
This can be in a number of areas including:

* PRs or code grants
* Documentation
* CI/CD automation
* Build systems
* Website structure and content

### Marketing Contributions
* Evangelizing the use of sketches to solve problems
* Organizing workshops, meetups
* Doing or promoting tech talks with our team and with external scientists and engineers.
* Writing blogposts and "what's new" posts for community growth

### Scientific Contributions

* Has this person authored research papers on sketching algorithms and contributed code that was developed as part of that research to our project?
* Has this person provided substantial assistance to us in understanding the nuances 
  of a theoretical research paper on sketching and how it could be implemented in our library?
* Has this person demonstrated a high level of interest in having their theoretical work implemented in our library even though this person may not be a fluent programmer?

### Community Contributions

* Has this person participated in our mailing lists, Slack discussions, and GitHub issues?
* Is this person known and respected by our active committers and our PMC?
* Has exhibited a high level of interest and commitment to learn, 
  not only about our code base, but about the underlying 
  theory of sketching as well?

## Write Proposal in a DISCUSS thread 
***NOTE*** On private datasketches mail list ONLY!

---
From: (PMC Member)<br>
To: private datasketches mail list **ONLY! DO NOT CC!**<br>
Subject: [DISCUSS] Proposal to invite <candidate> as new committer.

Dear PMC,

This is a proposal to offer to [Candidate] to join us as a committer on our project.

(justification)

This discussion will end on (month/ day at HH:MM [TimeZone]).

If this discussion produces a positive consensus or no objections, I will use [lazy consensus](https://www.apache.org/foundation/voting.html) and proceed to a vote.

Regards,
(PMC Member)

---

## Vote
### Write a vote email similar to the following:
***NOTE*** On private datasketches mail list ONLY!

---
From: (PMC Member)<br>
To: private datasketches mail list<br>
Subject: [VOTE] [Candidate] as new Committer<br>

(Summarize the results of the DISCUSS thread )

The vote will be performed as follows:

* Voting ends X days from today, i.e, midnight UTC on YYYY-MM-DD, <TimeZone>. (we recommend at least 3 days)
* At least 3 (+1) PMC votes and no (-1) votes.

Please vote accordingly:

[ ] +1 approve<br>
[ ] +0 no opinion<br>
[ ] -1 disapprove with the reason

Regards,<br>
(PMC Member)

---

## Close the Vote
***NOTE*** On private datasketches mail list ONLY!

After sufficient votes have been obtained and the time as elapsed, issue an email similar to the following:

---
Subject: [RESULT][VOTE] [Candidate] as new Committer

The vote is now closed.

Passed/Did not pass with X (+1) binding votes (list names), and Y 0 votes and Z -1 votes.

Regards,
(PMC MEMBER)

---

## Committer Invite Example
***NOTE*** CC'd to private datasketches mail list ONLY!

Write a letter to the candidate similar to the following:

---
To: [Candidate]<br>
CC: private datasketches mail list<br>
From: (PMC Member)<br>
Subject: Invitation to become DataSketches Committer<br>

Hello [Candidate]

The datasketches Project Management Committee (PMC) hereby offers
you committer privileges to the project. These privileges are offered on
the understanding that you'll use them reasonably and with common sense. We
like to work on trust rather than unnecessary constraints.

Being a committer grants you write access to all of our repositories. In our normal code committing process we all use PRs and like to have at least one other committer or PMC member review the code before it is merged into *master*. However, for trivial edits (e.g. typos, documentation corrections) you may make commits on your own.  

Being a committer does not require you to participate any more than
you already do. It does tend to make one even more involved as you will
probably find that you spend more time with our project.  Of course, you can decline and
instead remain as a contributor, participating as you do now.

A. This personal invitation is a chance for you to accept or decline
in private.  Either way, please let us know in reply to the 
private datasketches mail list address only.

B. If you accept, you will receive a follow-up message with
the next steps to establish you as a committer.


Regards
(PMC MEMBER)

---

## If Candidate Accepts, Followup Instructions
***NOTE*** CC'd to private datasketches mail list ONLY!

---
To: [Candidate]<br>
CC: private datasketches mail list<br>
From: (PMC Member)<br>
Subject: Welcome to the Apache DataSketches Project as a Committer!

Dear (Candidate),

Welcome to our project!  We are excited to have you join us.

To complete your admission as a committer you need to register an *Individual Contributor License Agreement* (ICLA) with ASF.

* Explanation of the ICLA: https://www.apache.org/licenses/contributor-agreements.html
* ICLA form: https://www.apache.org/licenses/icla.pdf.
    * On the form choose a preferred Apache ID.  This will become your Apache email address, e.g. <ApacheID>@apache.org.  When your account gets set up you will be able to specify a forwarding email address for messages sent to your apache.org email. You can look to see if your preferred ID is already taken at <https://people.apache.org/committer-index.html>.
    * Under notify project write *Apache DataSketches*
* Note that the ICLA contains your confidential information and should only be sent directly to <secretary@apache.org> and not CC'd to this private@ email address nor to me
* In your letter to the secretary please request the secretary to notify private datasketches mail list once the ICLA has been recorded.
* Once you have sent your ICLA to the secretary please notify us on this private@ email address so that we can coordinate with the secretary in getting your account set up.
    
After the ICLA has been recorded and your account created we will announce your joining the project on <dev@datasketches.apache.org>.

The developer section of the website describes the roles and provides other resources:
* <https://www.apache.org/foundation/how-it-works.html>
* <https://www.apache.org/dev/>

The Apache Incubator project also has some useful information for new committers:
* <https://incubator.apache.org/guides/committer.html>
* <https://incubator.apache.org/guides/ppmc.html>

Just as before you became a committer, participation in any ASF community
requires adherence to the ASF Code of Conduct:
* <https://www.apache.org/foundation/policies/conduct.html>

Again, welcome to the DataSketches project!

Best Regards,<br>
(PMC Member)

---


## Sample Letter from Secretary confirming filing of the ICLA
***NOTE*** CC'd to private datasketches mail list ONLY!

---
From: Matt Sicker (secretary)<br>
To: private datasketches mail list<br>
Subject: [FORM] Account Request - candidate@gxyz.com: [Candidate]<br>
Date:<br>
List: private datasketches mail list

Prospective userid: 123456<br>
Full Name: [Candidate]<br>
Forwarding emal address:  blah<br>

---

## Account Creation
***NOTE*** CC'd to private datasketches mail list ONLY!

If the ICLA identifies the project and a valid Apache id, and the [RESULT][VOTE] message has been posted to the PMC private list, then the account creation request is made by the secretary or assistant who files the ICLA.

Otherwise, new account requests will only be accepted from PMC chairs and ASF members. 

* See <https://www.apache.org/dev/pmc.html#newcommitter>
 

## Completing the New Committer Account Setup
After the committer account is established.

---

To: private datasketches mail list, (new committer)<br>
Subject: Completing the setup of your Account

Hello (new committer),<br>
The ASF Infrastructure team has set up your committer account with 
the username 'blah' with an Apache email of <blah>@apache.org.
The next portion of the setup you must do.

Please follow the instructions at the [Guide For New Project Committers](https://infra.apache.org/new-committers-guide.html) to set up your SSH,
svn password, svn configuration, mail forwarding, GitHub ID, etc.

* For new PMC members only:
    * Please subscribe to the DataSketches Project Management 
Committee mailing list private datasketches mail list.

As a committer on the DataSketches Project you will have commit access to the following DataSketches repositories as follows:

* https://github.com/apache/datasketches-java
* https://github.com/apache/datasketches-cpp
* https://github.com/apache/datasketches-hive
* https://github.com/apache/datasketches-pig
* https://github.com/apache/datasketches-postgresql
* https://github.com/apache/datasketches-characterization
* https://github.com/apache/datasketches-website
* https://github.com/apache/datasketches-memory
* https://github.com/apache/datasketches-vector
* https://github.com/apache/datasketches-server
* https://github.com/apache/datasketches-experimentation

Please become familiar with the following ASF resources:

* [Apache developer's pages](https://www.apache.org/dev/)
* [Account Setup](https://gitbox.apache.org/setup/) (This is the part only you can do.)

Once this is completed, I will send you an invite to our ASF slack channel (with your ASF account) and will announce your joining our project on our dev@ list.

ASF documentation is maintained by volunteers and hence can be out-of-date and incomplete, so we beg your understanding, and, of course, perhaps you can help us fix that!

Naturally, if you have any questions be sure to ask us on the <dev@datasketches.apache.org> mailing list or on our slack channel.

Regards,
(PMC Member)
---

## Adding the New Committer to the DataSketches Roster

### PMC
Any PMC Member can add the new committer to the roster on the [DataSketches Roster](https://whimsy.apache.org/roster/committee/datasketches) by clicking the **ADD** button at the top of the page. 


## Committer Announce Template
After the committer account is established.

----
From: PMC Member
To: dev@datasketches.apache.org
Subject: Please Welcome (new committer) as new committer

I am happy to announce that (new committer) has joined us as a new committer!

(add specific details here about new committer)

Regards,
(PMC Member)


---





