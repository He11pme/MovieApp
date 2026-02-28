package io.github.he11pme.movieapp.domain.repository

import io.github.he11pme.movieapp.domain.models.MovieDetails
import javax.inject.Singleton

@Singleton
interface MovieDetailRepository {

    suspend fun getMovieById(movieId: Int, isFavorite: Boolean): Result<MovieDetails>

}