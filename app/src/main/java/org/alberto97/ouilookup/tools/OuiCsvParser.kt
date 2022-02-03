package org.alberto97.ouilookup.tools

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import org.alberto97.ouilookup.db.Oui
import javax.inject.Inject
import javax.inject.Singleton

interface IOuiCsvParser {
    fun parse(data: String): List<Oui>
}

@Singleton
class OuiCsvParser @Inject constructor(private val reader: CsvReader) : IOuiCsvParser {
    override fun parse(data: String): List<Oui> {
        val entities = reader.readAllWithHeader(data).map {
            Oui(it["Assignment"]!!, it["Organization Name"]!!.trim(), it["Organization Address"]!!)
        }
        return entities
    }
}
