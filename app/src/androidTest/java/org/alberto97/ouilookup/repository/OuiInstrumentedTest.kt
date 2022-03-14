package org.alberto97.ouilookup.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.runBlocking
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.AppDatabase
import org.alberto97.ouilookup.db.Oui
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
        runBlocking { fillDb(ouiDao) }
    }

    private fun setupDao(context: Context): OuiDao {
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        return db.ouiDao()
    }

    private suspend fun fillDb(dao: OuiDao) {
        val list = listOf(
            Oui("000C42", "Routerboard.com", "Pernavas 46 Riga  LV LV-1009 "),
            Oui("00156D", "Ubiquiti Networks Inc.", "495 Montague Expwy. Milpitas CA US 95035 ")
        )
        dao.insert(list)
    }

    @Test
    fun testSearchOuiLong() = runBlocking {
        val result = ouiRepository.search("000C4264")
        assert(result.isNotEmpty())
    }

    @Test
    fun testSearchOuiColon() = runBlocking {
        val result = ouiRepository.search("00:0C:42")
        assert(result.isNotEmpty())
    }

    @Test
    fun testSearchOuiHyphen() = runBlocking {
        val result = ouiRepository.search("00-0C-42")
        assert(result.isNotEmpty())
    }

    @Test
    fun testSearchMultiple() = runBlocking {
        val result = ouiRepository.getMany(listOf("00-0C-42", "00156D"))
        assert(result.count() == 2)
    }
}