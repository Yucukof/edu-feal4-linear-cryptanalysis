package org.dcu.student.sem1.ca642.equations;

import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.feal.Keychain;

import static org.dcu.student.sem1.ca642.feal.Encryption.x2;
import static org.dcu.student.sem1.ca642.feal.Encryption.y3;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.left;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.xor;

public class Equation4 implements Equation {

    @Override
    public Integer apply(final CsvEntry entry, final Keychain keychain) {

        final long plaintext = entry.getPlaintext();
        final int x_2 = x2(plaintext, keychain);
        final int y_3 = y3(plaintext, keychain);

        final long ciphertext = entry.getCiphertext();
        final int l_4 = left(ciphertext);

        return xor(x_2, y_3, l_4);
    }
}
