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

# Apache DataSketches Website

This is the DataSketches website source.  Please visit the main [DataSketches website](https://datasketches.apache.org) for more information.

If you are interested in making contributions to this site please see our [Community](https://datasketches.apache.org/docs/Community/) page for how to contact us.

### How the documentation website works

The page content for the website is rendered directly from markdown documents.  These markdown documents are all referenced according to a table of contents JSON document.  The HTML for the website navigation is generated dynamically from the JSON structure.

### How to contribute content to the website

In order to contribute changes to the website content, you will need to fork this repository to your own GitHub profile.

If you would like to change an existing page, changes can be made directly to the relevant markdown document, and a pull request submitted.  However, should you intend to add a new page to the website, you will need to:

- Create a new markdown document with the appropriate layout definition, and copyright notice.  This can be copied from any of the existing pages.

- Ensure that there is a new entry in the table of contents JSON document. This file is named `toc.json`, and may be located in the `resources/docgen` subdirectory.

- Run the table of contents generator.  The `runTocGenerator` method is a static member of `TocGenerator.java`. You can run this from your preferred IDE or from the command line.  You should see the genenerated HTML as output to the console.

- Once you have run the generator, ensure that your new entry is included in the `toc.html` file under the `_includes`
  subdirectory.

- Lastly, submit your pull request for review!

----

Disclaimer: Apache DataSketches is an effort undergoing incubation at The Apache Software Foundation (ASF), sponsored by the Apache Incubator. Incubation is required of all newly accepted projects until a further review indicates that the infrastructure, communications, and decision making process have stabilized in a manner consistent with other successful ASF projects. While incubation status is not necessarily a reflection of the completeness or stability of the code, it does indicate that the project has yet to be fully endorsed by the ASF.
