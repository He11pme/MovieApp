package io.github.he11pme.movieapp.data.repository.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.data.network.dto.MovieResponse
import io.github.he11pme.movieapp.domain.models.Movie

/**
 * Base implementation of [PagingSource] for paginated movie loading.
 *
 * Encapsulates common pagination logic:
 * - determining the current page
 * - handling prevKey / nextKey
 * - mapping DTOs to domain models
 * - error handling
 *
 * Subclasses must implement the [getResponse] method,
 * which performs the specific network request and returns a [MovieResponse].
 *
 * Used together with Paging3 to load data page by page.
 */
abstract class MoviePagingSource: PagingSource<Int, Movie>() {

    /**
     * Performs a network request for the specified page.
     *
     * @param page The page index to be loaded.
     * @return [MovieResponse] containing a list of movies and pagination metadata.
     *
     * Example:
     * ```
     * override suspend fun getResponse(page: Int) =
     *     api.getPopularMovies(page)
     * ```
     */
    abstract suspend fun getResponse(page: Int): MovieResponse

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: INITIAL_PAGE

        return try {
            val response = getResponse(page)
            LoadResult.Page(
                data = response.movies.map { it.toDomain() },
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
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

    companion object {
        const val INITIAL_PAGE = 1
    }

}