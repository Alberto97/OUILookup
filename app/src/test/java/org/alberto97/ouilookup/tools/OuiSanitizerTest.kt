package org.alberto97.ouilookup.tools

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class OuiSanitizerTest {

    @Test
    fun testSanitizeMac() = runBlocking {
        val result = OuiSanitizer.sanitize("000C4264A026")
        assertEquals("000C42", result)
    }

    @Test
    fun testSanitizeColon() = runBlocking {
        val result = OuiSanitizer.sanitize("00:0C:42")
        assertEquals("000C42", result)
    }

    @Test
    fun testSanitizeHyphen() = runBlocking {
        val result = OuiSanitizer.sanitize("00-0C-42")
        assertEquals("000C42", result)
    }
}