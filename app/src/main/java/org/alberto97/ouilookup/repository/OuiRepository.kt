package org.alberto97.ouilookup.repository

import android.content.Context
import androidx.preference.PreferenceManager
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.alberto97.ouilookup.Extensions.readRawTextFile
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.db.OuiDao
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

interface IOuiRepository {
    fun getData(text: String?, type: SearchType?): Flow<List<Oui>>
    fun getByOui(oui: String): Flow<List<Oui>>
    fun getByOrganization(org: String): Flow<List<Oui>>
    fun getAll(): Flow<List<Oui>>
    suspend fun updateIfOldOrEmpty()
}

object SharedPreferenceConstants {
    const val LAST_DB_UPDATE = "last_db_update"
}

enum class SearchType {
    Address,
    Organization
}

@Singleton
class OuiRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reader: CsvReader,
    private val api: IEEEApi,
    private val dao: OuiDao,
) : IOuiRepository {

    override fun getData(text: String?, type: SearchType?): Flow<List<Oui>> {
        if (text.isNullOrEmpty())
            return flow { listOf<List<Oui>>() }

        return if (type == SearchType.Organization)
            getByOrganization(text)
        else
            getByOui(text)
    }

    override fun getByOui(oui: String): Flow<List<Oui>> {
        val saneOui = oui.filterNot { c -> ":-".contains(c)}.take(6)
        val param = saneOui.toUpperCase(Locale.ROOT)
        return dao.getByOui(param)
    }

    override fun getByOrganization(org: String): Flow<List<Oui>> {
        return dao.getByOrganization(org)
    }

    override fun getAll(): Flow<List<Oui>> = dao.getAll()

    @ExperimentalTime
    override suspend fun updateIfOldOrEmpty() {
        val isEmpty = dao.isEmpty()
        val isOffline = true // TODO: Actually check connectivity

        // If offline at first boot use bundled data
        if (isEmpty && isOffline) {
            updateFromCsv()
        }

        if (isOffline)
            return

        if (!isEmpty) {
            // Don't update until at least two weeks has passed since the last data fetch
            val lastUpdateMillis = getLastDbUpdate()
            val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
            if (duration.inDays < 14) return
        }
        updateFromIEEE()
    }

    private fun getLastDbUpdate(): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(SharedPreferenceConstants.LAST_DB_UPDATE, 0)
    }

    private suspend fun updateFromIEEE() {
        val csvData = api.fetchOui()

        saveData(csvData)

        // Save last db update
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        with (prefs.edit()) {
            putLong(SharedPreferenceConstants.LAST_DB_UPDATE, System.currentTimeMillis())
            commit()
        }
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