package io.github.he11pme.movieapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity

@Dao
interface FavoriteMoviesDao {

    @Insert
    suspend fun addFavorite(favorite: FavoriteMovieEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteMovieEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_movies WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavorites(): List<FavoriteMovieEntity>

}