package org.dcu.student.sem1.ca642.equations;

import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.feal.Keychain;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * A generic interface to represent the behaviour of a linear equation.
 */
public interface Equation extends BiFunction<CsvEntry, Keychain, Integer> {

    /**
     * Checks if for all known pairs plaintext-ciphertext and a given keychain, the linear equation holds.
     * <br> i.e. that the unknown constant value remain the same for all entries under this equation.
     *
     * @param dictionary the list of known pairs.
     * @param keychain   the keychain with the known keys.
     * @return true if the evaluation of the equation for all pairs is all 0 or 1, false otherwise.
     */
    default boolean isConstant(final Collection<CsvEntry> dictionary, final Keychain keychain) {
        Integer constant = null;
        for (CsvEntry entry : dictionary) {
            final int value = apply(entry, keychain);
            if (constant == null) {
                constant = value;
            }
            if (constant != value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluate the value of the unknown constant value for the given pair plaintext-ciphertext.
     *
     * @param entry    a known pair.
     * @param keychain the keychain with the known keys.
     * @return the value of the unknown constant for the equation to hold true.
     */
    @Override
    Integer apply(final CsvEntry entry, final Keychain keychain);
}
