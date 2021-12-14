package org.dcu.student.sem1.ca642.csv;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dcu.student.sem1.ca642.csv.CsvHelper.loadDictionary;

public class CsvHelperTest {

    @Test
    public void given_dictionary_csv_when_loading_then_expect_map() {
        final Set<CsvEntry> map = loadDictionary("dictionary.csv");

        assertThat(map)
              .isNotNull()
              .isNotEmpty();

        assertThat(map.size()).isEqualTo(200);
    }

}