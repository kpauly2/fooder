package tech.pauly.fooder.data.local

import androidx.room.TypeConverter
import tech.pauly.fooder.data.local.model.RecentSearchId

class RoomConverters {

    @TypeConverter
    fun fromRecentSearchId(value: RecentSearchId?): Int? {
        return value?.id
    }

    @TypeConverter
    fun toRecentSearchId(id: Int?): RecentSearchId? {
        return id?.let { RecentSearchId(it) }
    }
}
