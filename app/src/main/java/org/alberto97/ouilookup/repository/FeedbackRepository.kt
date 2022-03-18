package org.alberto97.ouilookup.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface IFeedbackRepository {
    fun getLastRequestInstant(): Flow<Long>
    suspend fun setLastRequestInstant(value: Long)
}

@Singleton
class FeedbackRepository @Inject constructor(@ApplicationContext private val context: Context,) : IFeedbackRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "feedback_persist")
    private val lastRequestDate = longPreferencesKey("last_request_date")

    override fun getLastRequestInstant(): Flow<Long> {
        return context.dataStore.data
            .map { settings -> settings[lastRequestDate] ?: 0L }
    }

    override suspend fun setLastRequestInstant(value: Long) {
        context.dataStore.edit { settings ->
            settings[lastRequestDate] = value
        }
    }
}
