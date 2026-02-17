package io.github.he11pme.movieapp.data.repository.paging_sources

import io.github.he11pme.movieapp.data.network.TMDbApiService

class MoviePopularPagingSource(
    private val api: TMDbApiService
) : MoviePagingSource() {

    override suspend fun getResponse(page: Int) = api.getPopularMovies(page = page)

}