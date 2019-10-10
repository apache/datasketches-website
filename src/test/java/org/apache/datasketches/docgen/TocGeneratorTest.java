/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.datasketches.docgen;

import org.testng.annotations.Test;

import org.apache.datasketches.docgen.TocGenerator;

/**
 * To create the Table of Contents for the website:
 * <ol>
 *  <li>Edit the JSON source file (refereced below) for the structure you want.</li>
 *  <li>Execute this test.  The result will be placed in the proper location as part of the web
 *  source.</li>
 *  <li>Stage the changes and push the web site source to origin.</li>
 *  <li>Refresh your browser and confirm that the TOC is correct.
 * </ol>
 * @author Lee Rhodes
 */
@SuppressWarnings("javadoc")
public class TocGeneratorTest {

  @Test
  public void testTocGenerator() {
    final String jsonSrcFile = "src/main/resources/docgen/toc.json";
    final String htmlScriptFile = "src/main/resources/docgen/tocScript.html";
    final String tgtTocFile = "_includes/toc.html";
    TocGenerator tocgen = new TocGenerator(jsonSrcFile, htmlScriptFile, tgtTocFile);
    tocgen.readJson();
  }

}
