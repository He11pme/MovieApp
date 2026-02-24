package io.github.he11pme.movieapp.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "movies_table")
@TypeConverters(Converters::class)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    val overview: String,
    val vote: Double,
    @ColumnInfo(name = "genres") val genreIds: List<Int>,
    @ColumnInfo(name = "is_popular") val isPopular: Boolean,
    @ColumnInfo(name = "is_now_playing") val isNowPlaying: Boolean,
)

class Converters {
    @TypeConverter
    fun listIntToString(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun stringToListInt(str: String): List<Int> {
        return str.split(",").map { it.toInt() }
    }
}