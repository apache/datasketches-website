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

package org.apache.datasketches;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A collection of useful static file handlers that conveniently convert the
 * java.io checked exceptions into runtime exceptions.
 *
 * @author Lee Rhodes
 */
@SuppressWarnings("javadoc")
public final class Files {
  private static final String LS = System.getProperty("line.separator");
  private static final byte CR = 0xD;
  private static final byte LF = 0xA;
  public static final int DEFAULT_BUFSIZE = 8192;

  // Common IO & NIO file methods

  /**
   * If the fileName string is null or empty, this method throws a
   * RuntimeException.
   *
   * @param fileName the given fileName
   * @throws RuntimeException if fileName is null or empty.
   */
  public static void checkFileName(final String fileName) {
    if (fileName == null) {
      throw new RuntimeException(LS + "FileName is null.");
    }
    if (fileName.length() == 0) {
      throw new RuntimeException(LS + "FileName is empty.");
    }
    return;
  }

  /**
   * Gets an existing normal file as a File. If fileName is null, or empty, or
   * if the file is actually a directory, or doesn't exist this will throw a
   * Runtime Exception; otherwise it will return the fileName as a File object.
   *
   * @param fileName the given fileName
   * @return a File object
   * @throws RuntimeException if fileName cannot resolve to a existing normal
   * file.
   */
  public static File getExistingFile(final String fileName) {
    checkFileName(fileName);
    final File file = new File(fileName);
    if (file.isFile()) {
      return file;
    }
    if (file.isDirectory()) {
      throw new RuntimeException(LS + "FileName is a Directory: " + fileName);
    }

    throw new RuntimeException(LS + "FileName does not exist: " + fileName);
  }

  /**
   * Returns true if file is a normal file and not a directory.
   *
   * @param fileName the given fileName
   * @return true if file is a normal file and not a directory.
   * @throws RuntimeException if fileName is null or empty.
   */
  public static boolean isFileValid(final String fileName) {
    checkFileName(fileName);
    final File file = new File(fileName);
    return file.isFile();
  }

  /**
   * Gets the System.getProperty("user.dir"), which is the expected location of
   * the user root directory.
   *
   * @return location of user root directory
   */
  public static String getUserDir() {
    return System.getProperty("user.dir");
  }

  /**
   * Opens a RandomAccessFile given a File object and String mode: "r", "rw",
   * "rws" or "rwd". The returned object must be closed by the calling program.
   *
   * @param file the given file
   * @param mode the given mode
   * @return RandomAccessFile
   * @throws RuntimeException if an IOException occurs.
   */
  public static RandomAccessFile openRandomAccessFile(final File file, final String mode) {
    final RandomAccessFile raf;
    try {
      raf = new RandomAccessFile(file, mode);
    } catch (final FileNotFoundException e) {
      throw new RuntimeException(LS + "Failed to open RandomAccessFile " + LS + e);
    }
    return raf;
  }

  /**
   * Gets the FileDescriptor from the given RandomAccessFile.
   *
   * @param raf RandomAccessFile
   * @return the FileDescriptor
   * @throws RuntimeException if an IOException occurs.
   */
  public static FileDescriptor getFD(final RandomAccessFile raf) {
    FileDescriptor fd = null;
    try {
      fd = raf.getFD();
    } catch (final IOException e) {
      throw new RuntimeException(LS + "RandomAccessFile.getFD() failure" + LS + e);
    }
    return fd;
  }

  /**
   * Sets the position of the given RandomAccessFile to the given position.
   *
   * @param raf RandomAccessFile
   * @param position the given position
   * @throws RuntimeException if an IOException occurs.
   */
  public static void seek(final RandomAccessFile raf, final long position) {
    try {
      raf.seek(position);
    } catch (final IOException e) {
      throw new RuntimeException(LS + "RandomAccessFile seek failure" + LS + e);
    }
  }

