package org.alberto97.ouilookup.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OuiDao {
    @Query("SELECT * FROM oui WHERE oui = :oui")
    fun getByOui(oui: String): Flow<List<Oui>>

    @Query("SELECT * FROM oui WHERE orgName LIKE :org || '%'")
    fun getByOrganization(org: String): Flow<List<Oui>>

    @Query("SELECT * FROM oui")
    fun getAll(): Flow<List<Oui>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bts: List<Oui>)

    @Query("DELETE FROM oui")
    suspend fun deleteAll()

    @Query("SELECT NOT EXISTS(SELECT 1 FROM oui LIMIT 1)")
    fun isEmpty(): Boolean
}