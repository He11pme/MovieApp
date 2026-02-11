package io.github.he11pme.movieapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.data.network.TMDbApiService
import io.github.he11pme.movieapp.domain.models.Movie

class MovieSearchPagingSource(
    private val title: String,
    private val api: TMDbApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1

        return try {
            val response = api.findMovieByTitle(
                title = title,
                page = page
            )
            LoadResult.Page(
                data = response.movies.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPage) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}