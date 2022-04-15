package org.alberto97.ouilookup.tools

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
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
        val entries = oui.count()
        println("Found $entries entries")

        assert(entries > 30000)
    }
}
