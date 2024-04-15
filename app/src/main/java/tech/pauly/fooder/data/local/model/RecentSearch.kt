package tech.pauly.fooder.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recentSearches")
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: RecentSearchId = RecentSearchId(0),
    @ColumnInfo(name = "search_query") val searchQuery: String
)


@JvmInline
value class RecentSearchId(val id: Int)