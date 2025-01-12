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

# Release Process For Java Components 
__NOTES:__

* This process covers major and minor releases only. Patch-fix releases, which increment the third digit, are performed on a A.B.X branch and not on master/main, but otherwise are similar.
* Some of these operations can be performed either on the Command-Line or in your IDE, whatever you prefer.
* The Java **datasketches-memory-2.X and 3.X** projects have their own specialized release process. Please consult the README.md file on those projects.

## Preparation

### Check For Code Completeness
* Confirm that all __temporary__ branches are checked into master/main and/or deleted, both local and remote.
* Confirm any new bug fixes have corresponding tests
* At major version releases, search for deprecated code and remove at __Major Versions__ only.
    * `find . -name "*.java" -type f -print | xargs grep -n -s -A0 "deprecat"` <br/> (This will find "deprecated", "Deprecating", "deprecation", etc.)
    * **Note:** When first marking a segment of code deprecated, please add the current version number. This will make it easier to know when to remove the deprecated code.

### Check POM for Correctness

* `mvn clean` will check for most things
* Import the pom into Eclipse's POM Editor

#### Check Project Dependencies

* `mvn dependency:tree`
* `mvn versions:display-dependency-updates` <br/>
For Java 8, the TestNG version must remain at 7.5.1

#### Check POM Plugin Updates

* `mvn versions:display-plugin-updates`
* `mvn versions:display-property-updates` For all POM properties

### Visual Checks for Correctness
* LICENSE
* NOTICE -- check for copyright dates
* README.md
* .asf.yaml
* .travis.yml (if used)
* .gitattributes -- used to exclude files from release zip, assumes .gitignore <br/>
Also specifies line separator characters for text files.
* .github/workflows
* .gitignore -- used to exclude files from git origin
* pom.xml / apache-rat-plugin config -- checks for license headers, assumes .gitignore <br/> Check if any files need to be added for exclusion.
* pom.xml

### Run Maven Tests
* `mvn apache-rat:check`
* `mvn clean test`
* `mvn clean test -P check-cpp-files` (only for ds-java)
* `mvn clean test -P check-cpp-historical-files` (only for ds-java)
* `mvn clean javadoc:javadoc`

### Run IDE Checks
* Run Code Coverage > 90%
    * **Hint:** Run `mvn clean test` first so that /target/ has a complete set of class files before you attempt the Eclipse `coverage` test.
* SpotBugs checks (is it properly configured?)
* Checkstyle (is it properly configured?)

### Run all GitHub Actions Workflows
* Make sure they can run from both 'main' and the release branch.

## Create Permanent Release Branch & Update POM Versions

### Assume Semantic Versioning
* Current master/main POM version = A.B.0-SNAPSHOT
* Next anticipated future release of master/main = A.B'.0-SNAPSHOT where B' = B + 1.
* Release branch = A.B.X.
* Target release version = A.B.0. 
    * Except for patch releases where the 3rd digit is modified on the same release branch A.B.X

### Make Sure Master/Main Branch Is Clean
* From command line at component root on master/main branch:
* Confirm master/main branch is current and git status is clean:
    * `git status` # should return:
    * "On branch master/main, your branch is up to date with 'origin/master(main)', nothing to commit, working tree clean."

### Edit Master/Main Branch POM with the SNAPSHOT Version of the Anticipated Next Future Release
* Edit master/main pom.xml to A'.B'.0-SNAPSHOT where A' or B' will be incremented by 1.
* Commit and Push this change to origin/master(main) with the comment "Release Process: Change master/main pom version to A'.B'.0-SNAPSHOT."
    * This may require changing to a temparary branch and creating a PR to be approved if master/main branch is restricted. 

### Create And Switch to New __Permanent Release Branch__ "A.B.X"

