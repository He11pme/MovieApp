package io.github.he11pme.movieapp.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movies_by_selection")
data class MoviesBySelectionEntity(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "genre_id") val genreId: Int,
    @ColumnInfo(name = "type_selection") val typeSelection: String
)
