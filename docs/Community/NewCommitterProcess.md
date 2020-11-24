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
* ***NOTE*** On <private@datasketches.apache.org> ONLY!

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
* Is this person known and respected by our active committers and PPMC?
* Has exhibited a high level of interest and commitment to learn, 
  not only about our code base, but about the underlying 
  theory of sketching as well?

## Write Proposal in a DISCUSS thread 
***NOTE*** On <private@datasketches.apache.org> ONLY!

---
From: (PMC Member)<br>
To: private@datasketches.apache.org **ONLY! DO NOT CC!**<br>
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
***NOTE*** On <private@datasketches.apache.org> ONLY!

---
From: (PMC Member)<br>
To: private@datasketches.apache.org<br>
Subject: [VOTE] [Candidate] as new Committer<br>

(Summarize the results of the DISCUSS thread )

The vote will be performed as follows:

* Voting ends X days from today, i.e, midnight UTC on YYYY-MM-DD, <TimeZone>. (we recommend at least 3 days)
* At least 3 (+1) PPMC votes and no (-1) votes.
* All PMC members including Mentors can vote.

Please vote accordingly:

[ ] +1 approve<br>
[ ] +0 no opinion<br>
[ ] -1 disapprove with the reason

Regards,<br>
(PMC Member)

---

## Close the Vote
***NOTE*** On <private@datasketches.apache.org> ONLY!

After sufficient votes have been obtained and the time as elapsed, issue an email similar to the following:

---
Subject: [RESULT] Re: [VOTE] [Candidate] as new Committer

The vote is now closed.

Passed/Did not pass with X (+1) binding votes (list names), and Y 0 votes and Z -1 votes.

Regards,
(PPMC MEMBER)

---

## Committer Invite Example
***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

Write a letter to the candidate similar to the following:

---
To: [Candidate]<br>
CC: private@datasketches.apache.org<br>
From: (PMC Member)<br>
Subject: Invitation to become DataSketches Committer<br>

Hello [Candidate]

The datasketches(incubating) Project Management Committee (PMC) hereby offers
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
<private@datasketches.apache.org> address only.

B. If you accept, you will receive a follow-up message with
the next steps to establish you as a committer.


Regards
(PMC MEMBER)

---

## If Candidate Accepts, Followup Instructions
***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

---
To: [Candidate]<br>
CC: private@datasketches.apache.org<br>
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
* In your letter to the secretary please request the secretary to notify <private@datasketches.apache.org> once the ICLA has been recorded.
* Once you have sent your ICLA to the secretary please notify us on this private@ email address so that we can coordinate with the secretary in getting your account set up.
    
After the ICLA has been recorded and your account created we will announce your joining the project on <dev@datasketches.apache.org>.

The developer section of the website describes the roles and provides other resources:
* <https://www.apache.org/foundation/how-it-works.html>
* <https://www.apache.org/dev/>

The incubator also has some useful information for new committers in incubating projects:
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
***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

---
From: Matt Sicker (secretary)<br>
To: private@datasketches.apache.org<br>
Subject: [FORM] Account Request - candidate@gxyz.com: [Candidate]<br>
Date:<br>
List: private@datasketches.apache.org

Prospective userid: 123456<br>
Full Name: [Candidate]<br>
Forwarding emal address:  blah<br>

---

## Account Creation
***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

If the ICLA identifies the project and a valid Apache id, and the [RESULT][VOTE] message has been posted to the PMC private list, then the account creation request is made by the secretary or assistant who files the ICLA.

Otherwise, new account requests will only be accepted from PMC chairs and ASF members. Projects in incubation can ask their Mentors to make the account request for them.

* See <https://www.apache.org/dev/pmc.html#newcommitter>


## Committer Done Template
After the committer account is established.

---

To: private@[PROJECT].a.o, ###JoeBloggs@foo.net
Subject: account request: ###Joe Bloggs

(Candidate), as you know, the ASF Infrastructure has set up your
committer account as the username 'blah'.

Please follow the instructions to set up your SSH,
svn password, svn configuration, mail forwarding, etc.
https://www.apache.org/dev/#committers

[If your project automatically adds committers to the PMC]
Please subscribe to the [PROJECT] Project Management 
Committee mailing list private@[PROJECT].apache.org.
[/If]

You have commit access to specific sections of the
ASF repository, as follows:

[PROJECT] has various resources at:
  https://svn.apache.org/repos/asf/[PROJECT]

The general "committers" at:
https://svn.apache.org/repos/private/committers

You will probably need to 'svn switch" previous checkouts to now use https, 
for example:

svn switch --relocate https://svn.apache.org/repos/asf/[PROJECT] https://svn.apache.org/repos/asf/[PROJECT]
    
If you have any questions during this phase, then please
see the following resources:

Apache developer's pages: https://www.apache.org/dev/
Incubator committer guide: https://incubator.apache.org/guides/committer.html

Naturally, if you don't understand anything be sure to ask us on the [PROJECT] dev mailing list. 
Documentation is maintained by volunteers and hence can be out-of-date and incomplete - of course
you can now help fix that.

A PMC member will announce your election to the dev list soon.

(for setup see <https://gitbox.apache.org/setup/>)

---

## Committer Announce Template
After the committer account is established.

----
From: (who sends this?)
To: dev@[PROJECT].apache.org
Subject: new committer: ###Joe Bloggs

The Project Management Committee (PMC) for Apache [PROJECT]
has invited Joe Bloggs to become a committer and we are pleased 
to announce that he has accepted.

(add specific details here)

Being a committer enables easier contribution to the
project since there is no need to go via the patch
submission process. This should enable better productivity.
Being a PMC member enables assistance with the management
and to guide the direction of the project.

---





