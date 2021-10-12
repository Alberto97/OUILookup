package org.alberto97.ouilookup.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.alberto97.ouilookup.Extensions.readRawTextFile
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.db.OuiDao
import org.alberto97.ouilookup.tools.IAppConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

interface IOuiRepository {
    fun get(text: String?): Flow<List<Oui>>
    fun getAll(): Flow<List<Oui>>
    fun getLastDbUpdate(): Flow<Long>
    suspend fun dbNeedsUpdate(): Boolean
    suspend fun updateIfOldOrEmpty()
}

@Singleton
class OuiRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reader: CsvReader,
    private val api: IEEEApi,
    private val dao: OuiDao,
    private val connManager: IAppConnectivityManager
) : IOuiRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "oui_settings")
    private val lastDbUpdateKey = longPreferencesKey("last_db_update")

    private fun sanitizeOui(oui: String): String {
        return oui.filterNot { c -> ":-".contains(c)}.take(6)
    }

    override fun get(text: String?): Flow<List<Oui>> {
        if (text.isNullOrEmpty())
            return flowOf(emptyList())

        val ouiText = sanitizeOui(text)
        return dao.get(ouiText, text)
    }

    override fun getAll(): Flow<List<Oui>> = dao.getAll()

    @OptIn(ExperimentalTime::class)
    override suspend fun updateIfOldOrEmpty() {
        val isEmpty = dao.isEmpty()
        val isOffline = !connManager.isConnected()

        // If offline at first boot use bundled data
        if (isEmpty && isOffline) {
            updateFromCsv()
        }

        if (isOffline)
            return

        if (dbNeedsUpdate())
            updateFromIEEE()
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun dbNeedsUpdate(): Boolean {
        return !isDbUpToDate() || dao.isEmpty()
    }

    @ExperimentalTime
    private suspend fun isDbUpToDate() : Boolean {
        // Don't update until at least a month has passed since the last data fetch
        val lastUpdateMillis = getLastDbUpdate().first()
        val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays < 30
    }

    override fun getLastDbUpdate(): Flow<Long> {
        return context.dataStore.data
            .map { settings -> settings[lastDbUpdateKey] ?: 0 }
    }

    private suspend fun setLastDbUpdate() {
        context.dataStore.edit { settings ->
            settings[lastDbUpdateKey] = System.currentTimeMillis()
        }
    }

    private suspend fun updateFromIEEE() {
        val csvData = api.fetchOui()
        saveData(csvData)
        setLastDbUpdate()
    }

    private suspend fun updateFromCsv() {
        val csvData = context.resources.readRawTextFile(R.raw.oui)
        saveData(csvData)
    }

    private suspend fun saveData(csvData: String) {
        // Read CSV data and map it to entity
        val entities = reader.readAllWithHeader(csvData).map {
            Oui(it["Assignment"]!!, it["Organization Name"]!!.trim(), it["Organization Address"]!!)
        }

        // There probably was a mistake, exit before clearing the db.
        // It should not happen but you never know...
        if (entities.count() < 1)
            return

        // Clear OUI table
        dao.deleteAll()

        // Insert all the records
        dao.insert(entities)
    }
}