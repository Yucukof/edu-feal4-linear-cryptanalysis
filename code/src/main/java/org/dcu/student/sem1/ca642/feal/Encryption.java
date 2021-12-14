package org.dcu.student.sem1.ca642.feal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.dcu.student.sem1.ca642.FEAL.f;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.*;

/**
 * The modified FEAL-4 encryption algorithm.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Encryption {

    public static long encrypt(final long plaintext, final Keychain keys) {
        final int l4 = l4(plaintext, keys);
        final int r4 = r4(plaintext, keys);
        return concatenate(l4, r4);
    }

    public static int l4(final long plaintext, final Keychain keys) {
        final int y3 = y3(plaintext, keys);
        final int x2 = x2(plaintext, keys);
        final int k4 = keys.getK4();
        return xor(x2, y3, k4);
    }

    public static int x0(final long plaintext) {
        final int l0 = left(plaintext);
        final int r0 = right(plaintext);
        return xor(l0, r0);
    }

    public static int x1(final long plaintext, final Keychain keys) {
        final int l0 = left(plaintext);
        final int y0 = y0(plaintext, keys);

        return xor(l0, y0);
    }

    public static int x2(final long plaintext, final Keychain keys) {
        final int x0 = x0(plaintext);
        final int y1 = y1(plaintext, keys);
        return xor(x0, y1);
    }

    public static int x3(final long plaintext, final Keychain keys) {
        final int x1 = x1(plaintext, keys);
        final int y2 = y2(plaintext, keys);
        return xor(x1, y2);
    }

    public static int y0(final long plaintext, final Keychain keys) {
        final int x0 = x0(plaintext);
        final int k0 = keys.getK0();

        final int x0_xor_k0 = xor(x0, k0);
        return f(x0_xor_k0);
    }

    public static int y1(final long plaintext, final Keychain keys) {
        final int x1 = x1(plaintext, keys);
        final int k1 = keys.getK1();
        final int x1_xor_k1 = xor(x1, k1);

        return f(x1_xor_k1);
    }

    public static int y2(final long plaintext, final Keychain keys) {
        final int x2 = x2(plaintext, keys);
        final int k2 = keys.getK2();
        final int x2_xor_k2 = xor(x2, k2);
        return f(x2_xor_k2);
    }

    public static int y3(final long plaintext, final Keychain keys) {
        final int x3 = x3(plaintext, keys);
        final int k3 = keys.getK3();
        final int x3_xor_k3 = xor(x3, k3);
        return f(x3_xor_k3);
    }

    public static int r4(final long plaintext, final Keychain keys) {
        final int x3 = x3(plaintext, keys);
        final int y3 = y3(plaintext, keys);
        final int k5 = keys.getK5();
        return xor(x3, y3, k5);
    }
}
