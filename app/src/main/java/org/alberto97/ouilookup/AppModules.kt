package org.alberto97.ouilookup

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.alberto97.ouilookup.datasource.IEEEApi
import org.alberto97.ouilookup.db.AppDatabase
import org.alberto97.ouilookup.repository.*
import org.alberto97.ouilookup.tools.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideOuiRepository(repository: OuiRepository): IOuiRepository

    @Binds
    abstract fun provideConnManager(connManager: AppConnectivityManager): IAppConnectivityManager

    @Binds
    abstract fun provideSettings(settings: SettingsRepository): ISettingsRepository

    @Binds
    abstract fun provideParser(parser: OuiCsvParser): IOuiCsvParser

    @Binds
    abstract fun provideUpdateManager(updateManager: UpdateManager): IUpdateManager

    @Binds
    abstract fun provideAppStoreUtils(appStoreUtils: AppStoreUtils): IAppStoreUtils

    @Binds
    abstract fun provideFeedbackRepository(feedbackManager: FeedbackRepository): IFeedbackRepository

    @Binds
    abstract fun provideFeedbackManager(feedbackManager: FeedbackManager): IFeedbackManager
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
            .build()

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.ouiDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://standards-oui.ieee.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    @Provides
    fun provideIEEEApi(retrofit: Retrofit): IEEEApi =
        retrofit.create(IEEEApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object CsvReaderModule {
    @Singleton
    @Provides
    fun provideCsvReader() = csvReader()
}

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {
    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}
