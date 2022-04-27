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

* This process covers major and minor releases only. Bug-fix releases, which increment the third digit, are performed on a A.B.X branch and not on master, but otherwise are similar.
* Some of these operations can be performed either on the Command-Line or in your IDE, whatever you prefer.

## Preparation
* Confirm correctness for
    * LICENSE
    * NOTICE -- check for copyright dates
    * README.md
    * .asf.yaml
    * .travis.yml (if used)
    * .gitattributes -- used to exclude files from release zip, assumes .gitignore
    * .github/workflows
    * .gitignore -- used to exclude files from origin
    * pom.xml / apache-rat-plugin -- checks for license headers, assumes .gitignore
    * pom.xml
         
* From Command Line or IDE:
    * Run Unit tests
    * Run Code Coverage > 90%
    * Run SpotBugs checks (is it properly configured?)
    * Run Checkstyle (is it properly configured?)
    * Confirm that all __temporary__ branches are checked into master and/or deleted, both local and remote.
    * Confirm any new bug fixes have corresponding tests

* From Command Line at Component root:
  * To confirm *gpg-agent* is running type:
      * `ps -axww | grep gpg`  # you should see something like:
          * <pre>9566 ??         0:30.33 gpg-agent --daemon<br>20315 ttys000   0:00:00 grep gpg</pre>
      * To start GPG if GPG Agent is not running:
          * `eval $(gpg-agent --daemon)`
  * Confirm GitHub repository is current and git status is clean:
      * `git status` # should return:
      * "On branch master, your branch is up to date with 'origin/master', nothing to commit, working tree clean."
  * At major version releases, search for deprecated code and remove at __Major Versions__ only.
      * `find . -name "*.java" -type f -print | xargs grep -i -n -s -A0 "deprecated"`
      * **Note:** When first marking a segment of code deprecated, please add the current version number. This will make it easier to know when to remove the deprecated code.
  * Check Maven Plugin, Dependency, Property Versions of the POM:
      * `mvn versions:display-plugin-updates`
      * `mvn versions:display-dependency-updates`
      * `mvn versions:display-property-updates`
  * Maven Tests:
      * `mvn apache-rat:check`
      * `mvn clean test`
      * `mvn clean test -P strict`
      * `mvn clean javadoc:javadoc`
      * `mvn clean install -DskipTests=true`
      * Check that the /target/ directory has 5 jars: (may need to refresh)
          * datasketches-\<component\>-SNAPSHOT-javadoc.jar
          * datasketches-\<component\>-SNAPSHOT-sources.jar
          * datasketches-\<component\>-SNAPSHOT-test-sources.jar
          * datasketches-\<component\>-SNAPSHOT-tests.jar
          * datasketches-\<component\>-SNAPSHOT.jar

      * Check your local Maven repository
          * _~/.m2/repository/org/apache/datasketches/datasketches-\<component\>/A.B.0-SNAPSHOT/_ 
          * It should have 5 new jars and a .pom file. 
 

## Create Permanent Release Branch & POM Version Preparation
* Assume current master POM version = A.B.0-SNAPSHOT
* From IDE or Command Line: 
    * Switch from Master to new __Permanent Branch__: "A.B.X"
    * Edit pom.xml version to A.B.0 (remove -SNAPSHOT, do not change A or B)
    * Commit the change. __DO NOT PUSH!__
    * Create Annotated TAG: A.B.0-RC1 (or RCn)
    * Write down the Git hash : example: 40c6f4f
    * Now Push Branch  "A.B.X" with edited pom.xml to origin
    * __DO NOT MERGE THIS PERMANENT BRANCH INTO MASTER__
