package org.alberto97.ouilookup.tools

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class OuiCsvParserTest {
    private val parser = OuiCsvParser(csvReader())

    @Test
    fun testParse() {
        val path = Paths.get("src","main","res", "raw", "oui.csv").toAbsolutePath().toString()
        val data = File(path).readText()

        val oui = parser.parse(data)

        val lines = data.trim().lines()
        // count the lines and remove the header
        val lineNumber = lines.count() - 1
        assertEquals(lineNumber, oui.count())
    }
}
