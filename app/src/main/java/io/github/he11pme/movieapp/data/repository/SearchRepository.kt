package io.github.he11pme.movieapp.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.data.network.TMDbApi
import io.github.he11pme.movieapp.data.network.dto.Movie
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor() {
    private val api = TMDbApi.retrofitService
    suspend fun findMovie(
        title: String,
        afterVote: Float = 0f
    ): Result<List<Movie>> {

        return safeApiCall {
            val movies: MutableList<Movie> = mutableListOf()
            var totalPages: Int = -1
            var currentPage = 1

            do {
                val response = api.findMovieByTitle(title = title, page = currentPage)
                if (totalPages < 0) totalPages = response.totalPage

                movies.addAll(response.movies.filter { it.vote >= afterVote })

                currentPage += 1
            } while (currentPage < totalPages && movies.size < 12)

            movies.sortedByDescending { it.vote }.take(12)
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