package org.dcu.student.sem1.ca642.equations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dcu.student.sem1.ca642.FEAL;

/**
 * An abstract class representing a linear relationship between input and output bits to the F function.
 *
 * @see FEAL#f(int)
 */
@Data
@RequiredArgsConstructor
public abstract class Relationship {

    /**
     * The indexes of the bits from the output.
     */
    private final int[] outputBits;
    /**
     * The indexes of the bits from the input.
     */
    private final int[] inputBits;

}
