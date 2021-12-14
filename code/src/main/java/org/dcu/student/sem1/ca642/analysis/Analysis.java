package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.csv.CsvHelper;
import org.dcu.student.sem1.ca642.feal.Keychain;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * The main program to analyse and break the keys of the FEAL-4 algorithm, using linear cryptanalysis.
 */
@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Analysis {

    /**
     * A simple long integer counter to keep track of the number of operations needed to hack the keys.
     */
    private static final AtomicLong operationsCounter = new AtomicLong(0);

    /**
     * Increments the operation counter.
     */
    public static void countOperation() {
        operationsCounter.getAndIncrement();
    }

    /**
     * The orchestrator method in the cryptanalysis of FEAL-4, that
     * <br> - loads the dictionary,
     * <br> - hacks its keys,
     * <br> - double-checks its finding, and
     * <br> - outputs the result.
     *
     * @return a set of valid FEAL-4 key sets.
     * @throws IOException when issues occurs during CSV operations.
     */
    public static Set<Keychain> run() throws IOException {

        log.info("Running new analysis...");
        final Set<CsvEntry> dictionary = CsvHelper.loadDictionary("dictionary.csv");
        final List<Keychain> candidateKeychains = hack(dictionary);
        final Set<Keychain> effectiveKeychains = check(dictionary, candidateKeychains);
        review(effectiveKeychains);
        CsvHelper.exportKeychains(effectiveKeychains, "keychains.csv");
        log.info("Analysis completed.");
        return effectiveKeychains;
    }

    /**
     * Try and get the possible key sets used to encrypt a given list of known pairs.
     *
     * @param dictionary the collection of known pairs plaintext-ciphertext.
     * @return a list containing all the valid key sets.
     */
    private static List<Keychain> hack(final Collection<CsvEntry> dictionary) {
        log.info("Running key hack...");
        final List<Keychain> k0Candidates = K0.crack(dictionary);
        final List<Keychain> k1Candidates = K1.crack(dictionary, k0Candidates);
        final List<Keychain> k2Candidates = K2.crack(dictionary, k1Candidates);
        final List<Keychain> k3Candidates = K3.crack(dictionary, k2Candidates);
        final List<Keychain> k4Candidates = K4.crack(dictionary, k3Candidates);
        final List<Keychain> k5Candidates = K5.crack(dictionary, k4Candidates);
        log.info("Key hacking completed.");
        log.info("Number of operations: [{}]", operationsCounter.get());
        return k5Candidates;
    }

    /**
     * Verifies that the possible key sets found for a given collection of known pairs effectively matches this
     * collection.
     *
     * @param dictionary         the collection of all the known pairs plaintext-ciphertext.
     * @param candidateKeychains the collection of all possible key sets.
     * @return a set containing all the key sets confirmed.
     */
    private static Set<Keychain> check(final Collection<CsvEntry> dictionary, final Collection<Keychain> candidateKeychains) {
        log.info("Checking candidate keys...");
        final Set<Keychain> confirmedKeychains = candidateKeychains.stream()
              .filter(keychain -> keychain.matches(dictionary))
              .collect(Collectors.toSet());
        log.info("Check over.");
        return confirmedKeychains;
    }

    /**
     * Analyses the confirmed key sets and display relevant information in the logs and the prompt.
     *
     * @param effectiveKeychains the collection of confirmed key sets.
     */
    private static void review(final Set<Keychain> effectiveKeychains) {
        log.info("Reviewing...");
        if (effectiveKeychains.isEmpty()) {
            Review.noneMatch();
            return;
        }
        if (effectiveKeychains.size() == 1) {
            Review.singleMatch(effectiveKeychains);
            return;
        }
        Review.multipleMatch(effectiveKeychains);
    }
}
