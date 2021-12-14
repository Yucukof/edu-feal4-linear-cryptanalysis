package org.dcu.student.sem1.ca642.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvRuntimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.feal.Keychain;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple helper class to read the list of known plaintexts and ciphertexts into memory, using OpenCsv library.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvHelper {

    /**
     * Reads a list of known pairs of plaintext-ciphertext from a flat CSV file.
     *
     * @param filename the name of the file to read.
     * @return a set of csv entries.
     */
    public static Set<CsvEntry> loadDictionary(final String filename) {

        final File file = new File(filename);
        log.info("Loading plaintext-ciphertext pairs from\n\t[{}]", file.getAbsolutePath());

        try (final CSVReader reader = new CSVReader(new FileReader(file))) {

            final HeaderColumnNameMappingStrategy<CsvEntry> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(CsvEntry.class);

            CsvToBean<CsvEntry> csvToBean = new CsvToBeanBuilder<CsvEntry>(reader)
                  .withMappingStrategy(strategy)
                  .withSeparator(';')
                  .withQuoteChar('"')
                  .build();

            final Set<CsvEntry> dictionary = csvToBean.stream()
                  .collect(Collectors.toSet());

            log.info("Dictionary loaded.");
            return dictionary;

        } catch (IOException e) {
            log.error("failed to load dictionary!", e);
            throw new CsvRuntimeException("Failed to load dictionary!", e);
        }
    }

    /**
     * Export the given keychains to a flat CSV file.
     *
     * @param keychains the collection of keychains to export.
     * @param filename  the name of the file to create.
     * @throws IOException when IO magic goes wrong.
     */
    public static void exportKeychains(final Collection<Keychain> keychains, final String filename) throws IOException {

        log.info("Exporting keychains to csv...");

        final File file = new File(filename);
        try (final CSVWriter writer = new CSVWriter(new FileWriter(filename))) {

            final HeaderColumnNameMappingStrategy<Keychain> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
            mappingStrategy.setType(Keychain.class);

            final StatefulBeanToCsv<Keychain> appender = new StatefulBeanToCsvBuilder<Keychain>(writer)
                  .withMappingStrategy(mappingStrategy)
                  .withSeparator(';')
                  .withQuotechar('"')
                  .build();

            keychains.forEach(entry -> write(appender, entry));
            log.info("Keychains exported to\n\t[{}]", file.getAbsolutePath());
        }
    }

    /**
     * Write a single keychain to the CSV file.
     *
     * @param appender the OpenCSV util to write beans in a stateful manner.
     * @param keychain the keychain to write.
     */
    private static void write(final StatefulBeanToCsv<Keychain> appender, final Keychain keychain) {
        try {
            appender.write(keychain);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new CsvRuntimeException("Failed to write bean to CSV!", e);
        }
    }
}
