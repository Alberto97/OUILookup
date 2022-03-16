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
    fun getShowAgain(): Flow<Boolean>
    suspend fun setShowAgain(value: Boolean)
}

@Singleton
class FeedbackRepository @Inject constructor(@ApplicationContext private val context: Context,) : IFeedbackRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "feedback_persist")
    private val lastRequestDate = longPreferencesKey("last_request_date")
    private val showAgain = booleanPreferencesKey("show_again")

    override fun getLastRequestInstant(): Flow<Long> {
        return context.dataStore.data
            .map { settings -> settings[lastRequestDate] ?: 0L }
    }

    override suspend fun setLastRequestInstant(value: Long) {
        context.dataStore.edit { settings ->
            settings[lastRequestDate] = value
        }
    }

    override fun getShowAgain(): Flow<Boolean> {
        return context.dataStore.data
            .map { settings -> settings[showAgain] ?: true }
    }

    override suspend fun setShowAgain(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[showAgain] = value
        }
    }
}
