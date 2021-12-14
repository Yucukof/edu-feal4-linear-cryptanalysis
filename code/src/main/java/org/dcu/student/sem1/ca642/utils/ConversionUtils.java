package org.dcu.student.sem1.ca642.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversionUtils {

    /**
     * Converts the given array of bytes to its corresponding integer value.
     * <br>CAUTION: an array longer than 4 bytes will see the excess right bytes be dropped.
     *
     * @param bytes the array of bytes to convert.
     * @return a 4-byte integer value.
     */
    public static int bytes2int(final byte[] bytes) {
        final int length = bytes.length;
        final int offset = length - 4;
        final int effectiveOffset = Math.max(offset, 0);
        final int effectiveLength = length - effectiveOffset;

        final ByteBuffer buffer = ByteBuffer.wrap(bytes, effectiveOffset, effectiveLength);
        return buffer.getInt();
    }

    /**
     * Convert a hexadecimal value back to its integer value.
     * <br>CAUTION: a hexadecimal value exceeding 4 bytes will see the excess right bytes be dropped.
     *
     * @param hexadecimal the hexadecimal value to convert.
     * @return an 4-byte integer value.
     * @throws NumberFormatException if the hexadecimal string is not valid.
     * @see #int2hex(int)
     */
    public static int hex2int(final String hexadecimal) {
        return Integer.parseUnsignedInt(hexadecimal, 16);
    }


    /**
     * Convert a hexadecimal value back to its long integer value.
     * <br>CAUTION: a hexadecimal value exceeding 8 bytes will see the excess right bytes be dropped.
     *
     * @param hexadecimal the hexadecimal value to convert.
     * @return a 8-byte integer value.
     * @throws NumberFormatException if the hexadecimal string is not valid.
     * @see #int2hex(int)
     */
    public static long hex2long(final String hexadecimal) {
        final byte[] bytes = hex2bytes(hexadecimal);
        return bytes2long(bytes);
    }

    /**
     * Convert a string containing a hexadecimal value to its corresponding byte array.
     *
     * @param hex the string to convert.
     * @return an array of bytes.
     */
    public static byte[] hex2bytes(final String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }

    /**
     * Converts a byte array to its corresponding long integer value.
     * <br>CAUTION: an array longer than 8 bytes will see the excess right bytes be dropped.
     *
     * @param bytes the byte array to convert.
     * @return an 8-byte long integer.
     */
    public static long bytes2long(final byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();
    }

    /**
     * Convert a given integer to its corresponding array of bytes.
     *
     * @param integer the integer to convert.
     * @return a 4-bytes array.
     */
    public static byte[] int2bytes(final int integer) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(integer);
        return buffer.array();
    }

    /**
     * Convert an integer value to its hexadecimal expression.
     *
     * @param integer the integer value to convert.
     * @return a string holding the binary valye of the integer.
     * @see ConversionUtils#hex2int(String)
     */
    public static String int2hex(final int integer) {
        final String rawString = Integer.toHexString(integer);
        return rawString.substring((Integer.SIZE - Byte.SIZE) / 4).toUpperCase();
    }

    /**
     * Converts a long integer into its corresponding byte array.
     *
     * @param integer the 8-byte long integer.
     * @return an 8-byte array.
     * @see #bytes2long(byte[])
     */
    public static byte[] long2bytes(final long integer) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(integer);
        return buffer.array();
    }

    /**
     * Converts a list of boxed integers into an array of primitive ints.
     *
     * @param list the list to convert.
     * @return an array of ints.
     */
    public static int[] toIntArray(final List<Integer> list) {
        final Integer[] array = list.toArray(new Integer[0]);
        return ArrayUtils.toPrimitive(array);
    }
}
