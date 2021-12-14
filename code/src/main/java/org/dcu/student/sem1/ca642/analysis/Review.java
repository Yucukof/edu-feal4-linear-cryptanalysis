package org.dcu.student.sem1.ca642.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dcu.student.sem1.ca642.feal.Keychain;
import org.dcu.student.sem1.ca642.model.Binary;

import java.util.Collection;
import java.util.stream.Stream;

import static org.dcu.student.sem1.ca642.utils.BytesUtils.xor;

/**
 * A simple clsss to isolate methods uses to review the key sets obtained at the end of an analysis run.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {

    /**
     * When not a single valid key set is found.
     */
    public static void noneMatch() {
        log.error("0 keychain found.");
        log.error("BOOM: you failed to approach FEAL-4...");
    }

    /**
     * When exactly a single key set is found.
     *
     * @param keychains the set of possible key sets, containing a unique set.
     */
    public static void singleMatch(final Collection<Keychain> keychains) {
        log.info("Unique Keychain found.");
        log.info("CONGRATS: You broke FEAL-4!");

        final Keychain keychain = keychains.stream()
              .findAny()
              .orElseThrow(RuntimeException::new);
        log.info("{}", keychain);
    }

    /**
     * When more than a single valid key set is found.
     *
     * @param keychains the collection of possible key sets.
     */
    public static void multipleMatch(final Collection<Keychain> keychains) {
        log.info("[{}] keychains found.", keychains.size());
        printKnowPositions(keychains);
        printKnownValues(keychains);
    }

    /**
     * Display for each key the bit positions known (1) and unknown (0).
     *
     * @param keychains the collection of possible key sets.
     * @see #printKnownValues(Collection)
     */
    private static void printKnowPositions(final Collection<Keychain> keychains) {
        final Binary k0 = getKnownBitsPosition(keychains.stream().map(Keychain::getK0));
        final Binary k1 = getKnownBitsPosition(keychains.stream().map(Keychain::getK1));
        final Binary k2 = getKnownBitsPosition(keychains.stream().map(Keychain::getK2));
        final Binary k3 = getKnownBitsPosition(keychains.stream().map(Keychain::getK3));
        final Binary k4 = getKnownBitsPosition(keychains.stream().map(Keychain::getK4));
        final Binary k5 = getKnownBitsPosition(keychains.stream().map(Keychain::getK5));

        final String str0 = String.format("K0 = [%s]", k0);
        final String str1 = String.format("K1 = [%s]", k1);
        final String str2 = String.format("K2 = [%s]", k2);
        final String str3 = String.format("K3 = [%s]", k3);
        final String str4 = String.format("K4 = [%s]", k4);
        final String str5 = String.format("K5 = [%s]", k5);

        log.info("Known bits position:\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}"
              , str0
              , str1
              , str2
              , str3
              , str4
              , str5);
    }


    /**
     * Display for each key the known bit values (unknown bit values will be shown as 0).
     * <br> To be considered in relation with {@link #printKnowPositions(Collection) known positions}.
     *
     * @param keychains the collection of possible key sets.
     * @see #printKnowPositions(Collection)
     */
    private static void printKnownValues(final Collection<Keychain> keychains) {
        final Binary k0 = getKnownBitsValue(keychains.stream().map(Keychain::getK0));
        final Binary k1 = getKnownBitsValue(keychains.stream().map(Keychain::getK1));
        final Binary k2 = getKnownBitsValue(keychains.stream().map(Keychain::getK2));
        final Binary k3 = getKnownBitsValue(keychains.stream().map(Keychain::getK3));
        final Binary k4 = getKnownBitsValue(keychains.stream().map(Keychain::getK4));
        final Binary k5 = getKnownBitsValue(keychains.stream().map(Keychain::getK5));

        final String str0 = String.format("K0 = [%s]", k0);
        final String str1 = String.format("K1 = [%s]", k1);
        final String str2 = String.format("K2 = [%s]", k2);
        final String str3 = String.format("K3 = [%s]", k3);
        final String str4 = String.format("K4 = [%s]", k4);
        final String str5 = String.format("K5 = [%s]", k5);

        log.info("Known bits values:\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}"
              , str0
              , str1
              , str2
              , str3
              , str4
              , str5);
    }

    private static Binary getKnownBitsPosition(final Stream<Integer> integerStream) {
        return integerStream.map(Review::toPair)
              .reduce(Review::rollingComparison)
              .map(pair -> xor(pair.getLeft(), pair.getRight()))
              .map(integer -> ~integer)
              .map(Binary::from)
              .orElse(Binary.ZERO);
    }

    private static Binary getKnownBitsValue(final Stream<Integer> integerStream) {
        return integerStream.map(Review::toPair)
              .reduce(Review::rollingComparison)
              .map(pair -> pair.getLeft() & pair.getRight())
              .map(Binary::from)
              .orElse(Binary.ZERO);
    }

    private static Pair<Integer, Integer> toPair(final Integer integer) {
        return new ImmutablePair<>(integer, integer);
    }

    private static ImmutablePair<Integer, Integer> rollingComparison(final Pair<Integer, Integer> a, final Pair<Integer, Integer> b) {
        return new ImmutablePair<>(a.getLeft() & b.getLeft(), a.getRight() | b.getRight());
    }
}
