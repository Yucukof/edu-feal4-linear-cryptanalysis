package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.equations.Equation;
import org.dcu.student.sem1.ca642.equations.Equation4;
import org.dcu.student.sem1.ca642.feal.Keychain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class K4 {

    public static List<Keychain> crack(final Collection<CsvEntry> dictionary, final List<Keychain> k3Candidates) {

        log.info("Attacking K4...");

        final List<Keychain> k4Candidates = new Resolve().process(dictionary, k3Candidates);
        log.info("Found {} K4 key candidates.", k4Candidates.size());

        return k4Candidates;
    }

    private static class Resolve {

        final Equation equation = new Equation4();

        private List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> candidates) {
            log.info("Processing plaintext-ciphertext matching...");
            return candidates.stream()
                  .peek(i -> Analysis.countOperation())
                  .filter(candidate -> equation.isConstant(dictionary, candidate))
                  .map(candidate -> update(dictionary, candidate))
                  .collect(Collectors.toList());
        }

        public Keychain update(final Collection<CsvEntry> dictionary, final Keychain keychain) {
            final CsvEntry entry = dictionary.stream()
                  .findAny()
                  // Should not happen
                  .orElseThrow(RuntimeException::new);

            final int k_4 = equation.apply(entry, keychain);

            return keychain.toBuilder()
                  .k4(k_4)
                  .build();
        }
    }
}
