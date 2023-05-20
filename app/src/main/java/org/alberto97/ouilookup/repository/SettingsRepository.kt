package org.alberto97.ouilookup.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface ISettingsRepository {
    fun getLastDbUpdate(): Flow<Long>
    suspend fun setLastDbUpdate(value: Long)
    fun getFirstLaunchInstant(): Flow<Long>
    suspend fun setFirstLaunchInstant(value: Long)
    fun getUseDynamicTheme(): Flow<Boolean>
    suspend fun setUseDynamicTheme(value: Boolean)
}

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
): ISettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "oui_settings")
    private val lastDbUpdateKey = longPreferencesKey("last_db_update")
    private val firstLaunchInstant = longPreferencesKey("first_launch_instant")
    private val useDynamicTheme = booleanPreferencesKey("use_dynamic_theme")

    override fun getLastDbUpdate(): Flow<Long> {
        return context.dataStore.data
            .map { settings -> settings[lastDbUpdateKey] ?: 0 }
    }

    override suspend fun setLastDbUpdate(value: Long) {
        context.dataStore.edit { settings ->
            settings[lastDbUpdateKey] = value
        }
    }

    override fun getFirstLaunchInstant(): Flow<Long> {
        return context.dataStore.data
            .map { settings -> settings[firstLaunchInstant] ?: 0 }
    }

    override suspend fun setFirstLaunchInstant(value: Long) {
        context.dataStore.edit { settings ->
            settings[firstLaunchInstant] = value
        }
    }

    override fun getUseDynamicTheme(): Flow<Boolean> {
        return context.dataStore.data
            .map { settings -> settings[useDynamicTheme] ?: false }
    }

    override suspend fun setUseDynamicTheme(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[useDynamicTheme] = value
        }
    }
}
