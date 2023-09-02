package org.alberto97.ouilookup.tools

import java.lang.NumberFormatException


object OctetTool {
    private val delimiters = listOf(':', '-')
    private val delimitersStr = delimiters.joinToString("")

    fun sanitizeOui(oui: String): String {
        return oui.removeDelimiters().takeOui().uppercase()
    }

    private fun String.removeDelimiters(): String {
        return this.filterNot { char -> delimitersStr.contains(char) }
    }

    private fun String.takeOui(): String {
        return this.take(6)
    }

    fun isMacAddress(value: String): Boolean {
        val groups = 6
        return isValidDelimitedOctetGroup(value, groups) || isValidOctetGroup(value, groups)
    }

    fun isOui(value: String): Boolean {
        val groups = 3
        return isValidDelimitedOctetGroup(value, groups) || isValidOctetGroup(value, groups)
    }

    private fun isValidOctetGroup(value: String, numberOfOctets: Int): Boolean {
        val parts = value.chunked(2)
        return isValidOctetList(parts, numberOfOctets)
    }

    private fun isValidDelimitedOctetGroup(value: String, numberOfOctets: Int): Boolean {
        val parts = value.split(*delimiters.toCharArray())
        return isValidOctetList(parts, numberOfOctets)
    }

    private fun isValidOctetList(parts: List<String>, numberOfOctets: Int): Boolean {
        if (parts.count() != numberOfOctets)
            return false

        for (part in parts) {
            if (!isValidOctet(part))
                return false
        }
        return true
    }

    private fun isValidOctet(value: String): Boolean {
        try {
            val x = value.toInt(16)
            if (x >= 0 || x <= 0xff)
                return true
        } catch (_: NumberFormatException) {
        }
        return false
    }
}
