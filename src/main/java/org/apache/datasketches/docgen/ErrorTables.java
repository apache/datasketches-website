/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.datasketches.docgen;

import static org.apache.datasketches.docgen.MarkDownTable.emitAlignLine;
import static org.apache.datasketches.docgen.MarkDownTable.emitBoldRow;
import static org.apache.datasketches.docgen.MarkDownTable.emitRow;

import org.testng.annotations.Test;

/**
 * Builds the error tables for Theta QuickSelect and Alpha sketches.
 * @author Lee Rhodes
 */

public class ErrorTables {
  static final String LS = System.getProperty("line.separator");

  /**
   * Builds the QuickSelect error table
   */
  @Test
  public void buildQuickSelectTable() {
    buildTable(1.0, 4);
  }

  /**
   * Builds the Alpha error table
   */
  @Test
  public void buildAlphaTable() {
    buildTable(.708, 9);
  }

  private static void buildTable(final double factor, final int lgKlow) {
    final StringBuilder sb = new StringBuilder();
    final int[] colWidths = {10, 12, 7, 7, 7 };
    final double f = factor; //.708 for Alpha, 1.0 for QuickSelect
    //the &nbsp; is required on the first line when Col#1 is blank for GitHub to render properly.
    //MackDown doesn't care.
    sb.append(emitRow(colWidths, "&nbsp;","#Std Dev:", "1","2","3"));
    sb.append(emitAlignLine(colWidths, "CCCCC"));
    sb.append(emitBoldRow(colWidths,"","Conf:", "68.27%","95.45%","99.73%"));
    sb.append(emitBoldRow(colWidths,"LgK","K", "1 RSE","2 RSE","3 RSE"));
    for (int lgK = lgKlow; lgK <= 26; lgK++) { //for Alpha start with 9, QuickSelect start with 4
      final String lgKStr = Integer.toString(lgK);
      sb.append(emitRow(colWidths,lgKStr, k(lgK), rse(lgK, 1, f), rse(lgK,2,f), rse(lgK,3,f)));
    }
    println(sb.toString());
  }

  static String k(final int lgK) {
    final int lgk = 1 << lgK;
    return String.format("%,d", lgk);
  }

  static String rse(final int lgK, final int stdDev, final double factor) {
    final double k = 1 << lgK;
    final double rse = stdDev * factor * 100.0 / Math.sqrt(k - 1.0);
    return String.format("%6.3f%%", rse);
  }

  static void println(final Object o) { System.out.println(o.toString()); }

  static void printf(final String fmt, final Object ... args) {
    System.out.printf(fmt, args);
  }

}
