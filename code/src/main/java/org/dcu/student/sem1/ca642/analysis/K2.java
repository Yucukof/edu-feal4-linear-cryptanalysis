package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.equations.Equation;
import org.dcu.student.sem1.ca642.equations.Equation2;
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
public class K2 {

    public static List<Keychain> crack(final Collection<CsvEntry> dictionary, final List<Keychain> k1Candidates) {

        log.info("Attacking K2...");

        final List<Keychain> k2PrimeCandidates = new Prime().process(dictionary, k1Candidates);
        log.info("Found {} K'2 key candidates.", k2PrimeCandidates.size());

        final List<Keychain> k2Candidates = new Expand().process(dictionary, k2PrimeCandidates);
        log.info("Found {} K2 key candidates.", k2Candidates.size());

        final List<Keychain> k2Confirmed = new Validate().process(dictionary, k2Candidates);
        log.info("Found {} K2 confirmed key candidates.", k2Confirmed.size());

        return k2Confirmed;
    }

    private static class Expand {

        final Equation equation = new Equation2(new int[]{23, 29}, new int[]{31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> keychains) {

            log.info("Expanding possible K'2...");

            return keychains.stream()
                  .map(candidate -> processCandidate(dictionary, candidate))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processCandidate(final Collection<CsvEntry> dictionary, final Keychain keychain) {

            final int k2 = keychain.getK2();
            final byte[] bytes = int2bytes(k2);
            final byte b0_xor_b1 = bytes[1];
            final byte b2_xor_b3 = bytes[2];

            final List<Keychain> approvedCandidates = new ArrayList<>();
            for (byte b0 = Byte.MIN_VALUE; b0 < Byte.MAX_VALUE; b0++) {
                final byte b1 = xor(b0_xor_b1, b0);

                for (byte b3 = Byte.MIN_VALUE; b3 < Byte.MAX_VALUE; b3++) {
                    final byte b2 = xor(b2_xor_b3, b3);

                    final byte[] b = concatenate(b0, b1, b2, b3);
                    // Creates a K2 candidate from K'0
                    final int candidate = bytes2int(b);

                    keychain.setK2(candidate);
                    Analysis.countOperation();

                    final boolean isValidCandidate = equation.isConstant(dictionary, keychain);
                    Analysis.countOperation();

                    if (isValidCandidate) {
                        approvedCandidates.add(keychain.clone());
                    }
                }
            }
            if (!approvedCandidates.isEmpty()) {
                log.info("Successfully expanded K'2 [{}]", Binary.from(k2));
            }
            return approvedCandidates;
        }
    }

    private static class Prime {

        final Equation equation1 = new Equation2(new int[]{5, 13, 21}, new int[]{15});
        final Equation equation2 = new Equation2(new int[]{15, 21, 23, 29}, new int[]{23});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k1Candidates) {

            log.info("Processing K'2...");
            return k1Candidates.stream()
                  .map(k1 -> processByK1(dictionary, k1))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processByK1(final Collection<CsvEntry> dictionary, final Keychain keychain) {
            final int key_space = (1 << 16);
            return IntStream.range(0, key_space)
                  .map(i -> i << 8)
                  .mapToObj(k2Prime -> keychain.toBuilder().k2(k2Prime).build())
                  .peek(i -> Analysis.countOperation())
                  .filter(k2Prime -> equation1.isConstant(dictionary, k2Prime))
                  .peek(i -> Analysis.countOperation())
                  .filter(k2Prime -> equation2.isConstant(dictionary, k2Prime))
                  .collect(Collectors.toList());
        }
    }

    private static class Validate {

        final Equation equation1 = new Equation2(new int[]{13}, new int[]{7, 15, 23, 31});
        final Equation equation2 = new Equation2(new int[]{5, 15}, new int[]{7});
        final Equation equation3 = new Equation2(new int[]{15, 21}, new int[]{23, 31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k2Candidates) {

            log.info("Validating retained K2...");
            return k2Candidates.stream()
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
