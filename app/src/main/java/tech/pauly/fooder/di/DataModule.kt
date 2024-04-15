package tech.pauly.fooder.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tech.pauly.fooder.data.local.RecentSearchesDao
import tech.pauly.fooder.data.local.RecentSearchesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun providesRecentSearchesDatabase(@ApplicationContext context: Context): RecentSearchesDatabase =
        RecentSearchesDatabase.create(context)

    @Provides
    @Singleton
    fun providesRecentSearchesDao(database: RecentSearchesDatabase): RecentSearchesDao =
        database.recentSearchDao()

}