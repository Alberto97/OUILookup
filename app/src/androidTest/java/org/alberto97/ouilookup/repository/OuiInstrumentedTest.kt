package org.alberto97.ouilookup.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.runBlocking
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.AppDatabase
import org.alberto97.ouilookup.db.OuiDao
import org.alberto97.ouilookup.tools.OuiCsvParser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class OuiInstrumentedTest {

    private lateinit var ouiRepository: OuiRepository

    @Mock
    private lateinit var ieeeMock: IEEEApi

    @Mock
    private lateinit var settings: ISettingsRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val ouiDao = setupDao(context)
        val csvParser = OuiCsvParser(csvReader())
        ouiRepository = OuiRepository(context, csvParser, ieeeMock, ouiDao, settings)
    }

    private fun setupDao(context: Context): OuiDao {
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        return db.ouiDao()
    }

    private suspend fun fillDb() {
        ouiRepository.updateFromCsv()
    }

    @Test
    fun testSearchOuiLong() = runBlocking {
        fillDb()

        val result = ouiRepository.get("000C4264")
        assert(result.isNotEmpty())
    }

    @Test
    fun testSearchOuiColon() = runBlocking {
        fillDb()

        val result = ouiRepository.get("00:0C:42")
        assert(result.isNotEmpty())
    }

    @Test
    fun testSearchOuiHyphen() = runBlocking {
        fillDb()

        val result = ouiRepository.get("00-0C-42")
        assert(result.isNotEmpty())
    }
}