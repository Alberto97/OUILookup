package org.alberto97.ouilookup

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.work.WorkManager
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.AppDatabase
import org.alberto97.ouilookup.db.OuiDao
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainApplication : Application() {
    companion object {
        private lateinit var _instance: MainApplication
        val instance get() = _instance
    }

    private lateinit var _ouiDao: OuiDao
    val ouiDao: OuiDao get() = _ouiDao

    private lateinit var _ieeeApi: IEEEApi
    val ieeeApi get() = _ieeeApi

    private lateinit var _workManager: WorkManager
    val workManager get() = _workManager

    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "oui_settings")

    override fun onCreate() {
        super.onCreate()
        _instance = this

        val appDatabase = initializeDatabase(this)
        _ouiDao = appDatabase.ouiDao()

        val retrofit = initializeRetrofit()
        _ieeeApi = retrofit.create(IEEEApi::class.java)

        _workManager = WorkManager.getInstance(this)
    }

    private fun initializeDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
            .build()
    }

    private fun initializeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://standards-oui.ieee.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }
}