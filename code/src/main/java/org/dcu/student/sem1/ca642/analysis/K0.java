package org.dcu.student.sem1.ca642.analysis;

import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.equations.Equation;
import org.dcu.student.sem1.ca642.equations.Equation0;
import org.dcu.student.sem1.ca642.feal.Keychain;
import org.dcu.student.sem1.ca642.model.Binary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.dcu.student.sem1.ca642.utils.BytesUtils.concatenate;
import static org.dcu.student.sem1.ca642.utils.BytesUtils.xor;
import static org.dcu.student.sem1.ca642.utils.ConversionUtils.bytes2int;
import static org.dcu.student.sem1.ca642.utils.ConversionUtils.int2bytes;

@Slf4j
public class K0 {

    public static List<Keychain> crack(final Collection<CsvEntry> dictionary) {

        log.info("Attacking K0...");

        final List<Keychain> k0PrimeCandidates = new Prime().process(dictionary);
        log.info("Found {} K'0 key candidates.", k0PrimeCandidates.size());

        final List<Keychain> k0Candidates = new Expand().process(dictionary, k0PrimeCandidates);
        log.info("Found {} K0 key candidates.", k0Candidates.size());

        final List<Keychain> k0Confirmed = new Validate().process(dictionary, k0Candidates);
        log.info("Found {} K0 confirmed key candidates.", k0Confirmed.size());

        return k0Confirmed;
    }

    private static class Expand {

        final Equation equation = new Equation0(new int[]{23, 29}, new int[]{31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k0PrimeCandidates) {
            log.info("Expanding possible K'0...");
            return k0PrimeCandidates.stream()
                  .map(candidate -> processCandidate(dictionary, candidate))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processCandidate(final Collection<CsvEntry> dictionary, final Keychain keychain) {

            final int k0Prime = keychain.getK0();
            final byte[] bytes = int2bytes(k0Prime);
            final byte b0_xor_b1 = bytes[1];
            final byte b2_xor_b3 = bytes[2];

            final List<Keychain> approvedCandidates = new ArrayList<>();
            for (byte b0 = Byte.MIN_VALUE; b0 < Byte.MAX_VALUE; b0++) {
                final byte b1 = xor(b0_xor_b1, b0);

                for (byte b3 = Byte.MIN_VALUE; b3 < Byte.MAX_VALUE; b3++) {
                    final byte b2 = xor(b2_xor_b3, b3);

                    final byte[] b = concatenate(b0, b1, b2, b3);
                    // Creates a K0 candidate from K'0
                    final int candidate = bytes2int(b);

                    final Keychain newChain = new Keychain(candidate);
                    Analysis.countOperation();

                    final boolean isValidCandidate = equation.isConstant(dictionary, newChain);
                    Analysis.countOperation();

                    if (isValidCandidate) {
                        approvedCandidates.add(newChain);
                    }
                }
            }
            if (!approvedCandidates.isEmpty()) {
                log.info("Successfully expanded K'0 [{}]", Binary.from(k0Prime));
            }
            return approvedCandidates;
        }
    }

    private static class Prime {

        final Equation equation1 = new Equation0(new int[]{5, 13, 21}, new int[]{15});
        final Equation equation2 = new Equation0(new int[]{15, 21, 23, 29}, new int[]{23});

        public List<Keychain> process(final Collection<CsvEntry> dictionary) {
            log.info("Processing K'0...");
            final int key_space = (1 << 16);
            return IntStream.range(0, key_space)
                  .map(i -> i << 8)
                  .mapToObj(Keychain::new)
                  .peek(i -> Analysis.countOperation())
                  .filter(k0 -> equation1.isConstant(dictionary, k0))
                  .peek(i -> Analysis.countOperation())
                  .filter(k0 -> equation2.isConstant(dictionary, k0))
                  .collect(Collectors.toList());
        }
    }

    private static class Validate {

        final Equation equation1 = new Equation0(new int[]{13}, new int[]{7, 15, 23, 31});
        final Equation equation2 = new Equation0(new int[]{5, 15}, new int[]{7});
        final Equation equation3 = new Equation0(new int[]{15, 21}, new int[]{23, 31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k0Candidates) {

            log.info("Validating retained K0...");
            return k0Candidates.stream()
                  .filter(i -> equation1.isConstant(dictionary, i))
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation2.isConstant(dictionary, i))
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation3.isConstant(dictionary, i))
                  .collect(Collectors.toList());
        }
    }
}
