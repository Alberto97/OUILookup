package org.alberto97.ouilookup.tools

import org.junit.Assert.assertEquals
import org.junit.Test

class Rfc1123DateTimeTest {

    @Test
    fun parseLocalDateTime() {
        val dateTime = Rfc1123DateTime.parse("Sun, 06 Feb 2022 19:01:22 GMT")
        assertEquals(6, dateTime?.dayOfMonth)
        assertEquals(2, dateTime?.monthValue)
        assertEquals(2022, dateTime?.year)
        assertEquals(19, dateTime?.hour)
        assertEquals(1, dateTime?.minute)
        assertEquals(22, dateTime?.second)
    }

    @Test
    fun parseMillis() {
        val dateTime = Rfc1123DateTime.parseMillis("Sun, 06 Feb 2022 19:01:22 GMT")
        assertEquals(1644174082000, dateTime)
    }
}
