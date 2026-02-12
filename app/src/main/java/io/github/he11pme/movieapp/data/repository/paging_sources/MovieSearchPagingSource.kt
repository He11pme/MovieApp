package io.github.he11pme.movieapp.data.repository.paging_sources

import io.github.he11pme.movieapp.data.network.TMDbApiService
import io.github.he11pme.movieapp.data.network.dto.MovieResponse

class MovieSearchPagingSource(
    private val title: String,
    private val api: TMDbApiService
) : MoviePagingSource() {

    override suspend fun getResponse(page: Int): MovieResponse {
        return api.findMovieByTitle(
            title = title,
            page = page
        )
    }

}