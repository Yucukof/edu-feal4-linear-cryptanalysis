package org.dcu.student.sem1.ca642.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.dcu.student.sem1.ca642.utils.ConversionUtils;

import static org.dcu.student.sem1.ca642.utils.ConversionUtils.*;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Hexadecimal implements Comparable<Hexadecimal> {

    /**
     * The REGEX pattern for hexadecimal String.
     */
    private static final String HEXADECIMAL_PATTERN = "(\\s?[0-9A-F]{2}\\s?)+";

    /**
     * The hexadecimal value stored as string.
     */
    byte[] bytes;

    /**
     * Creates a hexadecimal value from a string value.
     *
     * @param value the string to convert to hexadecimal.
     * @return a hexadecimal value.
     * @throws IllegalArgumentException if the string value is not a valid hex.
     * @see #isHex(String)
     */
    public static Hexadecimal from(final String value) {
        final String upperValue = value.toUpperCase().replace(" ", "");
        if (isHex(upperValue)) {
            final byte[] bytes = hex2bytes(value);
            return Hexadecimal.from(bytes);
        }
        throw new IllegalArgumentException(String.format("Invalid hexadecimal value: [%s]", value));
    }

    /**
     * Check if the corresponding string is a valid hex string.
     *
     * @param str the string value to check.
     * @return true if str contains only hex characters, false otherwise.
     */
    public static boolean isHex(final String str) {
        return str.toUpperCase().matches(HEXADECIMAL_PATTERN);
    }

    /**
     * Creates a hexadecimal value from a byte array.
     *
     * @param bytes the bytes to convert to Hexadecimal.
     * @return a hexadecimal value.
     */
    public static Hexadecimal from(final byte[] bytes) {
        return new Hexadecimal(bytes);
    }

    /**
     * Creates a hexadecimal value from an integer value.
     *
     * @param value the integer to convert to hexadecimal.
     * @return a hexadecimal value.
     */
    public static Hexadecimal from(final int value) {
        final byte[] hex = int2bytes(value);
        return new Hexadecimal(hex);
    }

    /**
     * Creates a hexadecimal value from a long value.
     *
     * @param integer the integer to convert to Hexadecimal.
     * @return a hexadecimal value.
     */
    public static Hexadecimal from(final long integer) {
        final byte[] bytes = long2bytes(integer);
        return new Hexadecimal(bytes);
    }

    /**
     * Return the length of the underlying array of bytes.
     *
     * @return a byte length.
     */
    public int size() {
        return toBytes().length;
    }

    /**
     * Convert the current hexadecimal value to its corresponding byte array.
     *
     * @return a byte array.
     */
    public byte[] toBytes() {
        return bytes;
    }

    /**
     * Converts the current hexadecimal values into its corresponding binary value.
     *
     * @return a binary value.
     */
    public Binary toBin() {
        return Binary.from(bytes);
    }

    /**
     * Convert current hexadecimal value to its corresponding integer value.
     * <br> CAUTION: if the Hexadecimal value is longer than 4-byte long, the excess right bytes will be dropped.
     *
     * @return an 4-bytes int.
     * @see ConversionUtils#hex2int(String)
     */
    public int toInt() {
        return bytes2int(bytes);
    }

    /**
     * Converts the current hexadecimal values into its corresponding long value.
     * <br> CAUTION: if the Hexadecimal value is longer than 8-byte long, the excess right bytes will be dropped.
     *
     * @return an 8-bytes long value.
     */
    public long toLong() {
        return bytes2long(bytes);
    }

    @Override
    public int compareTo(final Hexadecimal that) {
        return Long.compare(this.toLong(), that.toLong());
    }

    @Override
    public String toString() {
        final int length = bytes.length;

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", bytes[i]));
            if ((i + 1) % 4 == 0 && i != 1 && (i + 1) != length) {
                sb.append(" ");
            }
        }
        return sb.toString().toUpperCase();
    }
}
