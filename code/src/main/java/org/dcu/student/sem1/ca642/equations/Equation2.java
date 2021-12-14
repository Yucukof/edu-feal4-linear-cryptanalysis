package org.dcu.student.sem1.ca642.equations;

import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.feal.Keychain;

import static org.dcu.student.sem1.ca642.FEAL.f;
import static org.dcu.student.sem1.ca642.feal.Encryption.x1;
import static org.dcu.student.sem1.ca642.feal.Encryption.x2;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.*;

public class Equation2 extends Relationship implements Equation{

    public Equation2(final int[] outputBits, final int[] inputBits) {
        super(outputBits, inputBits);
    }

    @Override
    public Integer apply(final CsvEntry entry, final Keychain keychain) {

        final long plaintext = entry.getPlaintext();
        final int x_1 = x1(plaintext, keychain);
        final int x_2 = x2(plaintext, keychain);

        final long ciphertext = entry.getCiphertext();
        final int l_4 = left(ciphertext);

        final int k_2 = keychain.getK2();

        final int term1 = getTerm1(x_2, l_4);
        final int term2 = getTerm2(x_1);
        final int term3 = getTerm3(x_2, k_2);

        return xor(term1, term2, term3);
    }

    private int getTerm1(final int x_2, final int l_4) {
        final int xor = xor(x_2, l_4);
        final int s = extract(xor, getOutputBits());
        return parityOf(s);
    }

    private int getTerm2(final int x_1) {
        final int s = extract(x_1, getInputBits());
        return parityOf(s);
    }

    private int getTerm3(final int x_2, final int k_2) {
        final int xor = xor(x_2, k_2);
        final int f = f(xor);
        final int s = extract(f, getInputBits());
        return parityOf(s);
    }
}
