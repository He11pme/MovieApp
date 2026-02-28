package io.github.he11pme.movieapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import io.github.he11pme.movieapp.data.local.room.dao.MoviesDao
import io.github.he11pme.movieapp.data.local.room.entity.Converters
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity
import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity
import io.github.he11pme.movieapp.data.local.room.entity.MoviesBySelectionEntity

@Database(
    entities = [FavoriteMovieEntity::class, MovieEntity::class, MoviesBySelectionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val favoriteMoviesDao: FavoriteMoviesDao
    abstract val moviesDao: MoviesDao
}