### Edit Release Branch POM Version to Target Release Version "A.B.0"
* Note: if major version change, the target release version will be A'.0.0, where A' = A + 1.
* Edit pom.xml version to A.B.0 (remove -SNAPSHOT, do not change A or B) in case of normal progression, or A'.0.0 in the case of a new major release.
* Commit the change locally. __DO NOT PUSH YET!__
* Create Annotated TAG for this commit: A.B.0-RC1 (or RCn) or A'.0.0-RC1
* Write down the Git hash : example: 40c6f4f (you will need it later)
* __NOW PUSH__ Release Branch  "A.B.X" with edited pom.xml to origin __DO NOT MERGE THIS PERMANENT BRANCH INTO MASTER/MAIN__
* Do explicit push of tags on new branch A.B.X (or A'.0.X) to origin:
    * `git push origin --tags`

### Run Maven Install on Release Branch
* `mvn clean install -DskipTests=true`
* Check that the /target/ directory has 5 jars: (may need to refresh)
    * datasketches-\<component-version\>-javadoc.jar
    * datasketches-\<component-version\>-sources.jar
    * datasketches-\<component-version\>-test-sources.jar
    * datasketches-\<component-version\>-tests.jar
    * datasketches-\<component-version\>.jar
* Check your local Maven local repository
    * _~/.m2/repository/org/apache/datasketches/datasketches-\<component\>/A.B.0/_ 
    * It should have 5 new jars and a .pom file. 

#### Confirm new Release Branch, Tag and Git hash
* From a web browser at origin web site: github.com/apache/datasketches-\<component\>
    * Select the A.B.X branch or A'.0.X
    * Confirm that the tag: A.B.0-RC1 (or A'.0.0-RC1) exists and that the tag is on the latest commit and with the correct Git hash.
    * __DO NOT CREATE PR OR MERGE THIS PERMANENT BRANCH INTO MASTER/MAIN__
* From IDE or Command Line:
    * Confirm that the tag A.B.0-RC1 and the branch A.B.X, (or A'.0.0-RC1 and the branch A'.0.X) and HEAD coincide with the correct Git hash.
    * Confirm that there are no unstaged or staged changes.
* You may minimize your IDE, pointing at the release branch.

## Push Candidate Release to *dist/dev*

### If Absent, Create Local *dist/dev* directories on your system
* On your system create the two directory structures that mirror the dist.apache.org/repos/ directories:
    * `mkdir dist/dev/datasketches/`
    * `mkdir dist/release/datasketches/`

### Checkout the *dist/dev* directory 
* Open a terminal in the dist/dev/datasketches directory and do a checkout:
    * `svn co https://dist.apache.org/repos/dist/dev/datasketches/ .`      #Note the DOT
    * `svn status`    # make sure it is clean: does not list any (?) or (!) files
        * If any (?) or (!) files exist they must be resolved before proceding. 

### Create & Push Zip Files & Signatures/Hashes to *dist/dev*
* You will need the following arguments:
  * Absolute system path of target project.basedir pointing to the release branch
  * Project.artifactId : "datasketches-\<component\>" where component is e.g., java, memory, pig, hive,...
  * Release GitHub Tag: A.B.0-RC1 (or RCn)
  * Have your GPG passphrase handy -- you may have only a few seconds to enter it!
* Start a new terminal in the above *dist/dev/datasketches/scripts* directory on your system:
  * Confirm *gpg-agent* is running:
      * `eval $(gpg-agent --daemon)`
          * if it is not running it will start it
          * if it is already running you will see something like:
          * `gpg-agent: a gpg-agent is already running - not starting a new one`
  * Run something like this (you must copy & edit):
    * `./bashDeployToDist.sh /Users/<name>/dev/git/Apache/datasketches-<component> datasketches-<component> A.B.0-RC1`
    * Follow the instructions.
    * NOTE: if you get the error "gpg: signing failed: No pinentry":
        * open .gnupg/gpg-agent.conf
        * change to: pinentry-program */usr/local/bin/pinentry-tty*
        * reload the gpg agent in the terminal: `gpg-connect-agent reloadagent /bye` 
        * restart the *./bashDeployToDist* script
  * Close the terminal

#### Check Primary Zip Files & Signatures/Hashes
* Check this web URL ~ *https://dist.apache.org/repos/dist/dev/datasketches/\<component\>/A.B.0-RC1/*
    * Update SVN: `svn up`
    * There should be 3 files: \*-src.zip, \*-src.zip.asc, \*-src.zip.sha512
    * Copy the URL for later.

## Java Only: Push Jars to Nexus (Maven Central) Staging
* **NOTE:** If you are deploying datasketches-memory to Nexus use the *sign-deploy-jar.sh* script in the *datasketches-memory/tools/scripts/ directory* instead.

* Return to original terminal at the project.basedir, still in the A.B.X branch.
* If starting new terminal make sure GPG is running:
  * Confirm *gpg-agent* is running:
      * `eval $(gpg-agent --daemon)`
          * if it is not running it will start it
          * if it is already running you will see something like:
          * `gpg-agent: a gpg-agent is already running - not starting a new one`  
* `git status` # make sure you are still on the release branch: A.B.X

### TRIAL-RUN:
* **Have your GPG passphrase handy -- you may have only a few seconds to enter it!**
* `mvn clean install -Pnexus-jars -DskipTests=true`
    * Check target/ that jars & pom exist

### DEPLOY
* **Have your GPG passphrase handy -- you may have only a few seconds to enter it, but it may be automatic!**
* `mvn clean deploy -Pnexus-jars -DskipTests=true`

#### DEPLOY-CHECK
* Login to Nexus: [repository.apache.org](https://repository.apache.org/) / Staging Repositories for orgapachedatasketches-XXXX
* Click __Content__ and search to the end.  Each jar & pom should have .asc, .md5, .sha1 signatures
* Check target/ & .m2 that jars & pom exist and have .asc signatures

### CLOSE (Very Important)
* [CLOSE] the Staging Repository with a comment: "\<component\> A.B.0"

#### CHECK CLOSE
* Confirm its existance under Repositories/Staging web-site URL (in the summary window)
* Grab its URL while there. You will need it for the Vote Letter.

#### CHECK Local Maven Repo
* Check your local Maven repository
    * _~/.m2/repository/org/apache/datasketches/datasketches-\<component\>/A.B.0/_ 
    * It should have 5 new jars and a .pom file each with .asc, .md5, and .sha1 signatures. <br/> Note: Newer versions of the Maven deploy plugin do not copy the md5 and sha1 signatures into .m2, but they will still exist in Nexus. md5 and sha1 are obsolete.

### Create Copy of External Artifact Distributions
#### JAVA ONLY
* Place copies of the artifact jars deployed to Nexus under a "maven" directory.  For example see <https://dist.apache.org/repos/dist/dev/datasketches/memory/3.0.0-RC1/>
* Note that the `jar` files with their `asc`, (optional `md5` and `sha1`) signature are all together in the .md2 archive 
* Add a `maven` directory under the `dist/dev/datasketches/<component>/A.B.0/`
* Bulk copy the `jar, asc, (optional md5` and `sha1`) files into the `maven` directory.
* `svn status` # check to see if it is ready to add
* `svn add . --force`
* `svn ci -m "add nexus jars to dist/dev/datasketches"`

#### NON-JAVA
* For external artifacts such as Python or Docker the subdirectory name should be relevant to the type.
* These must be signed with GPG (.asc) and SHA512 (.sha512)

## Prepare & Send [VOTE] Letter to dev@
* If vote is not successful, fix the problem and repeat above steps.
* After a successful vote return to __this point__ and continue ...

## Prepare & Send [VOTE-RESULT] Letter to dev@
* See VoteTemplates directory for a recent example
* Declare that the vote is closed.
* Summarize vote results

## If Successful, Finalize the Release

### Copy files from *dist/dev* to *dist/release*
* In local *dist/__dev__/datasketches/*
    * Open Terminal #1
        * Confirm you are in the `/dev/` directory: `pwd`
        * Perform SVN Checkout:
            * `svn co https://dist.apache.org/repos/dist/dev/datasketches/ .`  #note dot at end
            * `svn status` #make sure checkout is clean: does not list any (?) or (!) files
* In local *dist/__release__/datasketches/*
    * Open Terminal #2
        * Confirm you are in the `/release/` directory: `pwd`
        * Perform SVN Checkout:
            * `svn co https://dist.apache.org/repos/dist/release/datasketches/ .` #note dot at end
            * `svn status` #make sure checkout is clean: does not list any (?) or (!) files
        * Create new version directory under appropriate component directory:
            * `mkdir -p <component>/A.B.0`
    * Using local file system copy files 
        * From  ... /dist/dev/datasketches/\<component\>/version-RCnn/*
        * To    ... /dist/release/datasketches/\<component\>/version (no RCnn)/*
        * Make sure to move External Artifact Distributions *dist/dev* to *dist/release*
    * Using Terminal #2 at ... /dist/release/datasketches directory:
        * `svn add . --force`
        * `svn ci -m "Release A.B.0"`
        * Remove the prior release...
        * `svn remove <component>/X.Y.0`
        * `svn ci -m "Remove Prior release"`
        * `svn status` # should be empty
    * Using local file system
        * Delete the prior X.Y.0 directory if necessary.

### Java Only: Release Jars on Nexus Staging
* On Nexus [repository.apache.org](https://repository.apache.org/) click on Staging Repositories
* Select "orgapachedatasketches-XXXX" (If more than one make sure you select the right one!)
* At the top of the window, select "Release"
* Confirm that the attributes have moved to the "Releases" repository under "Repositories"
    * Browse to *Releases/org/apache/datasketches/...*

### Java Only: Drop any previous Release Candidates that were not used.
* On Nexus [repository.apache.org](https://repository.apache.org/) click on Staging Repositories
* Select "orgapachedatasketches-XXXX" (If more than one make sure you select the right one!)
* At the top of the window, select "Drop"

### If necessary, update branch *master/main* from branch *A.B.X*
If you have gone through more than one Release Candidate, you may have changes that need to be 
reflected in the master/main. Use the **git cherry-pick** command for this.  

## Finalize Release Documentation

### Update Apache Reporter
* Because of the commit to the `dist/release` branch, you should get an automated email requesting you to update the Apache DataBase about the releaase. The email should point you to the [Apache Committee Report Helper](https://reporter.apache.org/addrelease.html?datasketches). You can choose to go there directly without waiting for the notice, there is only one box to fillout.
* Update the full name of the component release. For example: `Apache datasketches-memory-1.3.0`

### Create & Document Release Tag on GitHub
* Open your IDE and switch to the recently created Release Branch A.B.X
* Find the recently created A.B.0-RCn tag in that branch
* At that same GitHub ID hash, create a new tag A.B.0 (without the RCn).
* From the Command Line: Push the new tag to origin:
    * `git push origin --tags`
* On the GitHub component site document the release 

### Update Website Downloads.md "Latest Source Zip Files" Table
* This script assumes that the remote *.../dist/release/datasketches/...* directories are up-to-date with no old releases.  
* Start a new terminal in the *../dist/dev/datasketches/scripts* directory on your system:
* Make sure your local website directory is pointing to master/main and up-to-date. 
* Run the following with the argument specifying the location of your local website directory:
    * `./createDownloadsInclude.sh /Users/<name>/ ... /datasketches-website`
* When this is done, be sure to commit the changes to the website.

### Update Website Documentation
 * From the *github.com/datasketches-\<component\>* website, run JavaDoc GitHub action for the release tag:
    * On the component website (github.com/apache/datasketches-\<component\>), go to *Actions* and select the *JavaDoc* workflow.
    * Open the *Run workflow* pull-down, then in the *Branch:master/main* pull-down, select the correct release tag instead of the branch. 
    * Run the workflow and check that it finished successfully.
    * Return to the component website home, select the *gh-pages* branch and check that it created *docs/X.Y.Z* directory
 * From your local *datasketches-website* directory:
     * Open the *_includes.html* directory and open the *javadocs.html* file in a text editor.
     * Update the tag at the end of the link to the just-released component *X.Y.Z* tag.
 * Visit *apache.github.io/datasketches-"component"/X.Y.Z* to confirm.
 * Commit the changes to the website

### Prepare Announce Letter to dev@
* ASF requests that you wait 24 hours to publish Announce letter to allow the propagation to mirrors.
* Use recent template
* Summarize vote results

## Update These Instructions
* If you have updated this file or any of the scripts, please update this file on the [website](https://datasketches.apache.org/docs/Community/ReleaseProcessForJavaComponents.html) and dist/dev/datasketches for the scripts.