* From IDE or Command-line: 
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
    * Edit master pom.xml to A'.B'.0-SNAPSHOT where A' or B' will be incremented by 1. (Bug fix releases will change the 3rd digit)
    * Commit and Push this change to origin/master with the comment "Release Process: Change pom version to A'.B'.0-SNAPSHOT."
    * Create a tag A'.B'.0-SNAPSHOT on master at the HEAD.
    * Push the tag to origin: `git push origin --tags`
    * Return to release branch A.B.X
    * You may minimize your IDE, pointing at the release branch.

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
  * Project.artifactId : datasketches-\<component\> where component is e.g., java, pig, hive,...
  * GitHub Tag: A.B.0-RC1 (or RCn)
  * Have your GPG passphrase handy -- you have only a few seconds to enter it!
* Start a new terminal in the above dist/dev/datasketches/scripts directory on your system:
  * To confirm *gpg-agent* is running type:
      * `ps -axww | grep gpg`  # you should see something like:
          * <pre>9566 ??         0:30.33 gpg-agent --daemon<br>20315 ttys000   0:00:00 grep gpg</pre>
      * To start GPG if GPG Agent is not running:
          * `eval $(gpg-agent --daemon)` 
  * Run something like:
    * `./bashDeployToDist.sh /Users/\<name\>/dev/git/Apache/datasketches-\<component\> datasketches-\<component\> A.B.0-RC1`
    * Follow the instructions.
    * NOTE: if you get the error "gpg: signing failed: No pinentry":
        * open .gnupg/gpg-agent.conf
        * change to: pinentry-program */usr/local/bin/pinentry-tty*
        * reload the gpg agent in the terminal: `gpg-connect-agent reloadagent /bye` 
        * restart the *./bashDeployToDist* script
    * Close the terminal
* Check this web URL ~ *https://dist.apache.org/repos/dist/dev/datasketches/\<component\>/A.B.0-RC1/*
    * There should be 3 files: \*-src.zip, \*-src.zip.asc, \*-src.zip.sha512
    * Copy the URL for later.

### Java: Push Jars to Nexus (Maven Central) Staging
* Return to original terminal at the project.basedir, still in the A.B.X branch.
* If starting new terminal make sure GPG is running:
  * To confirm *gpg-agent* is running type:
      * `ps -axww | grep gpg`  # you should see something like:
          * <pre>9566 ??         0:30.33 gpg-agent --daemon<br>20315 ttys000   0:00:00 grep gpg</pre>
      * To start GPG if GPG Agent is not running:
          * `eval $(gpg-agent --daemon)` 
* `git status` # make sure you are still on the release branch: A.B.X
* TRIAL-RUN:
  * **Have your GPG passphrase handy -- you have only a few seconds to enter it!**
  * `mvn clean install -Pnexus-jars -DskipTests=true`
      * Check target/ that jars & pom have .asc signatures
