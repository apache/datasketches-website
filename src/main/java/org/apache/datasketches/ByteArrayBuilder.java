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

import java.util.Arrays;

/**
 * A mutable sequence of bytes.
 *
 * <p>Implements a modifiable byte array. At any point in time it contains some
 * particular sequence of bytes but the length and content of the sequence can
 * be changed through certain method calls.
 *
 * @author Lee Rhodes
 */
@SuppressWarnings("javadoc")
public class ByteArrayBuilder {
  private byte[] arr_;
  private int count_ = 0;
  private int capacity_;

  public ByteArrayBuilder() {
    this(1024);
  }

  /**
   * Constructs an empty ByteArrayBuilder with an initial capacity specified by
   * the <code>capacity</code> argument.
   *
   * @param capacity the initial capacity.
   * @throws NegativeArraySizeException if the <code>capacity</code> argument is
   * less than <code>0</code>.
   */
  public ByteArrayBuilder(final int capacity) {
    arr_ = new byte[capacity];
  }

  /**
   * Returns the current capacity. The capacity is the amount of storage
   * available for newly inserted bytes, beyond which an allocation will occur.
   *
   * @return the current capacity
   */
  public int capacity() {
    return capacity_;
  }

  /**
   * Returns the length (byte count).
   *
   * @return the length of the sequence of bytes currently represented by this
   * object
   */
  public int length() {
    return count_;
  }

  /**
   * Ensures that the capacity is at least equal to the specified minimum. If
   * the current capacity is less than the argument, then a new internal array
   * is allocated with greater capacity. The new capacity is the larger of:
   * <ul>
   * <li>The <code>minimumCapacity</code> argument.
   * <li>Twice the old capacity, plus <code>2</code>.
   * </ul>
   * If the <code>minimumCapacity</code> argument is nonpositive, this method
   * takes no action and simply returns.
   *
   * @param minimumCapacity the minimum desired capacity.
   */
  public void ensureCapacity(final int minimumCapacity) {
    if (minimumCapacity > arr_.length) {
      expandCapacity(minimumCapacity);
    }
  }

  /**
   * This implements the expansion semantics of ensureCapacity with no size
   * check or synchronization.
   */
  void expandCapacity(final int minimumCapacity) {
    int newCapacity = (arr_.length + 1) * 2;
    if (newCapacity < 0) {
      newCapacity = Integer.MAX_VALUE;
    } else if (minimumCapacity > newCapacity) {
      newCapacity = minimumCapacity;
    }
    arr_ = Arrays.copyOf(arr_, newCapacity);
  }

  /**
   * Ensures that the space remaining (capacity - count) is at least as large as
   * the given space.
   *
   * @param space the given space in bytes
   */
  public void ensureSpace(final int space) {
    final int newCount = count_ + space;
    if (newCount > arr_.length) {
      expandCapacity(newCount);
    }
  }

  /**
   * Appends the given byte to the end of the current byte sequence.
   *
   * @param b byte
   * @return this ByteArrayBuilder
   */
  public ByteArrayBuilder append(final byte b) {
    ensureSpace(1);
    arr_[count_++] = b;
    return this;
  }

  /**
   * Appends the given byte array to the end of the current byte sequence.
   *
   * @param bArr byte array
   * @return this ByteArrayBuilder
   */
  public ByteArrayBuilder append(final byte[] bArr) {
    final int len = bArr.length;
    ensureSpace(len);
    System.arraycopy(bArr, 0, arr_, count_, len);
    count_ += len;
    return this;
  }

  /**
   * Sets the length of the byte sequence. The sequence is changed to a new byte
   * sequence whose length is specified by the argument. For every nonnegative
   * index <i>k</i> less than <code>newLength</code>, the byte at index <i>k</i>
   * in the new byte sequence is the same as the byte at index <i>k</i> in the
   * old sequence if <i>k</i> is less than the length of the old byte sequence;
   * otherwise, it is the null byte <code>0</code>.
   *
   * <p>In other words, if the <code>newLength</code> argument is less than the
   * current length, the length is changed to the specified length.
   *
   * <p>If the <code>newLength</code> argument is greater than or equal to the
   * current length, sufficient null byte (<code>0</code>) are appended so that
   * length becomes the <code>newLength</code> argument.
   *
   * <p>The <code>newLength</code> argument must be greater than or equal to
   * <code>0</code>.
   *
   * @param newLength the new length
   * @throws IndexOutOfBoundsException if the <code>newLength</code> argument is
   * negative.
   */
  public void setLength(final int newLength) {
    if (newLength < 0) {
      throw new ArrayIndexOutOfBoundsException(newLength);
    }
    if (newLength > arr_.length) {
      expandCapacity(newLength);
    }
    if (count_ < newLength) {
      for (; count_ < newLength; count_++) {
        arr_[count_] = 0;
      }
    } else {
      count_ = newLength;
    }
  }

  /**
   * Returns a byte array representing the data in this sequence. A new
   * <code>byte[]</code> object is allocated and initialized to contain the byte
   * sequence currently represented by this object. This <code>byte[]</code> is
   * then returned. Subsequent changes to this sequence do not affect the
   * contents of the <code>byte[]</code>.
   *
   * @return a byte array representation of this byte sequence.
   */
  public byte[] toByteArray() {
    return Arrays.copyOf(arr_, count_);
  }

  /**
   * Attempts to reduce storage used for the byte sequence. If the buffer is
   * larger than necessary to hold its current sequence of bytes, then it may be
   * resized to become more space efficient. Calling this method may, but is not
   * required to, affect the value returned by a subsequent call to the
   * {@link #capacity()} method.
   */
  public void trimToSize() {
    if (count_ < arr_.length) {
      arr_ = Arrays.copyOf(arr_, count_);
    }
  }
}
