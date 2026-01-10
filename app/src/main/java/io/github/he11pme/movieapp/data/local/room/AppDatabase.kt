package io.github.he11pme.movieapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val favoriteMoviesDao: FavoriteMoviesDao

}