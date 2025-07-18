package org.alberto97.ouilookup.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface IFeedbackRepository {
    fun getLastRequestInstant(): Flow<Long>
    suspend fun setLastRequestInstant(value: Long)
}

@Singleton
class FeedbackRepository @Inject constructor(private val app: Application) : IFeedbackRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "feedback_persist")
    private val lastRequestDate = longPreferencesKey("last_request_date")

    override fun getLastRequestInstant(): Flow<Long> {
        return app.dataStore.data
            .map { settings -> settings[lastRequestDate] ?: 0L }
    }

    override suspend fun setLastRequestInstant(value: Long) {
        app.dataStore.edit { settings ->
            settings[lastRequestDate] = value
        }
    }
}
