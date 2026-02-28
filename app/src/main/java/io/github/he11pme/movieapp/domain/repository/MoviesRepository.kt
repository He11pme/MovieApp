package io.github.he11pme.movieapp.domain.repository

import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.models.SelectionType
import javax.inject.Singleton

@Singleton
interface MoviesRepository {

    suspend fun getMoviesBySelection(type: SelectionType): Result<List<Movie>>


}