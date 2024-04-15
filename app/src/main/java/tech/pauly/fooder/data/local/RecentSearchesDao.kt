package tech.pauly.fooder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tech.pauly.fooder.data.local.model.RecentSearch
import tech.pauly.fooder.data.local.model.RecentSearchId

@Dao
interface RecentSearchesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(search: RecentSearch)

    @Query(
        "SELECT * FROM recentSearches ORDER BY " +
                "CASE WHEN :byRecent = 1 THEN id END DESC," +
                "CASE WHEN :byRecent = 0 THEN id END ASC"
    )
    fun getAll(byRecent: Boolean = true): Flow<List<RecentSearch>>

    @Query("DELETE FROM recentSearches WHERE id = :id")
    suspend fun delete(id: RecentSearchId)

    @Query("DELETE FROM recentSearches")
    suspend fun deleteAll()
}