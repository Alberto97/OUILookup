package org.alberto97.ouilookup.tools

import com.jsoizo.kotlincsv.csvReader
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OuiCsvParserTest {
    private val parser = OuiCsvParser(csvReader())

    @Test
    fun testParse() {
        val path = Paths.get("src","main","res", "raw", "oui.csv").toAbsolutePath().toString()
        val data = File(path).readText()

        val oui = parser.parse(data)
        val entries = oui.count()
        println("Found $entries entries")

        assertTrue(entries > 30000)
    }

    @Test
    fun parsesQuotedFieldsAndMultilineUnicodeAddresses() {
        val data = "Registry,Assignment,Organization Name,Organization Address\r\n" +
            "MA-L,001122,\"  Example, \"\"Devices\"\"  \",\"Via Roma 1\r\nCittà IT\"\r\n"

        val oui = parser.parse(data).single()
        assertEquals("001122", oui.oui)
        assertEquals("Example, \"Devices\"", oui.orgName)
        assertEquals("Via Roma 1\r\nCittà IT", oui.orgAddress)
    }

    @Test
    fun mapsColumnsByHeaderAndPreservesEmptyAddresses() {
        val data = "Organization Address,Organization Name,Assignment,Registry\n" +
            ",  Example Devices  ,001122,MA-L\n"

        val oui = parser.parse(data).single()
        assertEquals("001122", oui.oui)
        assertEquals("Example Devices", oui.orgName)
        assertEquals("", oui.orgAddress)
    }

    @Test
    fun headerWithoutRecordsReturnsEmptyList() {
        assertTrue(parser.parse("Registry,Assignment,Organization Name,Organization Address\n").isEmpty())
    }
}
