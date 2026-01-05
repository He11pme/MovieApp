package io.github.he11pme.movieapp.repository

import io.github.he11pme.movieapp.model.Genre
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.model.MovieDetails
import io.github.he11pme.movieapp.model.Selection
import io.github.he11pme.movieapp.model.SelectionType
import io.github.he11pme.movieapp.network.TMDbApi
import java.lang.Exception

class AppRepository(
    private val collectionsDataSource: MovieCollectionsDataSource
) {

    private val api = TMDbApi.retrofitService
    private var genres: List<Genre>? = null

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
        return safeApiCall { api.getMovieById(movieId) }
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