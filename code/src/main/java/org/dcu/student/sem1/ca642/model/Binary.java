package org.dcu.student.sem1.ca642.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import static org.dcu.student.sem1.ca642.utils.ConversionUtils.*;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Binary implements Comparable<Binary> {

    /**
     * Binary value of 0.
     */
    public static final Binary ZERO = Binary.from(0);
    /**
     * Binary value of 1.
     */
    public static final Binary ONE = Binary.from(1);
    /**
     * Binary value with all bits set to 0.
     */
    public static final Binary MIN = ZERO;
    /**
     * Binary value with all bits set to 1.
     */
    public static final Binary MAX = Binary.from(-1);

    /**
     * The REGEX pattern for binary String.
     */
    private static final String BINARY_PATTERN = "(\\s?[0-1]\\s?)+";

    /**
     * The underlying byte array representation.
     */
    byte[] bytes;

    /**
     * Creates a binary value from a single byte.
     *
     * @param b the byte to convert.
     * @return a binary value.
     */
    public static Binary from(final byte b) {
        return new Binary(new byte[]{b});
    }

    /**
     * Creates a binary value from a binary string.
     *
     * @param s the string to convert.
     * @return a binary value.
     * @throws IllegalArgumentException if the string is not a valid binary string.
     * @see #isBin(String)
     */
    public static Binary from(final String s) {
        final String value = s.replace(" ", "");
        if (isBin(value)) {
            final long integer = Long.parseUnsignedLong(value, 2);
            return Binary.from(integer);
        }
        throw new IllegalArgumentException("[" + s + "] is not a valid binary string (must contain only 0, 1 and space characters).");
    }

    /**
     * Check if a given string is a valid binary string.
     *
     * @param s the string to validate.
     * @return true if the string contains only 0, 1 or space characters, false otherwise.
     */
    public static boolean isBin(final String s) {
        return s.matches(BINARY_PATTERN);
    }

    /**
     * Creates a binary value from an integer.
     *
     * @param integer the 4-byte integer to convert.
     * @return a binary value.
     */
    public static Binary from(final int integer) {
        final byte[] bytes = int2bytes(integer);
        return from(bytes);
    }

    /**
     * Creates a binary value from a long integer.
     *
     * @param integer the 8-byte integer to convert.
     * @return a binary value.
     */
    public static Binary from(final long integer) {
        final byte[] bytes = long2bytes(integer);
        return from(bytes);
    }

    /**
     * Creates a binary value from a byte array.
     *
     * @param bytes the byte array to convert.
     * @return a binary value.
     */
    public static Binary from(final byte[] bytes) {
        return new Binary(bytes);
    }

    /**
     * Convert the current binary into its corresponding long integer value.
     * <br>CAUTION: if the binary is longer than 4-byte long, the excess right bytes will be dropped.
     *
     * @return a 8-byte long integer value.
     */
    public int toInt() {
        return bytes2int(bytes);
    }

    /**
     * Convert the current binary into its corresponding long integer value.
     * <br>CAUTION: if the binary is longer than 8-byte long, the excess right bytes will be dropped.
     *
     * @return a 8-byte long integer value.
     */
    public long toLong() {
        return bytes2long(bytes);
    }

    @Override
    public int compareTo(final Binary that) {
        return Long.compare(this.toLong(), that.toLong());
    }

    @Override
    public String toString() {
        final String paddedStr;
        if (bytes.length == 8) {
            final long value = toLong();
            final String str = Long.toBinaryString(value);
            paddedStr = StringUtils.leftPad(str, 64, '0');
        } else {
            int value = toInt();
            final String str = Integer.toBinaryString(value);
            paddedStr = StringUtils.leftPad(str, 32, '0');
        }
        return paddedStr.replaceAll("([01]{8})", "$0 ").trim();
    }
}
