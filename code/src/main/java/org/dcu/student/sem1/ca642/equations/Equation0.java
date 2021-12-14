package org.dcu.student.sem1.ca642.equations;

import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.feal.Keychain;

import static org.dcu.student.sem1.ca642.FEAL.f;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.*;

public class Equation0 extends Relationship implements Equation {

    public Equation0(final int[] outputBits, final int[] inputBits) {
        super(outputBits, inputBits);
    }

    @Override
    public Integer apply(final CsvEntry entry, final Keychain keychain) {

        final long plaintext = entry.getPlaintext();
        final int l_0 = left(plaintext);
        final int r_0 = right(plaintext);

        final long ciphertext = entry.getCiphertext();
        final int l_4 = left(ciphertext);
        final int r_4 = right(ciphertext);

        final int k_0 = keychain.getK0();

        final int term1 = getTerm1(l_0, r_0, l_4);
        final int term2 = getTerm2(l_0, l_4, r_4);
        final int term3 = getTerm3(l_0, r_0, k_0);

        return xor(term1, term2, term3);
    }

    private int getTerm1(final int l_0, final int r_0, final int l_4) {
        final int xor = xor(l_0, r_0, l_4);
        final int s = extract(xor, getOutputBits());
        return parityOf(s);
    }

    private int getTerm2(final int l_0, final int l_4, final int r_4) {
        final int xor = xor(l_0, l_4, r_4);
        final int s = extract(xor, getInputBits());
        return parityOf(s);
    }

    private int getTerm3(final int l_0, final int r_0, final int k_0) {
        final int xor = xor(l_0, r_0, k_0);
        final int f = f(xor);
        final int s = extract(f, getInputBits());
        return parityOf(s);
    }
}
