package org.alberto97.ouilookup.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Headers
import org.alberto97.ouilookup.Extensions.readRawTextFile
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.db.OuiDao
import org.alberto97.ouilookup.tools.IOuiCsvParser
import org.alberto97.ouilookup.tools.OctetTool
import org.alberto97.ouilookup.tools.Rfc1123DateTime
import javax.inject.Inject
import javax.inject.Singleton


interface IOuiRepository {
    suspend fun get(text: String): List<Oui>
    fun getLastDbUpdate(): Flow<Long>
    suspend fun updateFromIEEE()
    suspend fun updateFromCsv()
}

@Singleton
class OuiRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ouiCsvParser: IOuiCsvParser,
    private val api: IEEEApi,
    private val dao: OuiDao,
    private val settings: ISettingsRepository
) : IOuiRepository {

    override suspend fun get(text: String): List<Oui> {
        val ouiText = OctetTool.sanitizeOui(text)
        return dao.get(ouiText, text)
    }

    override fun getLastDbUpdate(): Flow<Long> {
        return settings.getLastDbUpdate()
    }

    private fun getLastModifiedMillis(headers: Headers): Long? {
        val lastModified = headers["Last-Modified"] ?: return null
        return Rfc1123DateTime.parseMillis(lastModified)
    }

    override suspend fun updateFromIEEE() = withContext(Dispatchers.IO) {
        val csvData = api.fetchOui()
        val body = csvData.body() ?: return@withContext
        val lastModified = getLastModifiedMillis(csvData.headers()) ?: System.currentTimeMillis()
        saveData(body)
        settings.setLastDbUpdate(lastModified)
    }

    override suspend fun updateFromCsv() {
        val csvData = context.resources.readRawTextFile(R.raw.oui)
        val csvMillis = context.resources.readRawTextFile(R.raw.oui_date_millis).toLong()
        saveData(csvData)
        settings.setLastDbUpdate(csvMillis)
    }

    private suspend fun saveData(csvData: String) = withContext(Dispatchers.Default) {
        // Read CSV data and map it to entity
        val entities = ouiCsvParser.parse(csvData)

        // There probably was a mistake, exit before clearing the db.
        // It should not happen but you never know...
        if (entities.count() < 1)
            return@withContext

        // Clear OUI table
        dao.deleteAll()

        // Insert all the records
        dao.insert(entities)
    }
}