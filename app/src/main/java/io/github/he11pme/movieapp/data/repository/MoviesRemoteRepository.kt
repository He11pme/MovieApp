package io.github.he11pme.movieapp.data.repository

import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.data.network.TMDbApi
import io.github.he11pme.movieapp.data.network.dto.MovieDTO
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.models.SelectionType
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRemoteRepository @Inject constructor() {
    private val api = TMDbApi.retrofitService
    suspend fun getMoviesBySelection(type: SelectionType): Result<List<Movie>> {
        return safeApiCall {
            when (type) {
                SelectionType.NowPlaying -> getNowPlayingMovies()
                SelectionType.Popular -> getPopularMovies()
                is SelectionType.OfGenres -> getMoviesByGenres(type.genresIds)
            }.map { it.toDomain() }
        }
    }

    private suspend fun getMoviesByGenres(genres: List<Int>): List<MovieDTO> {
        return api.getMoviesByGenres(genres.joinToString(",")).movies
    }

    private suspend fun getPopularMovies(): List<MovieDTO> = api.getPopularMovies().movies

    private suspend fun getNowPlayingMovies(): List<MovieDTO> = api.getNowPlayingMovies().movies

    private suspend fun <T> safeApiCall(onSuccess: suspend () -> T): Result<T> {
        return try {
            Result.success(onSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}