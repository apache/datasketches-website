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

# Release Process For C++ Components
__NOTES:__

* This process covers major and minor releases only. Bug-fix releases, which increment the third digit, are performed on a A.B.X branch and not on master, but otherwise is similar.
* Some of these operations can be performed either on the Command-Line or in your IDE, whatever you prefer.

## Preparation
* Confirm correctness for
    * LICENSE
    * NOTICE -- check for copyright dates
    * README.md
    * .asf.yaml
    * .gitignore
         
* From Command Line or IDE:
    * Run Unit tests
    * Run Code Coverage > 90%
    * Confirm that all __temporary__ branches are checked into master and/or deleted, both local and remote.
    * Confirm any new bug fixes have corresponding tests

* From Command Line at Component root:
  * To confirm *gpg-agent* is running type:
      * `ps -axww | grep gpg`  # you should see something like:
          * *64438 ??         0:30.33 gpg-agent --homedir /Users/\<name\>/.gnupg --use-standard-socket --daemon*
      * To start GPG if GPG Agent is not running:
          * `eval $(gpg-agent --daemon)`
  * Confirm GitHub repository is current and git status is clean:
      * `git status`  # should return:
      * "nothing to commit, working tree clean"
  * At major version releases, search for deprecated code and remove at __Major Versions__ only.
      * `find . -name "*.?pp" -type f -print | xargs grep -i -n -s -A0 "deprecated"`
      * you may need to ignore false positives in the pybind11 directory
 

## Create Permanent Release Branch & Python Version Preparation
* Assume target version = A.B.0
* From IDE or Command Line: 
    * Create new __Permanent Branch__: "A.B.X"
    * Change the content of the version.cfg.in file to A.B.0
    * Commit the change.
    * Create Annotated TAG: A.B.0-RC1 (or RCn)
    * Write down the Git hash : example: 40c6f4f
    * Push Branch "A.B.X" with edited version.cfg.in to origin
    * __DO NOT MERGE THIS PERMANENT BRANCH INTO MASTER__
    * Do explicit push of tags on branch "A.B.X" to origin:
        * `git push origin --tags`
* From a web browser at origin web site: github.com/apache/datasketches-\<component\>
    * Select the A.B.X branch
    * Confirm that the tag: A.B.0-RC1 exists and that the tag is on the latest commit and with the correct Git hash.
    * __DO NOT CREATE PR OR MERGE THIS PERMANENT BRANCH INTO MASTER__
* From IDE or Command Line:
    * Confirm that the tag A.B.0-RC1 and the branch A.B.X, and HEAD coincide with the correct Git hash.
    * Confirm that there are no unstaged or staged changes.
    * Return to master branch
    * Edit master version.cfg.in to A'.B'.@DT@.@HHMM@ where A' or B' will be incremented by 1.
    * Commit and Push this change to origin/master with the comment "Release Process: Change version to A'.B' development"
    * Return to release branch A.B.X
    
## Create and/or Checkout Local *dist/dev* directories on your system
* If you have not already, on your system create the two directory structures that mirror the dist.apache.org/repos/ directories:
    * `mkdir dist/dev/datasketches/`
    * `mkdir dist/release/datasketches/`
* Checkout both "dev" and "release" directories 
    * Open a terminal in the dist/dev/datasketches directory and do a checkout:
        * `svn co https://dist.apache.org/repos/dist/dev/datasketches/ .`      #Note the DOT
        * `svn status`    # make sure it is clean
    * Open a terminal in the dist/release/datasketches directory and do a checkout:
        * `svn co https://dist.apache.org/repos/dist/release/datasketches/ .`  #Note the DOT
        * `svn status`    # make sure it is clean

## Create the Candidate Apache Release Distribution on *dist/dev*
### Create primary zip files & signatures
* You will need the following arguments:
  * Absolute path of target project.basedir on your system
  * Artifact name : datasketches-\<component\> where component is e.g., cpp
  * GitHub Tag: A.B.0-RC1 (or RCn)
* Start a new terminal in the above *dist/dev/datasketches/scripts* directory on your system: 
  * To confirm *gpg-agent* is running type:
      * `ps -axww | grep gpg`  # you should see something like:
          * *64438 ??         0:30.33 gpg-agent --homedir /Users/\<name\>/.gnupg --use-standard-socket --daemon*
      * To start GPG if GPG Agent is not running:
          * `eval $(gpg-agent --daemon)`
  * Run something like:
    * `./bashDeployToDist.sh /Users/\<name\>/dev/git/Apache/datasketches-\<component\> datasketches-\<component\> A.B.0-RC1`
    * Follow the instructions.
    * NOTE: if you get the error "gpg: signing failed: No pinentry":
        * open .gnupg/gpg-agent.conf
        * change to: pinentry-program /usr/local/bin/pinentry-tty
        * reload the gpg agent in the terminal: `gpg-connect-agent reloadagent /bye`   
        * restart the ./bashDeployToDist script
    * Close the terminal
* Check and grab the web URL ~ *https://dist.apache.org/repos/dist/dev/datasketches/\<component\>/A.B.0-RC1/*
    * There should be 3 files: \*-src.zip, \*-src.zip.asc, \*-src.zip.sha512 

### Create Copy of External Artifact Distributions
* Run 'Build Python Wheels' action on GitHub for the release branch
* When the build finishes download artifact.zip from it
* Use sign_pypi_wheels.sh script to add GPG signatures and SHA512 checksums
* Check in the result as 'pypi' subdirectory in the release candidate directory 

## Prepare & Send [VOTE] Letter to dev@
* See VoteTemplates directory for a recent example
* If vote is not successful, fix the problem and repeat above steps.
* After a successful vote return to __this point__ and continue ...

## Prepare & Send [VOTE-RESULT] Letter to dev@
* See VoteTemplates directory for a recent example
* Declare that the vote is closed.
* Summarize PPMC vote results

## Move files from dev/staging to release
* use dist/dev/datasketches/scripts/moveDevToRelease.sh script to move the approved release candidate to the destination
* upload pypi artifacts to pypi.org
   * make a temporary copy of the pypi directory and remove *.asc and *.sha512 files from it
   * follow [this guide](https://packaging.python.org/en/latest/tutorials/packaging-projects/#uploading-the-distribution-archives)
      * upload to test.pypi.org
      * install from test pypi
      * upload to pypi.org

## Create & Document Release Tag on GitHub
* Open your IDE and switch to the recently created Release Branch A.B.X
* Find the recently created A.B.0-RCn tag in that branch
* At that same GitHub ID hash, create a new tag A.B.0 (without the RCn).
* From the Command Line: Push the new tag to origin:
    * `git push origin --tags`
* On the GitHub component site document the release 

## Update Website Downloads.md "Latest Source Zip Files" Table
* This script assumes that the remote .../dist/release/datasketches/... directories are up-to-date with no old releases.  
* Start a new terminal in the ../dist/dev/datasketches/scripts directory on your system:
* Make sure you local website directory is pointing to master and up-to-date. 
* Run the following with the argument specifying the location of your local website directory:
    * `./createDownloadsInclude.sh /Users/\<name\>/ ... /datasketches-website`
* When this is done, be sure to commit the changes to the website.

## Update Website Documentation (if new functionality)

## Prepare Announce Letter to dev@
* ASF requests that you wait 24 hours to publish Announce letter to allow the propagation to mirrors.
* Use recent template
* Summarize vote results

## Update These Instructions
* If you have updated this file or any of the scripts, please update this file on the [website](https://datasketches.apache.org/docs/Community/ReleaseProcessForCppComponents.html) and dist/dev/datasketches for the scripts.
