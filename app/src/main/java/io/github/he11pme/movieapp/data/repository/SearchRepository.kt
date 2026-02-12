package io.github.he11pme.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.data.network.TMDbApi
import io.github.he11pme.movieapp.data.repository.paging_sources.MoviePopularPagingSource
import io.github.he11pme.movieapp.data.repository.paging_sources.MovieSearchPagingSource
import io.github.he11pme.movieapp.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor() {
    private val api = TMDbApi.retrofitService
    fun findMovie(title: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = TMDB_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieSearchPagingSource(title, api) }
        ).flow
    }

    fun getPopularMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = TMDB_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePopularPagingSource(api) }
        ).flow
    }

    companion object {
        const val TMDB_PAGE_SIZE = 20
    }
}