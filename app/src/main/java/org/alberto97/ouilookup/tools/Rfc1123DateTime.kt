package org.alberto97.ouilookup.tools

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Date in the format: Sun, 06 Feb 2022 19:01:22 GMT
object Rfc1123DateTime {
    fun parse(value: String): LocalDateTime? {
        val rfc1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
        return try {
            LocalDateTime.parse(value, rfc1123)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun parseMillis(value: String): Long? {
        val localDateTime = parse(value)
        return localDateTime?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    }
}
