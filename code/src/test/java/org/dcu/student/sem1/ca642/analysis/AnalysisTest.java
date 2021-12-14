package org.dcu.student.sem1.ca642.analysis;

import org.dcu.student.sem1.ca642.feal.Keychain;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalysisTest {

    @Test
    public void given_dictionary_when_test_then_expect_candidates() throws IOException {
        final Set<Keychain> keychains = Analysis.run();
        assertThat(keychains.size()).isEqualTo(192);
    }
}