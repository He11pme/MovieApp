package io.github.he11pme.movieapp.data.repository

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity
import io.github.he11pme.movieapp.data.local.room.dao.MoviesDao
import io.github.he11pme.movieapp.data.network.dto.GenreDTO
import io.github.he11pme.movieapp.data.network.dto.MovieDTO
import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.data.mappers.toDto
import io.github.he11pme.movieapp.data.mappers.toEntity
import io.github.he11pme.movieapp.data.network.TMDbApi
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.models.Selection
import io.github.he11pme.movieapp.domain.models.SelectionType
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class AppRepository @Inject constructor(
    private val collectionsDataSource: MovieCollectionsDataSource,
    private val favoriteMoviesDao: FavoriteMoviesDao,
    private val localMoviesDao: MoviesDao
) {

    private val api = TMDbApi.retrofitService
    private var genres: List<GenreDTO>? = null

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
        } else {
            favoriteMoviesDao.addFavorite(favorite)
            return true
        }
    }

    suspend fun getAllFavorites(): List<Result<MovieDetails>> {
        return favoriteMoviesDao.getAllFavorites().map { getMovieById(it.id) }
    }

    suspend fun removeFavoriteById(movieId: Int) {
        favoriteMoviesDao.removeFavorite(FavoriteMovieEntity(movieId))
    }

    suspend fun getGenres(): Result<List<GenreDTO>> {
        return safeApiCall { genres ?: api.getGenres().genres.also { genres = it } }
    }

    fun getCollections(): Result<List<Selection>> {
        return try {
            Result.success(collectionsDataSource.getCollections().map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSelectionMovies(
        type: SelectionType,
        fromCache: Boolean = false
    ): Result<List<Movie>> {
        val result = safeApiCall {
            when (type) {
                SelectionType.NowPlaying -> getNowPlayingMovies(fromCache)
                SelectionType.Popular -> getPopularMovies(fromCache)
                is SelectionType.OfGenres -> getMoviesByGenres(type.genresIds, fromCache)
            }.map { it.toDomain() }
        }
        return if (result.isSuccess || fromCache) result else getSelectionMovies(type, true)
    }

    suspend fun getMovieById(movieId: Int): Result<MovieDetails> {
        return safeApiCall { api.getMovieById(movieId).toDomain(isFavoriteMovie(movieId)) }
    }

    private suspend fun getMoviesByGenres(
        genres: List<Int>,
        fromCache: Boolean = false
    ): List<MovieDTO> {
        return if (!fromCache) getMoviesAndSaveToCache { api.getMoviesByGenres(genres.joinToString(",")).movies }
        else localMoviesDao.getMoviesByGenres(genres).map { it.toDto() }
    }

    private suspend fun getPopularMovies(fromCache: Boolean = false): List<MovieDTO> {
        return if (!fromCache) getMoviesAndSaveToCache(isPopular = true) { api.getPopularMovies().movies }
        else localMoviesDao.getPopularMovies().map { it.toDto() }
    }


    private suspend fun getNowPlayingMovies(fromCache: Boolean = false): List<MovieDTO> {
        return if (!fromCache) getMoviesAndSaveToCache(isNowPlaying = true) { api.getNowPlayingMovies().movies }
        else localMoviesDao.getNowPlayingMovies().map { it.toDto() }
    }

    private suspend fun saveToCache(
        movies: List<MovieDTO>,
        isPopular: Boolean = false,
        isNowPlaying: Boolean = false
    ) {
        if (isPopular) localMoviesDao.removePopularMovies()
        if (isNowPlaying) localMoviesDao.removeNowPlayingMovies()

        localMoviesDao.addMovies(movies.map { it.toEntity(isPopular, isNowPlaying) })
    }

    private suspend fun getMoviesAndSaveToCache(
        isPopular: Boolean = false,
        isNowPlaying: Boolean = false,
        getMovies: suspend () -> List<MovieDTO>
    ): List<MovieDTO> {
        return getMovies().also {
            try {
                saveToCache(it, isPopular, isNowPlaying)
            } catch (e: Exception) {
                Log.e("SAVE TO CACHE", e.message.toString())
            }
        }
    }

    private suspend fun <T> safeApiCall(onSuccess: suspend () -> T): Result<T> {
        return try {
            Result.success(onSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}