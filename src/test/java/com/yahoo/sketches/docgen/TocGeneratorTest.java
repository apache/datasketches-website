/*
 * Copyright 2019, Yahoo! Inc. Licensed under the terms of the
 * Apache License 2.0. See LICENSE file at the project root for terms.
 */

package com.yahoo.sketches.docgen;

import org.testng.annotations.Test;

import com.yahoo.sketches.docgen.TocGenerator;

/**
 * @author Lee Rhodes
 */
public class TocGeneratorTest {

  @Test
  public void testTocGenerator() {
    final String jsonSrcFile = "src/main/resources/docgen/toc.json";
    final String htmlScriptFile = "src/main/resources/docgen/tocScript.html";
    final String tgtTocFile = "src/main/resources/docgen/toc.html";
    TocGenerator tocgen = new TocGenerator(jsonSrcFile, htmlScriptFile, tgtTocFile);
    tocgen.readJson();
  }

}
