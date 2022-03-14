package org.alberto97.ouilookup.tools

import org.junit.Assert.assertEquals
import org.junit.Test


class StringInspectorTest {
    @Test
    fun testSplitComma() {
        val result = StringInspector.splitForList("60:01:94,DC:15:C8")
        assert(result.any { it == "60:01:94" })
        assert(result.any { it == "DC:15:C8" })
    }

    @Test
    fun testSplitSemicolon() {
        val result = StringInspector.splitForList("60:01:94;DC:15:C8")
        assert(result.any { it == "60:01:94" })
        assert(result.any { it == "DC:15:C8" })
    }

    @Test
    fun testSplitWhitespace() {
        val result = StringInspector.splitForList("60:01:94 DC:15:C8")
        assert(result.any { it == "60:01:94" })
        assert(result.any { it == "DC:15:C8" })
    }

    @Test
    fun testSplitNewline() {
        val result = StringInspector.splitForList("60:01:94\nDC:15:C8")
        assert(result.any { it == "60:01:94" })
        assert(result.any { it == "DC:15:C8" })
    }

    @Test
    fun testContainsOneAddress() {
        val result = StringInspector.countSearchable(listOf("60:01:94:00:00:00"))
        assertEquals(1, result)
    }

    @Test
    fun testContainsSomeAddresses() {
        val result = StringInspector.countSearchable(listOf("60:01:94:00:00:00", "DC:15:C8:00:00:00", "AB"))
        assertEquals(2, result)
    }

    @Test
    fun testContainsAllAddresses() {
        val result = StringInspector.countSearchable(listOf("60:01:94:00:00:00", "DC:15:C8:00:00:00"))
        assertEquals(2, result)
    }
}