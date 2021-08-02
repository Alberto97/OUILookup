package org.alberto97.ouilookup.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
import android.net.NetworkCapabilities

import android.os.Build
import androidx.annotation.RequiresApi


interface IOuiRepository {
    fun getData(text: String?, type: SearchType?): Flow<List<Oui>>
    fun getByOui(oui: String): Flow<List<Oui>>
    fun getByOrganization(org: String): Flow<List<Oui>>
    fun getAll(): Flow<List<Oui>>
    fun getLastDbUpdate(): Long
    fun dbNeedsUpdate(): Boolean
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
            return flowOf(emptyList())

        return if (type == SearchType.Organization)
            getByOrganization(text)
        else
            getByOui(text)
    }

    override fun getByOui(oui: String): Flow<List<Oui>> {
        val saneOui = oui.filterNot { c -> ":-".contains(c)}.take(6)
        val param = saneOui.uppercase()
        return dao.getByOui(param)
    }

    override fun getByOrganization(org: String): Flow<List<Oui>> {
        return dao.getByOrganization(org)
    }

    override fun getAll(): Flow<List<Oui>> = dao.getAll()

    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            isConnected(cm)
        else
            isConnectedLegacy(cm)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnected(cm: ConnectivityManager): Boolean {
        val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Suppress("DEPRECATION")
    private fun isConnectedLegacy(cm: ConnectivityManager): Boolean {
        val networks = cm.allNetworks
        for (n in networks) {
            val nInfo = cm.getNetworkInfo(n)
            if (nInfo != null && nInfo.isConnected) return true
        }

        return false
    }

    // TODO: In the future the db will be with bundled data (csv) seeded during the splash screen [if the db is empty]
    //  and then in a later stage updated from IEEE in background if there is an active internet connection
    @ExperimentalTime
    override suspend fun updateIfOldOrEmpty() {
        val isEmpty = dao.isEmpty()
        val isOffline = !isConnected()

        // If offline at first boot use bundled data
        if (isEmpty && isOffline) {
            updateFromCsv()
        }

        if (isOffline)
            return

        if (dbNeedsUpdate())
            updateFromIEEE()
    }

    @ExperimentalTime
    override fun dbNeedsUpdate(): Boolean {
        return !isDbUpToDate() || dao.isEmpty()
    }

    @ExperimentalTime
    private fun isDbUpToDate() : Boolean {
        // Don't update until at least two weeks has passed since the last data fetch
        val lastUpdateMillis = getLastDbUpdate()
        val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays < 14
    }

    override fun getLastDbUpdate(): Long {
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