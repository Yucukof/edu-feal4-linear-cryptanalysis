package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.equations.Equation;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.equations.Equation3;
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
public class K3 {

    public static List<Keychain> crack(final Collection<CsvEntry> dictionary, final List<Keychain> k2Candidates) {

        log.info("Attacking K3...");

        final List<Keychain> k3PrimeCandidates = new Prime().process(dictionary, k2Candidates);
        log.info("Found {} K'3 key candidates.", k3PrimeCandidates.size());

        final List<Keychain> k3Candidates = new Expand().process(dictionary, k3PrimeCandidates);
        log.info("Found {} K3 key candidates.", k3Candidates.size());

        final List<Keychain> k3Confirmed = new Validate().process(dictionary, k3Candidates);
        log.info("Found {} K3 confirmed key candidates.", k3Confirmed.size());

        return k3Confirmed;
    }

    private static class Expand {

        final Equation equation = new Equation3(new int[]{23, 29}, new int[]{31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> keychains) {

            log.info("Expanding possible K'3...");

            return keychains.stream()
                  .map(candidate -> processCandidate(dictionary, candidate))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processCandidate(final Collection<CsvEntry> dictionary, final Keychain keychain) {

            final int k3_prime = keychain.getK3();
            final byte[] bytes = int2bytes(k3_prime);
            final byte b0_xor_b1 = bytes[1];
            final byte b2_xor_b3 = bytes[2];

            final List<Keychain> approvedCandidates = new ArrayList<>();
            for (byte b0 = Byte.MIN_VALUE; b0 < Byte.MAX_VALUE; b0++) {
                final byte b1 = xor(b0_xor_b1, b0);

                for (byte b3 = Byte.MIN_VALUE; b3 < Byte.MAX_VALUE; b3++) {
                    final byte b2 = xor(b2_xor_b3, b3);

                    final byte[] b = concatenate(b0, b1, b2, b3);
                    // Creates a K3 candidate from K'3
                    final int candidate = bytes2int(b);

                    keychain.setK3(candidate);
                    Analysis.countOperation();

                    final boolean isValidCandidate = equation.isConstant(dictionary, keychain);
                    Analysis.countOperation();

                    if (isValidCandidate) {
                        approvedCandidates.add(keychain.clone());
                    }
                }
            }
            if (!approvedCandidates.isEmpty()) {
                log.info("Successfully expanded K'3 [{}]", Binary.from(k3_prime));
            }
            return approvedCandidates;
        }
    }

    private static class Prime {

        final Equation equation1 = new Equation3(new int[]{5, 13, 21}, new int[]{15});
        final Equation equation2 = new Equation3(new int[]{15, 21, 23, 29}, new int[]{23});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k2Candidates) {

            log.info("Processing K'3...");
            return k2Candidates.stream()
                  .map(k2 -> processByK2(dictionary, k2))
                  .flatMap(List::stream)
                  .collect(Collectors.toList());
        }

        private List<Keychain> processByK2(final Collection<CsvEntry> dictionary, final Keychain keychain) {
            final int key_space = (1 << 16);
            return IntStream.range(0, key_space)
                  .map(i -> i << 8)
                  .mapToObj(k3 -> keychain.toBuilder().k3(k3).build())
                  .peek(i -> Analysis.countOperation())
                  .filter(k3 -> equation1.isConstant(dictionary, k3))
                  .peek(i -> Analysis.countOperation())
                  .filter(k3 -> equation2.isConstant(dictionary, k3))
                  .collect(Collectors.toList());
        }
    }

    private static class Validate {

        final Equation equation1 = new Equation3(new int[]{13}, new int[]{7, 15, 23, 31});
        final Equation equation2 = new Equation3(new int[]{5, 15}, new int[]{7});
        final Equation equation3 = new Equation3(new int[]{15, 21}, new int[]{23, 31});

        public List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> k3Candidates) {

            log.info("Validating retained K3...");
            return k3Candidates.stream()
                  .filter(i -> equation1.isConstant(dictionary, i))
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation2.isConstant(dictionary, i))
                  .peek(i -> Analysis.countOperation())
                  .filter(i -> equation3.isConstant(dictionary, i))
                  .collect(Collectors.toList());
        }
    }
}
