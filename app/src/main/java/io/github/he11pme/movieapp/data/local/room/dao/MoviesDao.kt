package io.github.he11pme.movieapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity
import io.github.he11pme.movieapp.data.local.room.entity.MoviesBySelectionEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert
    suspend fun insertMoviesBySelection(movies: List<MoviesBySelectionEntity>)

    @Transaction
    suspend fun addMovies(selectionEntity: List<MoviesBySelectionEntity>, movieEntity: List<MovieEntity>) {
        insertMovies(movieEntity)
        insertMoviesBySelection(selectionEntity)
    }

    @Query("DELETE FROM movies_by_selection")
    suspend fun clearMovieGenres()

    @Query("DELETE FROM movies_table")
    suspend fun clearMovies()

    @Transaction
    suspend fun clearAll() {
        clearMovieGenres()
        clearMovies()
    }

    @Query(
        """
        SELECT movies_table.* 
        FROM movies_table 
        INNER JOIN movies_by_selection ON movie_id = movies_table.id 
        WHERE genre_id IN (:genres) AND type_selection = :selectionType
        GROUP BY movies_table.id 
        HAVING COUNT(*) >= :genresCount 
        LIMIT 20
        """
    )
    suspend fun getMoviesBySelectionWithGenre(
        selectionType: String,
        genres: List<Int> = emptyList(),
        genresCount: Int = genres.size
    ): List<MovieEntity>

    @Query(
        """
        SELECT DISTINCT movies_table.* 
        FROM movies_table 
        INNER JOIN movies_by_selection ON movie_id = movies_table.id 
        WHERE type_selection = :selectionType
        LIMIT 20
        """
    )
    suspend fun getMoviesBySelection(
        selectionType: String,
    ): List<MovieEntity>


}