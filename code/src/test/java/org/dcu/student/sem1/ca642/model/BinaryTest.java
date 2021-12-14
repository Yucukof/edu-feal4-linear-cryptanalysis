package org.dcu.student.sem1.ca642.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BinaryTest {

    @Test
    public void given_bits_8_when_binary_from_then_expect_binary_1() {
        final String bits = "0000 0001";
        final Binary binary = Binary.from(bits);

        final byte[] bytes = binary.getBytes();
        assertThat(bytes)
              .isNotNull()
              .isNotEmpty()
              .hasSize(8);
        assertThat(bytes[bytes.length-1]).isEqualByComparingTo((byte) 0x1);
    }

    @Test
    public void given_bits_8_when_binary_from_then_expect_binary_2() {
        final String bits = "0000 0010";
        final Binary binary = Binary.from(bits);

        final byte[] bytes = binary.getBytes();
        assertThat(bytes)
              .isNotNull()
              .isNotEmpty()
              .hasSize(8);
        assertThat(bytes[bytes.length-1]).isEqualByComparingTo((byte) 0x2);
    }

    @Test
    public void given_valid_binary_1_when_toInt_then_expect_correct_value() {
        final String bits = "0000 0001";
        final Binary binary = Binary.from(bits);

        assertThat(binary.toInt()).isEqualTo(1);
    }

    @Test
    public void given_valid_binary_2_when_toInt_then_expect_correct_value() {
        final String bits = "0000 0010";
        final Binary binary = Binary.from(bits);

        assertThat(binary.toInt()).isEqualTo(2);
    }

    @Test
    public void given_valid_int_binary_when_toBytesString_then_expect_correct_value(){
        final int bits = -1903814973;
        final Binary binary = Binary.from(bits);

        final String string = binary.toString();
        assertThat(string)
              .isNotNull()
              .isNotEmpty()
              .isEqualTo("10001110 10000110 00010110 11000011");
    }

    @Test
    public void given_valid_int2_binary_when_toBytesString_then_expect_correct_value(){
        final int bits = -2147483648;
        final Binary binary = Binary.from(bits);

        final String string = binary.toString();
        assertThat(string)
              .isNotNull()
              .isNotEmpty()
              .isEqualTo("10000000 00000000 00000000 00000000");
    }

}