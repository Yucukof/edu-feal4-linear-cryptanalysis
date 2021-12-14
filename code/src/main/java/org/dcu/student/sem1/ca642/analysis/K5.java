package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.equations.Equation;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.equations.Equation5;
import org.dcu.student.sem1.ca642.feal.Keychain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class K5 {

    public static List<Keychain> crack(final Collection<CsvEntry> dictionary, final List<Keychain> k4Candidates) {

        log.info("Attacking K5...");

        final List<Keychain> k5Candidates = new Resolve().process(dictionary, k4Candidates);
        log.info("Found {} K5 key candidates.", k5Candidates.size());

        return k5Candidates;
    }

    private static class Resolve {

        final Equation equation = new Equation5();

        private List<Keychain> process(final Collection<CsvEntry> dictionary, final List<Keychain> candidates) {
            log.info("Processing plaintext-ciphertext matching...");
            return candidates.stream()
                  .peek(i -> Analysis.countOperation())
                  .filter(candidate -> equation.isConstant(dictionary, candidate))
                  .map(candidate -> update(dictionary, candidate))
                  .collect(Collectors.toList());
        }

        public Keychain update(final Collection<CsvEntry> dictionary, final Keychain candidate) {
            final CsvEntry entry = dictionary.stream()
                  .findAny()
                  // Should not happen
                  .orElseThrow(RuntimeException::new);

            final int k_5 = equation.apply(entry, candidate);

            return candidate.toBuilder()
                  .k5(k_5)
                  .build();
        }
    }
}
