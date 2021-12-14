package org.dcu.student.sem1.ca642.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HexadecimalTest {

    @Test
    public void given_valid_int_when_toHex_then_expect_object(){
        final int value = 917508765;
        final Hexadecimal hexadecimal = Hexadecimal.from(value);
        assertThat(hexadecimal).isNotNull();
    }

}