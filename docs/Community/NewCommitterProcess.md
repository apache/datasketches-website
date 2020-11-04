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
* ***NOTE*** On <private@datasketches.apache.org> ONLY!

```
From: (PMC Member)
To: private@datasketches.apache.org **ONLY! DO NOT CC!**
Subject: [DISCUSS] Proposal to invite <candidate> as new committer. 

Dear PMC,

This is a proposal to offer to [Candidate] to join us as a committer on our project.

(justification)

This discussion will end on (month/ day at HH:MM [TimeZone]). 

If this discussion produces a positive consensus, I will proceed to a vote.

Regards,
(PMC Member)

```

## Vote
### Write a vote email similar to the following:
* ***NOTE*** On <private@datasketches.apache.org> ONLY!

```
From: (PMC Member)
To: private@datasketches.apache.org
Subject: [VOTE] [Candidate] as new Committer

(Summarize the results of the DISCUSS thread )

The vote will be performed as follows:
   - Voting ends X days from today, i.e, midnight UTC on YYYY-MM-DD, <TimeZone>. 
     (we recommend at least 3 days)
   - At least 3 (+1) PPMC votes and no (-1) votes.
   - All PMC members including Mentors can vote.

Please vote accordingly:

[ ] +1 approve
[ ] +0 no opinion
[ ] -1 disapprove with the reason

Regards,
(PMC Member)
```

## Close the Vote
* ***NOTE*** On <private@datasketches.apache.org> ONLY!

After sufficient votes have been obtained and the time as elapsed, issue an email similar to the following:

```
Subject: [RESULT] Re: [VOTE] [Candidate] as new Committer

The vote is now closed.

Passed/Did not pass with X (+1) binding votes (list names), and Y 0 votes and Z -1 votes.

Regards,
(PPMC MEMBER)
```

## Committer Invite Example
* ***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

Write a letter to the candidate similar to the following:

```
To: [Candidate]
CC: private@datasketches.apache.org
From: (PMC Member)
Subject: Invitation to become DataSketches Committer

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
```


## If Candidate Accepts, Followup Instructions
* ***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

```
To: [Candidate]
CC: private@datasketches.apache.org
From: (PMC Member)
Subject: Welcome to the Apache DataSketches Project as a Committer!

Dear (Candidate),

Welcome to our project!  We are excited to have you join us.

To complete your admission as a committer you need to register an ICLA with ASF. Note that the ICLA contains your confidential information and should only be sent directly to the secretary@apache.org and not CC'd even to
this private@ email address.

    1. ICLA form: https://www.apache.org/licenses/icla.pdf.
    2. Explanation of the Contributor License Agreements and how to send: https://www.apache.org/licenses/contributor-agreements.html
    3. Make sure you send the ICLA just to the secretary@apache.org, request the secretary to notify private@datasketches.apache.org (on the form), and choose a unique Apache ID (on the form).  You can look to see if your preferred ID is already taken at https://people.apache.org/committer-index.html.  Please also send us an email at private@datasketches.apache.org letting us know that you have sent the ICLA to the secretary. This will enable us to monitor its progress.
    
After the ICLA has been recorded and your account created we will announce your joining the project on dev@datasketches.apache.org.

The developer section of the website describes the roles and provides other
resources:
  https://www.apache.org/foundation/how-it-works.html
  https://www.apache.org/dev/

The incubator also has some useful information for new committers
in incubating projects:
  https://incubator.apache.org/guides/committer.html
  https://incubator.apache.org/guides/ppmc.html

Just as before you became a committer, participation in any ASF community
requires adherence to the ASF Code of Conduct:
  https://www.apache.org/foundation/policies/conduct.html

Regards,
(PMC Member)
```


## Sample Letter from Secretary confirming filing of the ICLA
* ***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

```
From: Matt Sicker (secretary)
To: private@datasketches.apache.org
Subject: [FORM] Account Request - candidate@gxyz.com: [Candidate]
Date:
List: private@datasketches.apache.org

Prospective userid: 123456
Full Name: [Candidate]
Forwarding emal address:  blah


```

## Account Creation
* ***NOTE*** CC'd to <private@datasketches.apache.org> ONLY!

New account requests will only be accepted from PMC chairs and ASF members. Projects in incubation can ask their Mentors to make the account request for them.

* See <https://www.apache.org/dev/pmc.html#newcommitter>

## Announcement to dev@datasketches.apache.org






