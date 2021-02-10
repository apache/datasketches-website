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

# Apache<sup>&reg;</sup> DataSketches&trade; Website

This is the DataSketches website source.  Please visit the main [DataSketches website](https://datasketches.apache.org) for more information.

If you are interested in making contributions to this site please see our [Community](https://datasketches.apache.org/docs/Community/) page for how to contact us.

### How the website works
- The website is published directly from a specially named *asf-site* branch. The content of this branch is generated automatically by *Jekyll* from the *master* branch whenever changes are detected in the *master* branch.  One should never modify the content of the *asf-site* directly.

- The *master* branch consists primarily of GitHub compatible *MarkDown* documents, which hold all the written content. 

- There are two navigation mechanisms on the site to help the user find documents: the *nav_bar* at the top of each page and the table-of-contents *toc* drop-down menus on the left of each page.  Individual pages can link to each other using standard MarkDown links.

### How to contribute content to the website

In order to contribute changes to the website content, you will need to fork this repository to your own GitHub profile.

If you only need to change an existing page, edit the relevant MarkDown page locally and submit a pull-request to *master*.  

However, if you need to add a new page to the website, you may need to modify the *toc* to enable users to find it:

- Create the new MarkDown document with the appropriate layout definition, and copyright notice.  This can be copied from any of the existing pages. The types of available layouts can be found in the */_layouts/* directory. Almost all site documents use the *doc_page* layout.  Place the new page in an appropriate subdirectory in *master*.

- The *toc* is generated statically by the developer/author, when it needs to be updated, by running a small Java program called `TocGenerator.java` located in */src/main/java/org/datasketches/docgen/*.  The TocGenerator takes input from the */src/main/resources/docgen/toc.json* file and save the output html in */_includes/toc.html* in *master*.  Please do not edit the *toc.html* file directly. 

- The *toc.json* file is pretty easy to figure out. It is a tree structure of two types of elements, *Dropdown* and *Doc*. Each element has 4 or 5 *key:valu*e pairs. Make sure you structure the JSON correctly with matching braces and brackets, and with commas between tree elements.

- Run the table of contents generator.  The `runTocGenerator` method is a static member of `TocGenerator.java`. You can run this from your preferred IDE or from the command line.  You should see the genenerated HTML as output to the console.

- Once you have run the generator, ensure that your new entry is included in the `toc.html` file under the `_includes` subdirectory.
- If you have Jekyll installed on your computer you can visually check the *toc* for proper operation before submitting your PR.

- Lastly, submit your pull request for review!

