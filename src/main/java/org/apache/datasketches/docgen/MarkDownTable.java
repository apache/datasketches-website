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

import static java.lang.Math.max;

/**
 * General purpose MarkDown table generator for GitHub Flavored MarkDown.
 * This produces readable source and particularly useful when the data of the
 * table is machine generated.
 * @author Lee Rhodes
 */
public class MarkDownTable {
  private static final String LS = System.getProperty("line.separator");
  private static final int mcw = 7; //min column width

  /**
   * Emit a table row
   * @param colWidths integers representing the column widths of each column.
   * This only makes the markDown source look nice.
   * @param strings the content of each column
   * @return the source table row
   */
  public static String emitRow(final int[] colWidths, final String ... strings) {
    final int cols = colWidths.length;
    if (cols != strings.length) {
      throw new IllegalArgumentException("Unequal # of columns");
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cols; i++) {
      final String s = strings[i];
      final int w = colWidths[i];
      if (s.isEmpty()) {
        sb.append(pad(s, max(w, mcw))).append("|");
      } else {
        sb.append(pad(s, max(w, mcw))).append("|");
      }
    }
    sb.append(LS);
    return sb.toString();
  }

  /**
   * Emit a table row where all columns are bold face type
   * @param colWidths integers representing the column widths of each column.
   * This only makes the markdown source look nice.
   * @param strings the content of each column
   * @return the source table row in bold
   */
  public static String emitBoldRow(final int[] colWidths, final String ... strings) {
    final int cols = colWidths.length;
    if (cols != strings.length) {
      throw new IllegalArgumentException("Unequal # of columns");
    }
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cols; i++) {
      final String s = strings[i];
      final int w = colWidths[i];
      if (s.isEmpty()) {
        sb.append(pad(s, max(w, mcw))).append("|");
      } else {
        final String s2 = "<b>" + s + "</b>";
        sb.append(pad(s2, max(w, mcw))).append("|");
      }
    }
    sb.append(LS);
    return sb.toString();
  }

  /**
   * Emit the alignment line for GitHub MarkDown tables
   * @param colWidths the maximum width for each column
   * @param align a sequence of #columns characters in column order:
   * "C"=center, "L"=left, "R"=right
   * @return the alignment line
   */
  public static String emitAlignLine(final int[] colWidths, final String align) {
    final int cols = colWidths.length;
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cols; i++) {
      final char c = align.charAt(i);
      final int w = colWidths[i];
      if (c == 'C') {
        final int numDash = max(w - 2, mcw - 2);
        sb.append(":").append(dupDash(numDash)).append(":|");
      }
      else if (c == 'L') {
        final int numDash = max(w - 1, mcw - 1);
        sb.append(":").append(dupDash(numDash)).append("|");
      }
      else {
        final int numDash = max(w - 1, mcw - 1);
        sb.append(dupDash(numDash)).append(":|");
      }
    }
    sb.append(LS);
    return sb.toString();
  }

  private static String dupDash(final int num) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < num; i++) { sb.append("-"); }
    return sb.toString();
  }

  private static String pad(final String s, final int num) {
    final StringBuilder sb = new StringBuilder();
    final int strLen = s.length();
    if (strLen >= num) { return s; }
    sb.append(s);
    for (int i = 0; i < (num - strLen); i++) {
      sb.append(" ");
    }
    return sb.toString();
  }
}
