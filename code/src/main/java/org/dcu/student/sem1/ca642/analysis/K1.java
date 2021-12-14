package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.equations.Equation;
import org.dcu.student.sem1.ca642.equations.Equation1;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class K1 {

    public static List<Keychain> crack(final Collection<CsvEntry> dictionary, final List<Keychain> k0Candidates) {

        log.info("Attacking K1...");

        final List<Keychain> k1PrimeCandidates = new Prime().process(dictionary, k0Candidates);
        log.info("Found {} K'1 key candidates.", k1PrimeCandidates.size());

        final List<Keychain> k1Candidates = new Expand().process(dictionary, k1PrimeCandidates);
        log.info("Found {} K1 key candidates.", k1Candidates.size());

        final List<Keychain> k1Confirmed = new Validate().process(dictionary, k1Candidates);
        log.info("Found {} K1 confirmed key candidates.", k1Confirmed.size());

        return k1Confirmed;
    }

    private static class Expand {

        final Equation equation = new Equation1(new int[]{23, 29}, new int[]{31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> keychains) {

            log.info("Expanding possible K'1...");

            return keychains.stream()
                  .map(candidate -> processCandidate(dictionary, candidate))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processCandidate(final Collection<CsvEntry> dictionary, final Keychain keychain) {

            final int k1 = keychain.getK1();
            final byte[] bytes = int2bytes(k1);
            final byte b0_xor_b1 = bytes[1];
            final byte b2_xor_b3 = bytes[2];

            final List<Keychain> approvedCandidates = new ArrayList<>();
            for (byte b0 = Byte.MIN_VALUE; b0 < Byte.MAX_VALUE; b0++) {
                final byte b1 = xor(b0_xor_b1, b0);

                for (byte b3 = Byte.MIN_VALUE; b3 < Byte.MAX_VALUE; b3++) {
                    final byte b2 = xor(b2_xor_b3, b3);

                    final byte[] b = concatenate(b0, b1, b2, b3);
                    // Creates a K1 candidate from K'0
                    final int candidate = bytes2int(b);

                    keychain.setK1(candidate);
                    Analysis.countOperation();

                    final boolean isValidCandidate = equation.isConstant(dictionary, keychain);
                    Analysis.countOperation();

                    if (isValidCandidate) {
                        approvedCandidates.add(keychain.clone());
                    }
                }
            }
            if (!approvedCandidates.isEmpty()) {
                log.info("Successfully expanded K'1 [{}]", Binary.from(k1));
            }
            return approvedCandidates;
        }
    }

    private static class Prime {

        final Equation equation1 = new Equation1(new int[]{5, 13, 21}, new int[]{15});
        final Equation equation2 = new Equation1(new int[]{15, 21, 23, 29}, new int[]{23});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k0Candidates) {

            log.info("Processing K'1...");
            return k0Candidates.stream()
                  .map(k0 -> processByK0(dictionary, k0))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processByK0(final Collection<CsvEntry> dictionary, final Keychain keychain) {
            final int key_space = (1 << 16);
            return IntStream.range(0, key_space)
                  .map(i -> i << 8)
                  .mapToObj(k1 -> keychain.toBuilder().k1(k1).build())
                  .peek(i -> Analysis.countOperation())
                  .filter(k1 -> equation1.isConstant(dictionary, k1))
                  .peek(i -> Analysis.countOperation())
                  .filter(k1 -> equation2.isConstant(dictionary, k1))
                  .collect(Collectors.toList());
        }
    }

    private static class Validate {

        final Equation equation1 = new Equation1(new int[]{13}, new int[]{7, 15, 23, 31});
        final Equation equation2 = new Equation1(new int[]{5, 15}, new int[]{7});
        final Equation equation3 = new Equation1(new int[]{15, 21}, new int[]{23, 31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k1Candidates) {

            log.info("Validating retained K1...");
            return k1Candidates.stream()
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation1.isConstant(dictionary, i))
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation2.isConstant(dictionary, i))
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation3.isConstant(dictionary, i))
                  .collect(Collectors.toList());
        }
    }
}
