package org.dcu.student.sem1.ca642.utils;

import org.dcu.student.sem1.ca642.model.Binary;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.extract;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.parityOf;

public class ByteUtilsTest {

    @Test
    public void given_byte_0x1_when_parity_of_then_expect_1() {
        final byte b = (byte) 0x1;
        final int parity = BytesUtils.parityOf(b);

        assertThat(parity).isEqualTo(1);
    }

    @Test
    public void given_byte_0x2_when_parity_of_then_expect_0() {
        final byte b = (byte) 0x2;
        final int parity = BytesUtils.parityOf(b);

        assertThat(parity).isEqualTo(1);
    }

    @Test
    public void given_byte_210_when_parity_of_then_expect_0() {
        final int b = Binary.from("1101 0010").toInt();
        final int parity = parityOf(b);

        assertThat(parity).isEqualTo(0);
    }

    @Test
    public void given_byte_211_when_parity_of_then_expect_0() {
        final int b = Binary.from("1101 0011").toInt();
        final int parity = parityOf(b);

        assertThat(parity).isEqualTo(1);
    }

    @Test
    public void given_bytes_and_position_when_extract_list_then_expect_correct_value() {
        final int binary = Binary.from("00000000 00000000 00000000 00010010").toInt();
        final int position1 = 27;
        final int position2 = 30;
        final int extracted_27 = extract(binary, position1);
        final int extracted_30 = extract(binary, position2);
        final int extracted = extract(binary, position1, position2);

        assertThat(extracted_27).isEqualTo(Binary.from("00000000 00000000 00000000 00010000").toInt());
        assertThat(extracted_30).isEqualTo(Binary.from("00000000 00000000 00000000 00000010").toInt());
        assertThat(extracted).isEqualTo(binary);
    }

    @Test
    public void given_int_210_when_parity_of_then_expect_0() {
        final int i = Binary.from("0000 0000 0000 0011 0110 0111 0100 1101").toInt();
        final int parity = parityOf(i);

        assertThat(parity).isEqualTo(1);
    }

    @Test
    public void given_int_211_when_parity_of_then_expect_0() {
        final int i = Binary.from("1111111100000000").toInt();
        final int right = BytesUtils.right(i);

        assertThat(right).isEqualTo(Binary.from("00000000").toInt());
    }

    @Test
    public void given_int_when_left_then_expect_1111_1111_0000_0000() {
        final int i = Binary.from("1111111100000000").toInt();
        final int left = BytesUtils.left(i);

        assertThat(left).isEqualTo(Binary.from("11111111").toInt());
    }
}