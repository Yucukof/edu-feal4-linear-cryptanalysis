package org.dcu.student.sem1.ca642.equations;

import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.feal.Keychain;

import static org.dcu.student.sem1.ca642.feal.Encryption.x3;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.*;

public class Equation5 implements Equation {

    @Override
    public Integer apply(final CsvEntry entry, final Keychain keychain) {

        final long plaintext = entry.getPlaintext();
        final int x_3 = x3(plaintext, keychain);

        final long ciphertext = entry.getCiphertext();
        final int l_4 = left(ciphertext);
        final int r_4 = right(ciphertext);

        final int k_4 = keychain.getK4();

        return xor(x_3, l_4, r_4, k_4);
    }
}
