package com.yourbrand.englishlearn.learning

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MeaningDistractorTest {
    @Test fun girlAndDaughterAreNotPairedAsMeaningDistractors() {
        assertFalse(meaningDistractorAllowed("girl", "daughter"))
        assertFalse(meaningDistractorAllowed("daughter", "girl"))
        assertTrue(meaningDistractorAllowed("girl", "teacher"))
    }
}
