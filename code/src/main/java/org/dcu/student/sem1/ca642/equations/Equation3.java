package org.dcu.student.sem1.ca642.equations;

import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.feal.Keychain;

import static org.dcu.student.sem1.ca642.FEAL.f;
import static org.dcu.student.sem1.ca642.feal.Encryption.x2;
import static org.dcu.student.sem1.ca642.feal.Encryption.x3;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.*;

public class Equation3 extends Relationship  implements Equation{

    public Equation3(final int[] outputBits, final int[] inputBits) {
        super(outputBits, inputBits);
    }

    @Override
    public Integer apply(final CsvEntry entry, final Keychain keychain) {

        final long plaintext = entry.getPlaintext();
        final int x_2 = x2(plaintext, keychain);
        final int x_3 = x3(plaintext, keychain);

        final long ciphertext = entry.getCiphertext();
        final int l_4 = left(ciphertext);
        final int r_4 = right(ciphertext);

        final int k_3 = keychain.getK3();

        final int term1 = getTerm1(x_2, l_4);
        final int term2 = getTerm2(x_2, r_4);
        final int term3 = getTerm3(x_3, k_3);

        return xor(term1, term2, term3);
    }

    private int getTerm1(final int x_2, final int l_4) {
        final int xor = xor(x_2, l_4);
        final int s = extract(xor, getOutputBits());
        return parityOf(s);
    }

    private int getTerm2(final int x_2, final int r_4) {
        final int xor = xor(x_2, r_4);
        final int s = extract(xor, getInputBits());
        return parityOf(s);
    }

    private int getTerm3(final int x_3, final int k_3) {
        final int xor = xor(x_3, k_3);
        final int f = f(xor);
        final int s = extract(f, getInputBits());
        return parityOf(s);
    }
}
