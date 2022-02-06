package org.alberto97.ouilookup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.tools.IUpdateManager

@Database(
    entities = [Oui::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ouiDao(): OuiDao
}

class RoomCallback(
    private val repository: dagger.Lazy<IOuiRepository>,
    private val updateManager: dagger.Lazy<IUpdateManager>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.get().updateFromCsv()
            updateManager.get().shouldEnqueueUpdate()
        }
    }
}