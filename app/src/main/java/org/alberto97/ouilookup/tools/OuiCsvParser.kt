package org.alberto97.ouilookup.tools

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.alberto97.ouilookup.db.Oui

interface IOuiCsvParser {
    fun parse(data: String): List<Oui>
}

class OuiCsvParser(private val reader: CsvReader = csvReader()) : IOuiCsvParser {
    override fun parse(data: String): List<Oui> {
        val entities = reader.readAllWithHeader(data).map {
            Oui(it["Assignment"]!!, it["Organization Name"]!!.trim(), it["Organization Address"]!!)
        }
        return entities
    }
}