* DEPLOY
  * **Have your GPG passphrase handy -- you have only a few seconds to enter it, but it may be automatic!**
  * `mvn clean deploy -Pnexus-jars -DskipTests=true`
      * Login to [repository.apache.org](https://repository.apache.org/) / Staging Repositories for orgapachedatasketches-XXXX
      * Click Content and search to the end.  Each jar & pom should have .asc, .md5, .sha1 signatures
      * [CLOSE] the Staging Repository with a comment: "\<component\> A.B.0"
      * Confirm its existance under Repositories/Staging web-site URL (in the summary window)
      * Grab its URL while there. You will need it for the Vote Letter.
      * Check your local Maven repository
          * _~/.m2/repository/org/apache/datasketches/datasketches-\<component\>/A.B.0/_ 
          * It should have 5 new jars and a .pom file each with .asc, .md5, and .sha1 signatures

### Create Copy of External Artifact Distributions
#### JAVA
* Place copies of the artifact jars deployed to Nexus under a "maven" directory.  For example see <https://dist.apache.org/repos/dist/dev/datasketches/memory/1.3.0-RC1/>
* Note that the `jar` files with their `asc`, `md5` and `sha1` signature are all together in the .md2 archive 
* Add a `maven` directory under the `dist/dev/datasketches/\<component\>/A.B.0/`
* Bulk copy the `jar, asc, md5` and `sha1` files into the `maven` directory.
* Do: `svn status` # check to see if it is ready to add
* Do: `svn add . --force`
* Do: `svn ci -m "add nexus jars to dist/dev/datasketches"`

#### Non-Java
* For external artifacts such as Python or Docker the subdirectory name should be relevant to the type.
* These must be signed with GPG (.asc) and SHA512 (.sha512)

## Prepare & Send [VOTE] Letter to dev@
* See VoteTemplates directory for a recent example
* If vote is not successful, fix the problem and repeat above steps.
* After a successful vote return to __this point__ and continue ...

## Prepare & Send [VOTE-RESULT] Letter to dev@
* See VoteTemplates directory for a recent example
* Declare that the vote is closed.
* Summarize vote results

## Finalize the Release

### Copy files from *dist/dev* to *dist/release*
* In local *dist/__dev__/datasketches/*
    * Open Terminal #1
        * Confirm you are in the `/dev/` directory: `pwd`
        * Perform SVN Checkout:
            * `svn co https://dist.apache.org/repos/dist/dev/datasketches/ .`  #note dot at end
            * `svn status` #make sure checkout is clean
* In local *dist/__release__/datasketches/*
    * Open Terminal #2
        * Confirm you are in the `/release/` directory: `pwd` 
        * Perform SVN Checkout:
            * `svn co https://dist.apache.org/repos/dist/release/datasketches/ .` #note dot at end
            * `svn status` #make sure checkout is clean
        * Create new version directory under appropriate component directory:
            * `mkdir -p \<component\>/A.B.0`
    * Using local file system copy files 
        * From  ... /dist/dev/datasketches/\<component\>/version-RCnn/*
        * To    ... /dist/release/datasketches/\<component\>/version (no RCnn)/*
        * Make sure to move External Artifact Distributions *dist/dev* to *dist/release*
    * Using Terminal #2 at ... /dist/release/datasketches directory:
        * `svn add . --force`
        * `svn ci -m "Release A.B.0"`
        * Remove the prior release...
        * `svn remove \<component\>/X.Y.0`
        * `svn ci -m "Remove Prior release"`
        * `svn status` # should be empty
    * Using local file system
        * Delete the prior X.Y.0 directory if necessary.

### Java: Release Jars on Nexus Staging
* On Nexus [repository.apache.org](https://repository.apache.org/) click on Staging Repositories
* Select "orgapachedatasketches-XXXX" (If more than one make sure you select the right one!)
* At the top of the window, select "Release"
* Confirm that the attributes have moved to the "Releases" repository under "Repositories"
    * Browse to *Releases/org/apache/datasketches/...*

### Java: Drop any previous Release Candidates that were not used.
* On Nexus [repository.apache.org](https://repository.apache.org/) click on Staging Repositories
* Select "orgapachedatasketches-XXXX" (If more than one make sure you select the right one!)
* At the top of the window, select "Drop"

### If necessary, update branch *master* from branch *A.B.X*
If you have gone through more than one Release Candidate, you may have changes that need to be 
reflected in the master. Use the **git cherry-pick** command for this.  

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
* Make sure your local website directory is pointing to master and up-to-date. 
* Run the following with the argument specifying the location of your local website directory:
    * `./createDownloadsInclude.sh /Users/\<name\>/ ... /datasketches-website`
* When this is done, be sure to commit the changes to the website.

### Update Javadocs (or Equivalent) on Website

### Update Website Documentation (if new functionality)

### Prepare Announce Letter to dev@
* ASF requests that you wait 24 hours to publish Announce letter to allow the propagation to mirrors.
* Use recent template
* Summarize vote results

## Update These Instructions
* If you have updated this file or any of the scripts, please update this file on the [website](https://datasketches.apache.org/docs/Community/ReleaseProcessForJavaComponents.html) and dist/dev/datasketches for the scripts.