  /**
   * Reads buf.length bytes into the given buf.
   *
   * @param raf RandomAccessFile
   * @param buf the size of this buffer is the number of bytes requested.
   * @return the number of bytes actually read.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int readBytes(final RandomAccessFile raf, final byte[] buf) {
    final int len = buf.length;
    int read = 0;
    try {
      read = raf.read(buf);
    } catch (final IOException e) {
      throw new RuntimeException(LS + "RandomAccessFile read failure" + LS + e);
    }
    if (read < len) {
      Arrays.fill(buf, read, len, (byte) 0);
    }
    return read;
  }

  // ************************
  // NIO OPERATIONS
  // ************************
  // ByteBuffer methods
  /**
   * Gets a MappedByteBuffer from the given FileChannel, mode, position and
   * size.
   *
   * @param fChan the given FileChannel
   * @param mmode the given MapMode
   * @param position the given position
   * @param size the given size
   * @return a MappedByteBuffer
   * @throws RuntimeException if an IOException occurs.
   */
  public static MappedByteBuffer getMappedByteBuffer(final FileChannel fChan,
      final FileChannel.MapMode mmode, final long position, final long size) {
    final MappedByteBuffer mbBuf;
    try {
      mbBuf = fChan.map(mmode, position, size);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return mbBuf;
  }

  /**
   * Gets a MappedByteBuffer from the given FileChannel and mode. Assumes a
   * start position of zero and size of the length of the file (via the
   * FileChannel. Equivalent to:<br>
   * getMappedByteBuffer(FileChannel, FileChanel.MapMode, 0L, size(fChan));
   *
   * @param fChan the given FileChannel
   * @param mmode the given MapMode
   * @return a MappedByteBuffer
   * @throws RuntimeException if an IOException occurs.
   */
  public static MappedByteBuffer getMappedByteBuffer(final FileChannel fChan,
      final FileChannel.MapMode mmode) {
    return getMappedByteBuffer(fChan, mmode, 0L, size(fChan));
  }

  /**
   * Reads bytes from the given (Mapped)ByteBuffer until either a CR, LF or CRLF
   * is detected in the byte stream and then converts the captured bytes,
   * excluding the CR and LF characters into a string. This method will work
   * with US-ASCII, ISO-8859 families, Windows 1252, and UTF-8. encodings. In
   * general any character encoding that is isomorphic with respect to the
   * exclusive use of the CR (0xD) and the LF (0xA) codes. Equivalent to:<br>
   * readLine(ByteBuffer, ByteArrayBuilder, Charset.defaultCharset());
   *
   * @param mbBuf Given ByteBuffer or MappedByteBuffer
   * @param bab an optional ByteArrayBuilder for internal reuse, which will
   * improve multi-line reading performance. The result of the read as an array
   * of bytes is available from the bab.
   * @return the line as a string
   *
   */
  public static String readLine(final ByteBuffer mbBuf, final ByteArrayBuilder bab) {
    return readLine(mbBuf, bab, Charset.defaultCharset());
  }

  /**
   * Reads bytes from the given (Mapped)ByteBuffer until either a CR, LF or CRLF
   * is detected in the byte stream and then converts the captured bytes,
   * excluding the CR and LF characters into a string. This method will work
   * with US-ASCII, ISO-8859 families, Windows 1252, and UTF-8. encodings. In
   * general any character encoding that is isomorphic with respect to the
   * exclusive use of the CR (0xD) and the LF (0xA) codes.
   *
   * @param mbBuf Given ByteBuffer or MappedByteBuffer
   * @param bab an optional ByteArrayBuilder for internal reuse, which will
   * improve multiline reading performance. The result of the read as an array
   * of bytes is available from the bab.
   * @param charset The Charset to use when converting arrays of bytes from the
   * source to a Unicode String (UTF-16).
   * @return The characters of a line, or NULL if End-of-File, or "" if line was
   * empty.
   */
  public static String readLine(final ByteBuffer mbBuf, final ByteArrayBuilder bab,
      final Charset charset) {
    if (!mbBuf.hasRemaining()) {
      return null;
    }
    final ByteArrayBuilder bab1;
    if (bab == null) {
      bab1 = new ByteArrayBuilder();
    } else {
      bab1 = bab;
      bab1.setLength(0);
    }
    while (mbBuf.hasRemaining()) {
      final byte b = mbBuf.get();
      if (b == LF) {
        break; // EOL
      }
      if (b == CR) {
        if (mbBuf.hasRemaining()) {
          // peek next byte without moving position
          if (mbBuf.get(mbBuf.position()) == LF) {
            mbBuf.get(); // consume it
          }
        }
        break; // EOL
      }
      bab1.append(b); // transfer the byte
    }
    if (bab1.length() == 0) {
      if (!mbBuf.hasRemaining()) {
        return null;
      }
      return "";
    }
    final byte[] out = bab1.toByteArray();
    final String s = new String(out, charset);
    return s;
  }

  /**
   * Reads a ByteBuffer (or subclass) with a request for numBytes. The result is
   * stuffed into the provided byte[] array (required), which must be larger or
   * equal to numBytes.
   *
   * @param bb The ByteBuffer to read from
   * @param numBytes The requested number of bytes to read.
   * @param out The target array for the bytes.
   * @return the actual number of bytes read.
   * @throws BufferUnderflowException if numBytes is greater than bytes
   * available in the buffer.
   */
  public static int readByteBuffer(final ByteBuffer bb, final int numBytes, final byte[] out) {
    final int rem = bb.remaining();
    if (rem == 0) {
      return 0;
    }
    final int nBytes = (rem < numBytes) ? rem : numBytes;
    bb.get(out);
    return nBytes;
  }

  // FileChannel methods
  /**
   * Sets the FileChannel position.
   *
   * @param fc FileChannel
   * @param position the position
   * @throws RuntimeException if an IOException occurs.
   */
  public static void position(final FileChannel fc, final long position) {
    try {
      fc.position(position);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets the current size of the FileChannel.
   *
   * @param fc FileChannel
   * @return the size in bytes.
   * @throws RuntimeException if an IOException occurs.
   */
  public static long size(final FileChannel fc) {
    final long sz;
    try {
      sz = fc.size();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return sz;
  }

  /**
   * Appends the given string to the end of the file specified via the given
   * FileChannel. Equivalent to:<br>
   * append(String, FileChannel, Charset.defaultCharset());
   *
   * @param out the string to append
   * @param fc the given FileChannel
   * @return the number of bytes actually appended.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int append(final String out, final FileChannel fc) {
    return append(out, fc, Charset.defaultCharset());
  }

  /**
   * Appends the given string to the end of the file specified via the given
   * FileChannel.
   *
   * @param out the string to append
   * @param fc the given FileChannel
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @return the number of bytes actually appended.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int append(final String out, final FileChannel fc, final Charset charset) {
    final int bytes;
    final ByteBuffer bb = ByteBuffer.wrap(out.getBytes(charset));
    try {
      bytes = fc.write(bb, fc.size());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return bytes;
  }

  /**
   * Appends the given byteArr to the end of the file specified via the given
   * FileChannel.
   *
   * @param byteArr the byte[] to append
   * @param fc the given FileChannel
   * @return the number of bytes actually appended.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int append(final byte[] byteArr, final FileChannel fc) {
    final int bytes;
    final ByteBuffer bb = ByteBuffer.wrap(byteArr);
    try {
      bytes = fc.write(bb, fc.size());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return bytes;
  }

  /**
   * Writes the given string out to the file specified via the given FileChannel
   * starting at the given file position. Equivalent to:<br>
   * write(String, FileChannel, long, Charset.defaultCharset());
   *
   * @param out the given string
   * @param fc FileChannel
   * @param position the position
   * @return the total number of bytes written.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int write(final String out, final FileChannel fc, final long position) {
    return write(out, fc, position, Charset.defaultCharset());
  }

  /**
   * Writes the given string out to the file specified via the given FileChannel
   * starting at the given file position.
   *
   * @param out the given string
   * @param fc FileChannel
   * @param position the given position
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @return the total number of bytes written.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int write(final String out, final FileChannel fc, final long position,
      final Charset charset) {
    final int bytes;
    final ByteBuffer bb = ByteBuffer.wrap(out.getBytes(charset));
    try {
      bytes = fc.write(bb, position);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return bytes;
  }

  /**
   * Writes the given byteArr to the file specified via the given FileChannel at
   * the given position.
   *
   * @param byteArr the byte[] to append
   * @param fc the given FileChannel
   * @param position the given position
   * @return the number of bytes actually appended.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int write(final byte[] byteArr, final FileChannel fc, final long position) {
    final int bytes;
    final ByteBuffer bb = ByteBuffer.wrap(byteArr);
    try {
      bytes = fc.write(bb, position);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return bytes;
  }

  // Complete NIO BASED FILE WRITE OPERATIONS
  /**
   * Writes the given String text to the fileName using NIO FileChannel.
   *
   * @param text Source string to place in a file. Equivalent to: <br>
   * stringToFileNIO(String, String, Charset.defaultCharset());
   * @param fileName name of target file
   * @return the total number of bytes written.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int stringToFileNIO(final String text, final String fileName) {
    return stringToFileNIO(text, fileName, Charset.defaultCharset());
  }

  /**
   * Writes the given String text to the fileName using NIO FileChannel
   *
   * @param text Source string to place in a file.
   * @param fileName name of target file
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @return the total number of bytes written.
   * @throws RuntimeException if an IOException occurs.
   */
  public static int stringToFileNIO(final String text, final String fileName,
      final Charset charset) {
    checkFileName(fileName);
    final File file = new File(fileName);
    int bytes = 0;
    if (!file.isFile()) {
      try {
        file.createNewFile();
      } catch (final IOException e) {
        throw new RuntimeException("Cannot create new file: " + fileName + LS + e);
      }
      try (FileChannel fc = openRandomAccessFile(file, "rw").getChannel()) {
        bytes = write(text, fc, 0L, charset);
      } catch (final IOException e) {
        throw new RuntimeException("Cannot create File Channel: " + fileName + LS + e);
      }
    }

    return bytes;
  }

  /**
   * Appends a String to a file using NIO. If fileName does not exist, this
   * creates a new empty file of that name. This closes the file after
   * appending.
   *
   * @param text is the source String. Equivalent to: <br>
   * appendStringToFileNIO(String, String, Charset.defautCharset());
   * @param fileName the given fileName
   * @return the total number of bytes written
   * @throws RuntimeException if IOException or SecurityException occurs, or if
   * fileName is null or empty.
   */
  public static int appendStringToFileNIO(final String text, final String fileName) {
    return appendStringToFileNIO(text, fileName, Charset.defaultCharset());
  }

  /**
   * Appends a String to a file using NIO and a Charset. If fileName does not
   * exist, this creates a new empty file of that name. This closes the file
   * after appending.
   *
   * @param text is the source String.
   * @param fileName the given fileName
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @return the total number of bytes written
   * @throws RuntimeException if IOException or SecurityException occurs, or if
   * fileName is null or empty.
   */
  public static int appendStringToFileNIO(final String text, final String fileName,
      final Charset charset) {
    checkFileName(fileName);
    final File file = new File(fileName);
    if (!file.isFile()) { // does not exist
      try {
        file.createNewFile();
      } catch (final Exception e) {
        throw new RuntimeException("Cannot create file: " + fileName + LS + e);
      }
    }
    int bytes = 0;
    try (FileChannel fc = openRandomAccessFile(file, "rw").getChannel()) {
      bytes = append(text, fc, charset);
    } catch (final IOException e) {
      throw new RuntimeException("Cannot create File Channel: " + fileName + LS + e);
    }
    return bytes;
  }

  // Complete NIO BASED FILE READ OPERATIONS
  /**
   * Reads a file into a char array using NIO FileChannel, then closes the file. Useful when
   * special handling of Line-Separation characters is required. Equivalent to:
   * <br>
   * fileToCharArrayNIO(String, Charset.defaultCharset());
   *
   * @param fileName the given fileName
   * @return a char[]
   * @throws RuntimeException if IOException occurs.
   * @throws IllegalArgumentException if File size is greater than
   * Integer.MAX_VALUE.
   */
  public static char[] fileToCharArrayNIO(final String fileName) {
    return fileToCharArrayNIO(fileName, Charset.defaultCharset());
  }

  /**
   * Reads a file into a char array using NIO FileChannel, then closes the file. Useful when
   * special handling of Line-Separation characters is required.
   *
   * @param fileName the given fileName
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @return a char[]
   * @throws RuntimeException if IOException occurs.
   * @throws IllegalArgumentException if File size is greater than
   * Integer.MAX_VALUE.
   */
  public static char[] fileToCharArrayNIO(final String fileName, final Charset charset) {
    final File file = getExistingFile(fileName);
    char[] chArr = null;
    try (RandomAccessFile raf = openRandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel()) {
      final MappedByteBuffer mbBuf = getMappedByteBuffer(fc, READ_ONLY);
      final long len = size(fc);
      if (len > Integer.MAX_VALUE) {
        fc.close();
        throw new IllegalArgumentException("File size cannot exceed Integer.MAX_VALUE.");
      }
      final byte[] in = new byte[(int) len];
      mbBuf.get(in); // fill the buffer
      final String out = new String(in, charset);
      chArr = out.toCharArray();
    } catch (final IOException e) {
      throw new RuntimeException("Could not create or close File Channel.");
    }
    return chArr;
  }

  /**
   * Reads a file into a String using NIO FileChannel. Each line of the file is delimited by
   * the current operating systems's "line.separator" characters. Closes the
   * file. This method is equivalent to:<br>
   * fileToStringNIO(String fileName, Charset.defaultCharset())
   *
   * @param fileName given fileName
   * @return a String
   * @throws RuntimeException if IOException occurs.
   */
  public static String fileToStringNIO(final String fileName) {
    return fileToStringNIO(fileName, Charset.defaultCharset());
  }

  /**
   * Reads a file into a String using NIO FileChannel. Each line of the file is delimited by
   * the current operating systems's "line.separator" characters. Closes the
   * file.
   *
   * @param fileName given fileName
   * @param charset The Charset to use when converting arrays of bytes from the
   * source to a Unicode String (UTF-16).
   * @return a String
   * @throws RuntimeException if IOException occurs.
   */
  public static String fileToStringNIO(final String fileName, final Charset charset) {
    final File file = getExistingFile(fileName);
    final StringBuilder sb = new StringBuilder(1024);
    try (RandomAccessFile raf = openRandomAccessFile(file, "r");
        FileChannel fChan = raf.getChannel();) {
      final MappedByteBuffer mbBuf = getMappedByteBuffer(fChan, READ_ONLY);
      final ByteArrayBuilder bab = new ByteArrayBuilder();

      String s;
      while ((s = readLine(mbBuf, bab, charset)) != null) {
        sb.append(s);
        sb.append(LS);
      }
    } catch (final IOException e) {
      throw new RuntimeException("Cannot create File Channel.");
    }
    return sb.toString();
  }

  // *******************************
  // STANDARD IO READER OPERATIONS
  // *******************************
  /**
   * Opens a BufferedReader wrapped around a File. Equivalent to the call<br>
   * openBufferedReader(file, DEFAULT_BUFSIZE) Rethrows any IOException as a
   * RuntimeException. The returned object must be closed by the calling program.
   *
   * @param file the given file
   * @return BufferedReader object
   * @throws RuntimeException if File Not Found.
   */
  public static BufferedReader openBufferedReader(final File file) {
    return openBufferedReader(file, DEFAULT_BUFSIZE, Charset.defaultCharset());
  }

  /**
   * Opens a BufferedReader wrapped around a FileReader with a specified file
   * and buffer size. If bufSize is less than the default (8192) the default
   * will be used. The returned object must be closed by the calling program.
   *
   * @param file the given file
   * @param bufSize the given buffer size
   * @return a BufferedReader object
   * @throws RuntimeException if File Not Found.
   */
  public static BufferedReader openBufferedReader(final File file, final int bufSize) {
    return openBufferedReader(file, bufSize, Charset.defaultCharset());
  }

  /**
   * Opens a BufferedReader, which specifies a bufSize, wrapped around an
   * InputStreamReader, which specifies a Charset. The InputStreamReader wraps a
   * FileInputStream. If bufSize is less than the default (8192) the default
   * will be used. If the charset is null, Charset.defaultCharset() will be
   * used. The returned object must be closed by the calling program.
   *
   * @param file the given file
   * @param bufSize the given buffer size
   * @param charset the given Charset
   * @return a BufferedReader object
   * @throws RuntimeException if FileNotFoundException occurs.
   */
  public static BufferedReader openBufferedReader(final File file, final int bufSize,
      final Charset charset) {
    final int bufSz = (bufSize < DEFAULT_BUFSIZE) ? DEFAULT_BUFSIZE : bufSize;
    final Charset cSet = (charset == null) ? Charset.defaultCharset() : charset;
    BufferedReader in = null; // default bufsize is 8192.
    try {
      final FileInputStream fis = new FileInputStream(file);
      final InputStreamReader isr = new InputStreamReader(fis, cSet);
      in = new BufferedReader(isr, bufSz);
    } catch (final FileNotFoundException e) { // from FileInputStream
      // never opened, so don't close it.
      throw new RuntimeException(LS + "File Not Found: " + file.getPath() + LS + e);
    }
    return in;
  }

  /**
   * Configures a file for string writing. If a file by the same name exists, it will be
   * deleted. If fileName is not fully qualified, it will be relative to the root of this
   * package. The returned object must be closed by the calling program.
   * @param fileName the name of the file to configure
   * @return a PrintWriter
   */
  public static final PrintWriter openPrintWriter(final String fileName) {
    File file = null;
    PrintWriter pw = null;
    if ((fileName != null) && !fileName.isEmpty()) {
      file = new File(fileName);
      if (file.isFile()) {
        file.delete(); //remove old file if it exists
      } else {
        try {
          file.createNewFile();
        } catch (final Exception e) {
          throw new RuntimeException("Cannot create file: " + fileName + LS + e);
        }
      }
      final BufferedWriter bw;
      try {
        final FileOutputStream fos = new FileOutputStream(file, true);
        final OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.defaultCharset());
        bw = new BufferedWriter(osw, 8192);
      } catch (final IOException e) {
        // never opened, so don't close it.
        throw new RuntimeException("Could not create: " + file.getPath() + LS + e);
      }
      pw = new PrintWriter(bw);
    }
    return pw;
  }

  /**
   * Tests a Reader object if it is ready.
   *
   * @param in the given Reader
   * @return boolean true if ready.
   * @throws RuntimeException if IOException occurs.
   */
  public static boolean ready(final Reader in) {
    boolean out = false;
    try {
      out = in.ready();
    } catch (final IOException e) {
      throw new RuntimeException(LS + "Reader.ready() unsuccessful: " + LS + e);
    }
    return out;
  }

  /**
   * Skips bytes in the given Reader object.
   *
   * @param in the given Reader
   * @param skipLen in bytes.
   * @throws RuntimeException if IOException occurs.
   */
  public static void skip(final Reader in, final long skipLen) {
    try {
      in.skip(skipLen);
    } catch (final IOException e) {
      try {
        in.close();
      } catch (final IOException f) {
        throw new RuntimeException(LS + "Close Unsuccessful" + LS + f);
      }
      throw new RuntimeException(LS + "Reader.skip(len) unsuccessful: " + LS + e);
    }
  }

  /**
   * Reads characters from the given Reader into the given character array.
   *
   * @param in the given Reader
   * @param length number of characters to read
   * @param cArr Array must be equal to or larger than length
   * @return number of characters actually read from Reader
   * @throws RuntimeException if IOException occurs.
   */
  public static int readCharacters(final Reader in, final int length, final char[] cArr) {
    int readLen = 0;
    try {
      readLen = in.read(cArr, 0, length);
    } catch (final IOException e) {
      try {
        in.close();
      } catch (final IOException f) {
        throw new RuntimeException(LS + "Close Unsuccessful" + LS + f);
      }
      throw new RuntimeException(LS + "Reader.read(char[],0,len) unsuccessful: " + LS + e);
    }
    return readLen;
  }

  /**
   * Reads a file into a char array, then closes the file. Useful when special
   * handling of Line-Separation characters is required. Equivalent to: <br>
   * fileToCharArray(String, int, Charset.defaultCharset());
   *
   * @param fileName the given fileName
   * @return a char[]
   * @throws RuntimeException if IOException occurs.
   * @throws IllegalArgumentException if File size is greater than
   * Integer.MAX_VALUE.
   */
  public static char[] fileToCharArray(final String fileName) {
    return fileToCharArray(fileName, DEFAULT_BUFSIZE, Charset.defaultCharset());
  }

  /**
   * Reads a file into a char array, then closes the file. Useful when special
   * handling of Line-Separation characters is required.
   *
   * @param fileName the given fileName
   * @param bufSize if less than 8192 it defaults to 8192
   * @param charset The Charset to use when converting arrays of bytes from the
   * source to a Unicode String (UTF-16).
   * @return a char[]
   * @throws RuntimeException if IOException occurs.
   * @throws IllegalArgumentException if File size is greater than
   * Integer.MAX_VALUE.
   */
  public static char[] fileToCharArray(final String fileName, final int bufSize,
      final Charset charset) {
    final File file = getExistingFile(fileName);
    char[] cArr = null;
    final long fileLen = (long) (file.length() * 1.1); // 10% headroom
    if (fileLen > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(
          LS + "File Size is too large: " + fileLen + " >" + " Max: " + Integer.MAX_VALUE);
    }
    final int len = (int) fileLen;
    try (BufferedReader in = openBufferedReader(file, bufSize, charset)) {
      cArr = new char[len];
      in.read(cArr, 0, len);
    } catch (final IOException e) { // thrown by read()
      throw new RuntimeException(LS + "BufferedReader.read(char[],0,len) unsuccessful: " + LS + e);
    }
    return cArr;
  }

  /**
   * Reads a file into a String. Each line of the file is delimited by the
   * current operating systems's "line.separator" characters. Closes the file.
   * Equivalent to: <br>
   * fileToString(String, 8192, Charset.defaultCharset());
   *
   * @param fileName the given fileName
   * @return a String
   * @throws RuntimeException if IOException occurs.
   */
  public static String fileToString(final String fileName) {
    return fileToString(fileName, DEFAULT_BUFSIZE, Charset.defaultCharset());
  }

  /**
   * Reads a file into a String. Each line of the file is delimited by the
   * current operating systems's "line.separator" characters. Closes the file.
   *
   * @param fileName the given fileName
   * @param bufSize if less than 8192 it defaults to 8192
   * @param charset The Charset to use when converting arrays of bytes from the
   * source to a Unicode String (UTF-16).
   * @return String
   * @throws RuntimeException if IOException occurs.
   */
  public static String fileToString(final String fileName, final int bufSize,
      final Charset charset) {
    final StringBuilder sb = new StringBuilder();
    final File file = getExistingFile(fileName);
    try (BufferedReader in = openBufferedReader(file, bufSize, charset)) {
      String s;
      while ((s = in.readLine()) != null) {
        sb.append(s);
        sb.append(LS);
      }
    } catch (final IOException e) { // thrown by readLine()
      throw new RuntimeException(LS + "BufferedReader.readLine() unsuccessful: " + LS + e);
    }
    return sb.toString();
  }

  // STANDARD IO WRITE OPERATIONS
  /**
   * Opens a BufferedWriter wrapped around a FileWriter with a specified file
   * and buffer size. If bufSize is less than the default (8192) the default
   * will be used. The returned object must be closed by the calling program.
   *
   * @param file the given file
   * @param bufSize the given buffer size
   * @param append to existing file if true.
   * @return BufferedWriter object
   * @throws RuntimeException if IOException occurs.
   */
  public static BufferedWriter openBufferedWriter(final File file, final int bufSize,
      final boolean append) {
    return openBufferedWriter(file, bufSize, append, Charset.defaultCharset());
  }

  /**
   * Opens a BufferedWriter wrapped around a FileWriter with a specified file
   * and buffer size. If bufSize is less than the default (8192) the default
   * will be used. The returned object must be closed by the calling program.
   *
   * @param file the given file
   * @param bufSize if less than 8192 it defaults to 8192.
   * @param append to existing file if true.
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @return BufferedWriter object
   * @throws RuntimeException if IOException occurs.
   */
  public static BufferedWriter openBufferedWriter(final File file, final int bufSize,
      final boolean append, final Charset charset) {
    final int bufSz = (bufSize < DEFAULT_BUFSIZE) ? DEFAULT_BUFSIZE : bufSize;
    BufferedWriter out = null; // default bufsize is 8192.
    try {
      final FileOutputStream fos = new FileOutputStream(file, append);
      final OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
      out = new BufferedWriter(osw, bufSz);
    } catch (final IOException e) {
      // never opened, so don't close it.
      throw new RuntimeException(LS + "Could not create: " + file.getPath() + LS + e);
    }
    return out;
  }

  /**
   * Writes a String to a file using a BufferedWriter. Closes the file.
   *
   * @param text is the source String.
   * @param fileName the given fileName
   * @throws RuntimeException if IOException occurs or if fileName is null or
   * empty.
   */
  public static void stringToFile(final String text, final String fileName) {
    stringToFile(text, fileName, DEFAULT_BUFSIZE, Charset.defaultCharset());
  }

  /**
   * Writes a String to a file using a BufferedWriter. Closes the file.
   *
   * @param text is the source String.
   * @param fileName the given fileName
   * @param bufSize if less than 8192 it defaults to 8192.
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @throws RuntimeException if IOException occurs or if fileName is null or
   * empty.
   */
  public static void stringToFile(final String text, final String fileName, final int bufSize,
      final Charset charset) {
    checkFileName(fileName);
    final File file = new File(fileName);
    try (BufferedWriter bw = openBufferedWriter(file, bufSize, false, charset);
        PrintWriter out = new PrintWriter(bw);) {
      out.print(text);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Appends a String to a file using a BufferedWriter. If fileName does not
   * exist, this creates a new empty file of that name. This closes the file
   * after appending.
   *
   * @param text is the source String.
   * @param fileName the given fileName
   * @throws RuntimeException if IOException or SecurityException occurs, or if
   * fileName is null or empty.
   */
  public static void appendStringToFile(final String text, final String fileName) {
    appendStringToFile(text, fileName, DEFAULT_BUFSIZE, Charset.defaultCharset());
  }

  /**
   * Appends a String to a file using a BufferedWriter, bufSize and Charset. If
   * fileName does not exist, this creates a new empty file of that name. This
   * closes the file after appending.
   *
   * @param text is the source String.
   * @param fileName the given fileName
   * @param bufSize if less than 8192 it defaults to 8192.
   * @param charset The Charset to use when converting the source string
   * (UTF-16) to a sequence of encoded bytes of the Charset.
   * @throws RuntimeException if IOException or SecurityException occurs, or if
   * fileName is null or empty.
   */
  public static void appendStringToFile(final String text, final String fileName,
      final int bufSize, final Charset charset) {
    checkFileName(fileName);
    final File file = new File(fileName);
    if (!file.isFile()) { // does not exist
      try {
        file.createNewFile();
      } catch (final Exception e) {
        throw new RuntimeException("Cannot create file: " + fileName + LS + e);
      }
    }
    try (BufferedWriter bw = openBufferedWriter(file, bufSize, true, charset);
        PrintWriter out = new PrintWriter(bw);) {
      out.print(text);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
