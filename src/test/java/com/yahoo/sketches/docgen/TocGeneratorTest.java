/*
 * Copyright 2019, Yahoo! Inc. Licensed under the terms of the
 * Apache License 2.0. See LICENSE file at the project root for terms.
 */

package com.yahoo.sketches.docgen;

import org.testng.annotations.Test;

import com.yahoo.sketches.docgen.TocGenerator;

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
