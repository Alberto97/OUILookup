package org.alberto97.ouilookup.tools

import com.jsoizo.kotlincsv.reader.CsvReader
import com.jsoizo.kotlincsv.reader.withHeader
import org.alberto97.ouilookup.db.Oui
import javax.inject.Inject
import javax.inject.Singleton

interface IOuiCsvParser {
    fun parse(data: String): List<Oui>
}

@Singleton
class OuiCsvParser @Inject constructor(private val reader: CsvReader) : IOuiCsvParser {
    override fun parse(data: String): List<Oui> {
        val entities = reader.read(data.asSequence()).withHeader().map {
            Oui(it["Assignment"]!!, it["Organization Name"]!!.trim(), it["Organization Address"]!!)
        }
        return entities.toList()
    }
}
