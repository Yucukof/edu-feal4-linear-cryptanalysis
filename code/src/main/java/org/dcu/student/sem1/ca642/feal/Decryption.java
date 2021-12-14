package org.dcu.student.sem1.ca642.feal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.dcu.student.sem1.ca642.FEAL.f;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.*;

/**
 * The modified FEAL-4 decryption algorithm.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Decryption {

    public static long decrypt(final long ciphertext, final Keychain keys) {
        final int l0 = l0(ciphertext, keys);
        final int r0 = r0(ciphertext, keys);
        return concatenate(l0, r0);
    }

    public static int l0(final long ciphertext, final Keychain keys) {
        final int y0 = y0(ciphertext, keys);
        final int x1 = x1(ciphertext, keys);
        return xor(y0, x1);
    }

    public static int r0(final long ciphertext, final Keychain keys) {
        final int x0 = x0(ciphertext, keys);
        final int l0 = l0(ciphertext, keys);
        return xor(x0, l0);
    }

    public static int x0(final long ciphertext, final Keychain keys) {
        final int x2 = x2(ciphertext, keys);
        final int y1 = y1(ciphertext, keys);
        return xor(x2, y1);
    }

    public static int x1(final long ciphertext, final Keychain keys) {
        final int x3 = x3(ciphertext, keys);
        final int y2 = y2(ciphertext, keys);
        return xor(x3, y2);
    }

    public static int x2(final long ciphertext, final Keychain keys) {
        final int x4 = x4(ciphertext, keys);
        final int y3 = y3(ciphertext, keys);
        return xor(x4, y3);
    }

    public static int x3(final long ciphertext, final Keychain keys) {
        final int r4 = right(ciphertext);
        final int k5 = keys.getK5();
        final int x4 = x4(ciphertext, keys);
        return xor(r4, k5, x4);
    }

    public static int x4(final long ciphertext, final Keychain keys) {
        final int l4 = left(ciphertext);
        final int k4 = keys.getK4();
        return xor(l4, k4);
    }

    public static int y0(final long ciphertext, final Keychain keys) {
        final int x0 = x0(ciphertext, keys);
        final int k0 = keys.getK0();
        final int x0_xor_k0 = xor(x0, k0);
        return f(x0_xor_k0);
    }

    public static int y1(final long ciphertext, final Keychain keys) {
        final int x1 = x1(ciphertext, keys);
        final int k1 = keys.getK1();
        final int x1_xor_k1 = xor(x1, k1);
        return f(x1_xor_k1);
    }

    public static int y2(final long ciphertext, final Keychain keys) {
        final int x2 = x2(ciphertext, keys);
        final int k2 = keys.getK2();
        final int x2_xor_k2 = xor(x2, k2);
        return f(x2_xor_k2);
    }

    public static int y3(final long ciphertext, final Keychain keys) {
        final int x3 = x3(ciphertext, keys);
        final int k3 = keys.getK3();
        final int x3_xor_k3 = xor(x3, k3);
        return f(x3_xor_k3);
    }
}
