package org.alberto97.ouilookup

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.AppDatabase
import org.alberto97.ouilookup.db.OuiDao
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.repository.OuiRepository
import org.alberto97.ouilookup.tools.IAppConnectivityManager
import org.alberto97.ouilookup.tools.OuiCsvParser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.time.ExperimentalTime


class ApiMock : IEEEApi {
    override suspend fun fetchOui(): String {
        return ""
    }
}

@RunWith(MockitoJUnitRunner::class)
class ExampleInstrumentedTest {

    private lateinit var ouiRepository: OuiRepository

    @Mock
    private lateinit var connManager: IAppConnectivityManager

    @Mock
    private lateinit var settings: ISettingsRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val ouiDao = setupDao(context)
        val csvParser = OuiCsvParser(csvReader())
        ouiRepository = OuiRepository(context, csvParser, ApiMock(), ouiDao, connManager, settings)
    }

    private fun setupDao(context: Context): OuiDao {
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        return db.ouiDao()
    }

    private suspend fun fillDb() {
        // Make sure we use the bundled data
        `when`(connManager.isConnected()).thenReturn(false)

        ouiRepository.updateIfOldOrEmpty()
    }

    @ExperimentalTime
    @Test
    fun testNeedsUpdate() {
        val time = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()
        `when`(settings.getLastDbUpdate()).thenReturn(flowOf(time))

        runBlocking {
            val result = ouiRepository.isDbUpToDate()
            assert(!result)
        }
    }

    @ExperimentalTime
    @Test
    fun testUpToDate() {
        val time = LocalDateTime.now().minusDays(29).toInstant(ZoneOffset.UTC).toEpochMilli()
        `when`(settings.getLastDbUpdate()).thenReturn(flowOf(time))

        runBlocking {
            val result = ouiRepository.isDbUpToDate()
            assert(result)
        }
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