package io.github.he11pme.movieapp.data.repository

import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.data.network.TMDbApi
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.repository.MovieDetailRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailRepositoryImpl @Inject constructor() : MovieDetailRepository {
    private val api = TMDbApi.retrofitService

    override suspend fun getMovieById(movieId: Int, isFavorite: Boolean): Result<MovieDetails> {
        return safeApiCall { api.getMovieById(movieId).toDomain(isFavorite) }
    }

    private suspend fun <T> safeApiCall(onSuccess: suspend () -> T): Result<T> {
        return try {
            Result.success(onSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}