package io.github.he11pme.movieapp.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity
import io.github.he11pme.movieapp.model.Genre
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.model.MovieDetails
import io.github.he11pme.movieapp.model.Selection
import io.github.he11pme.movieapp.model.SelectionType
import io.github.he11pme.movieapp.data.network.TMDbApi
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class AppRepository @Inject constructor(
    private val collectionsDataSource: MovieCollectionsDataSource,
    private val favoriteMoviesDao: FavoriteMoviesDao
) {

    private val api = TMDbApi.retrofitService
    private var genres: List<Genre>? = null

    suspend fun isFavoriteMovie(id: Int) = favoriteMoviesDao.isFavorite(id)

    /**
     * Toggles the favorite status of a movie with the given [id]
     *
     * If the movie is already in the favorites table, it will be removed.
     * If the movie is not in favorites, it will be added.
     *
     * @return `true` if the movie is now marked as favorite after the call
     *         `false` if the movie was removed from favorites
     */
    suspend fun toggleFavorite(id: Int): Boolean {
        val favorite = FavoriteMovieEntity(id)
        if (favoriteMoviesDao.isFavorite(id)) {
            favoriteMoviesDao.removeFavorite(favorite)
            return false
        }
        else {
            favoriteMoviesDao.addFavorite(favorite)
            return true
        }
    }

    suspend fun getAllFavorites(): List<Result<MovieDetails>> {
        return favoriteMoviesDao.getAllFavorites().map { getMovieById(it.id) }
    }

    suspend fun getGenres(): Result<List<Genre>> {
        return safeApiCall { genres ?: api.getGenres().genres.also { genres = it } }
    }

    fun getCollections(): Result<List<Selection>> {
        return try {
            Result.success(collectionsDataSource.getCollections())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSelectionMovies(type: SelectionType): Result<List<Movie>> {
        return safeApiCall {
            when (type) {
                SelectionType.NowPlaying -> getNowPlayingMovies()
                SelectionType.Popular -> getPopularMovies()
                is SelectionType.OfGenres -> getMoviesByGenres(type.genres)
            }
        }
    }

    suspend fun getMovieById(movieId: Int): Result<MovieDetails> {
        return safeApiCall { api.getMovieById(movieId).apply { isFavorite = isFavoriteMovie(id) } }
    }

    private suspend fun getMoviesByGenres(genres: List<Genre>): List<Movie> {
        return api.getMoviesByGenres(genres.map { it.id }.joinToString(",")).movies
    }

    private suspend fun getPopularMovies() = api.getPopularMovies().movies
    private suspend fun getNowPlayingMovies() = api.getNowPlayingMovies().movies

    private suspend fun <T> safeApiCall(onSuccess: suspend () -> T): Result<T> {
        return try {
            Result.success(onSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}