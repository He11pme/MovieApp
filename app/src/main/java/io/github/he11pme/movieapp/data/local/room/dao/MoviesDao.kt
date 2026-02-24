package io.github.he11pme.movieapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies_table WHERE id = :id")
    suspend fun removeMovie(id: Int)

    @Query("DELETE FROM movies_table WHERE is_popular = 1")
    suspend fun removePopularMovies()

    @Query("DELETE FROM movies_table WHERE is_now_playing = 1")
    suspend fun removeNowPlayingMovies()

    @Query("SELECT * FROM movies_table")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies_table WHERE is_popular = 1")
    suspend fun getPopularMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies_table WHERE is_now_playing = 1")
    suspend fun getNowPlayingMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies_table WHERE genres = :genres")
    suspend fun getMoviesByGenres(genres: List<Int>): List<MovieEntity>

}