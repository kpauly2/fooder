package tech.pauly.fooder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.pauly.fooder.data.local.model.RecentSearch

private const val DB_NAME = "recent_searches"

@Database(
    entities = [(RecentSearch::class)], version = 2,
)
@TypeConverters(RoomConverters::class)
abstract class RecentSearchesDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchesDao

    companion object {
        fun create(context: Context): RecentSearchesDatabase =
            Room.databaseBuilder(
                context,
                RecentSearchesDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration().build()
    }
}