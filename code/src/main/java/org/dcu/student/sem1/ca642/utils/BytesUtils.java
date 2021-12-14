package org.dcu.student.sem1.ca642.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.dcu.student.sem1.ca642.utils.ConversionUtils.bytes2int;
import static org.dcu.student.sem1.ca642.utils.ConversionUtils.int2bytes;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BytesUtils {

    /**
     * XOR three arrays of bytes, bytes per bytes.
     * <br> CAUTION: behaviour is only defined when the bytes arrays are of same length.
     *
     * @return a byte array of same size as the left byte array, filled with xor results.
     * @see #xor(byte[], byte[])
     */
    public static byte[] xor(final byte[] b0, final byte[] b1, final byte[] b2) {
        final byte[] temp = xor(b0, b1);
        return xor(temp, b2);
    }

    /**
     * XOR two arrays of bytes, bytes per bytes.
     * <br> CAUTION: behaviour is only defined when the two bytes arrays are of same length.
     *
     * @param left  the left array of bytes.
     * @param right the right arrays of bytes.
     * @return a byte array of same size as the left byte array, filled with xor results.
     * @see #xor(byte[], byte[], byte[])
     */
    public static byte[] xor(final byte[] left, final byte[] right) {

        final int size = left.length;
        final byte[] xor = new byte[size];

        for (int i = 0; i < size; i++) {
            final byte bl = left[i];
            final byte br = right[i];
            xor[i] = xor(bl, br);
        }
        return xor;
    }

    /**
     * Bitwise XOR comparison of the two bytes
     *
     * @return a byte containing XORed results.
     */
    public static byte xor(final byte b0, final byte b1) {
        return (byte) (b0 ^ b1);
    }

    /**
     * XOR three bytes.
     *
     * @return a single xored byte.
     */
    public static byte xor(final byte b0, final byte b1, final byte b2) {
        return (byte) (b0 ^ b1 ^ b2);
    }

    /**
     * Bitwise XOR comparison of an array of 4-byte integers.
     *
     * @return a 4-byte integer.
     */
    public static int xor(final int... integers) {
        int result = 0;
        for (int i : integers) {
            result ^= i;
        }
        return result;
    }

    /**
     * Bitwise AND comparison of three bytes arrays.
     * <br> CAUTION: behaviour is undefined if arrays are not of same length.
     *
     * @return a byte array of the length of the first byte array.
     * @see #and(byte[], byte[])
     */
    public static byte[] and(final byte[] b0, final byte[] b1, final byte[] b2) {
        final byte[] b0_and_b1 = and(b0, b1);
        return and(b0_and_b1, b2);
    }

    /**
     * Bitwise AND comparison of two bytes arrays.
     * <br> CAUTION: behaviour is undefined if arrays are not of same length.
     *
     * @return a byte array of length of the first array.
     */
    public static byte[] and(final byte[] b0, final byte[] b1) {
        final int length = b0.length;
        final byte[] and = new byte[length];
        for (int i = 0; i < length; i++) {
            and[i] = (byte) (b0[i] & b1[i]);
        }
        return and;
    }


    /**
     * Bitwise AND comparison of two bytes.
     *
     * @return a single byte.
     */
    public static byte and(final byte b0, final byte b1) {
        return (byte) (b0 & b1);
    }

    /**
     * Simple function that extract the left half of a bytes array.
     *
     * @param b the byte array to truncate.
     * @return a b.length/2-bytes array.
     * @see #right(byte[])
     */
    public static byte[] left(final byte[] b) {
        final int half = b.length / 2;
        final byte[] left = new byte[half];
        System.arraycopy(b, 0, left, 0, half);
        return left;
    }

    /**
     * Simple method that extracts the first two bytes of an integer,
     * <br> i.e. takes its left half.
     *
     * @param i the integ array to truncate.
     * @return a 4-byte integer.
     * @see #right(int)
     */
    public static int left(final int i) {
        return i >>> 8;
    }

    /**
     * Simple method that extracts the second two left bytes of a byte array,
     * <br> i.e. take the right half of a 4-bytes array.
     *
     * @param b the byte array to truncate.
     * @return a 2-byte array.
     * @see #left(byte[])
     */
    public static byte[] right(final byte[] b) {
        final int half = b.length / 2;
        final byte[] right = new byte[half];
        System.arraycopy(b, half, right, 0, half);
        return right;
    }


    /**
     * Simple method that extracts the last two bytes of an integer,
     * <br> i.e. takes its right half.
     *
     * @param i the integer to truncate.
     * @return a 4-byte integer.
     * @see #left(int)
     * @see #right(long)
     */
    public static int right(final int i) {
        return i & 255;
    }

    /**
     * Concatenate two 4-bytes arrays into one 8-bytes array.
     *
     * @param left  the bytes to put on the left side.
     * @param right the bytes to put on the right side.
     * @return an 8-bytes array.
     */
    public static byte[] concatenate(final byte[] left, final byte[] right) {
        final byte[] c = new byte[8];
        System.arraycopy(left, 0, c, 0, left.length);
        System.arraycopy(right, 0, c, left.length, right.length);
        return c;
    }

    /**
     * Compute the parity of a given byte array.
     *
     * @param bytes the byte arrays to process.
     * @return a 1-bit parity number.
     * @see #parityOf(int)
     * @see #parityOf(byte)
     */
    public static int parityOf(final byte[] bytes) {
        int parity = 0;
        for (byte b : bytes) {
            parity = parity ^ parityOf(b);
        }
        return parity;
    }

    /**
     * Returns the parity of a given byte.
     *
     * @param b the byte for which compute the parity
     * @return a 1-bit parity number (0 = even - 1 = odd)
     * @see #parityOf(int)
     * @see #parityOf(byte[])
     */
    public static int parityOf(final byte b) {
        int num = b;
        num ^= (num >>> 4);
        num ^= (num >>> 2);
        num ^= (num >>> 1);
        return (byte) num & 0x1;
    }

    /**
     * Applies a mask onto an array of bytes.
     *
     * @param input the input to mask.
     * @param mask  the array of bytes to use as mask. Must be of same length as the input.
     * @return an array of masked bytes.
     * @see #apply(int, int)
     * @see #apply(byte, byte)
     */
    public static byte[] apply(final byte[] input, final byte[] mask) {
        final int length = input.length;
        final byte[] masked = new byte[length];
        for (int i = 0; i < length; i++) {
            masked[i] = apply(input[i], mask[i]);
        }
        return masked;
    }

    /**
     * Applies the given mask onto the given input to extract some bits.
     *
     * @param input the input to mask.
     * @param mask  the mask to use.
     * @return a masked byte.
     * @see #apply(int, int)
     * @see #apply(byte[], byte[])
     */
    public static byte apply(final byte input, final byte mask) {
        return (byte) (input & mask);
    }

    /**
     * Applies the given mask onto the given input to extract some bits.
     *
     * @param input the input to mask.
     * @param mask  the mask to use.
     * @return a masked int.
     * @see #apply(byte, byte)
     * @see #apply(byte[], byte[])
     */
    public static int apply(final int input, final int mask) {
        return (input & mask);
    }

    /**
     * Extract the bits located at the given positions and XOR them into a single bit output.
     * <br> CAUTION: behaviour not defined for position outside 4-bytes space.
     *
     * @param bytes     a 4-bytes value wrapped in signed integer.
     * @param positions the list of bits to extract.
     * @return a 1-bit value wrapped in a 4-bytes integer.
     */
    public static int extract(final int bytes, final int... positions) {
        int parity = 0;
        for (int position : positions) {
            final int bit = extract(bytes, position);
            parity ^= bit;
        }
        return parity;
    }

    /**
     * Extract the bit at a given position in the given 4-bytes.
     *
     * @param bytes    a 4-bytes value wrapped in signed integer.
     * @param position the position of the bit to extract.
     * @return a single bit wrapped in a 4-bytes integer.
     */
    public static int extract(final int bytes, final int position) {
        final int bit = 1 << (31 - position);
        return bytes & bit;
    }

    /**
     * Returns the parity of a given 4-bytes integer.
     * <br>
     * We use the shifting to "transfer" the parity from one half to another:
     * <br> - first, from the left 4-bits to the right 4-bits.
     * <br> - Next, from the left 2-bits to the right 2-bits from the right half.
     * <br> - Finally, from the second-to-last bit to the last one.
     * <br> This means that the parity is now located on the very last bit. So, at last, we only have to AND the last
     * bit with the 0000 0001 mask to see if the byte is odd or even.
     * <p>
     * Inspired from <a href="https://www.freecodecamp.org/news/algorithmic-problem-solving-efficiently-computing-the-parity-of-a-stream-of-numbers-cd652af14643"/>Algorithmic
     * problem solving: How to efficiently compute the parity of a stream of numbers</a>
     *
     * @param b the integer value for which compute the parity
     * @return a 1-bit parity number (0 = even - 1 = odd)
     * @see #parityOf(byte)
     * @see #parityOf(byte[])
     */
    public static int parityOf(final int b) {
        int num = b;
        num ^= (num >>> 16);
        num ^= (num >>> 8);
        num ^= (num >>> 4);
        num ^= (num >>> 2);
        num ^= (num >>> 1);
        return num & 0x1;
    }


    /**
     * Simple method that extracts the first two bytes of a long integer,
     * <br> i.e. takes its left half.
     *
     * @param i the long integer to truncate.
     * @return an 8-byte long integer.
     * @see #right(long)
     * @see #left(int)
     */
    public static int left(final long i) {
        return (int) (i >>> 32);
    }

    /**
     * Simple method that extracts the last two bytes of a long integer,
     * <br> i.e. takes its right half.
     *
     * @param i the long integer to truncate.
     * @return an 8-byte long integer.
     * @see #right(int)
     * @see #left(long)
     */
    public static int right(final long i) {
        return (int) i;
    }

    /**
     * Takes a 4-byte word, and
     * <br> - xor bytes 1 & 2 into byte 2
     * <br> - xor bytes 3 & 4 into byte 3
     *
     * @param word the word to squeeze into 16 bits.
     * @return a 4-bytes (32-bit) word.
     */
    public static int squeeze(final int word) {
        final byte[] bytes = int2bytes(word);
        final byte[] m = {0, xor(bytes[0], bytes[1]), xor(bytes[2], bytes[3]), 0};
        return bytes2int(m);
    }

    /**
     * Concatenate 4 bytes into a integer.
     *
     * @param b0 first byte
     * @param b1 second byte
     * @param b2 third byte
     * @param b3 fourth byte
     * @return an integer value.
     */
    public static byte[] concatenate(final byte b0, final byte b1, final byte b2, final byte b3) {
        return new byte[]{b0, b1, b2, b3};
    }

    /**
     * Concatenate two (4-bytes) integers into a (8-bytes) long.
     *
     * @param left  the 4-bytes to insert on the left.
     * @param right the 4-bytes to insert on the right.
     * @return a long value.
     */
    public static long concatenate(final int left, final int right) {
        final Long leftL = ((long) left) << 16;
        final Long rightL = (long) right;
        return leftL + rightL;
    }
}
