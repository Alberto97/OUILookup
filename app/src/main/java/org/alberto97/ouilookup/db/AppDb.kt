package org.alberto97.ouilookup.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Oui::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ouiDao(): OuiDao
}