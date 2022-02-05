package org.alberto97.ouilookup.tools

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class OctetToolTest {

    @Test
    fun testSanitizeMac() = runBlocking {
        val result = OctetTool.sanitizeOui("000C4264A026")
        assertEquals("000C42", result)
    }

    @Test
    fun testSanitizeColon() = runBlocking {
        val result = OctetTool.sanitizeOui("00:0C:42")
        assertEquals("000C42", result)
    }

    @Test
    fun testSanitizeHyphen() = runBlocking {
        val result = OctetTool.sanitizeOui("00-0C-42")
        assertEquals("000C42", result)
    }

    @Test
    fun testIsOui() {
        val result = OctetTool.isOui("00005e")
        assert(result)
    }

    @Test
    fun testIsOuiDelimited() {
        val result = OctetTool.isOui("00:00:5e")
        assert(result)
    }

    @Test
    fun testIsOuiDelimitedFailure() {
        val result = OctetTool.isOui("00:00:gg")
        assert(!result)
    }

    @Test
    fun testIsMacAddress() {
        val result = OctetTool.isMacAddress("00005e0053af")
        assert(result)
    }

    @Test
    fun testIsMacAddressDelimited() {
        val result = OctetTool.isMacAddress("00:00:5e:00:53:af")
        assert(result)
    }
}