package io.github.he11pme.movieapp.domain.repository

import androidx.paging.PagingData
import io.github.he11pme.movieapp.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun findMovie(title: String): Flow<PagingData<Movie>>

    fun getPopularMoviesPaging(): Flow<PagingData<Movie>>

}