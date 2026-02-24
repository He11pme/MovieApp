package io.github.he11pme.movieapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import io.github.he11pme.movieapp.data.local.room.dao.MoviesDao
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity
import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity

@Database(
    entities = [FavoriteMovieEntity::class, MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val favoriteMoviesDao: FavoriteMoviesDao
    abstract val moviesDao: MoviesDao

}