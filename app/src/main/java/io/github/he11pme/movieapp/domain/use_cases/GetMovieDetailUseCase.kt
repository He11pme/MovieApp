package io.github.he11pme.movieapp.domain.use_cases

import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.repository.FavoriteRepository
import io.github.he11pme.movieapp.domain.repository.MovieDetailRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMovieDetailUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val movieDetailRepository: MovieDetailRepository,
) {

    suspend operator fun invoke(movieId: Int): Result<MovieDetails> {
        val isFavorite = favoriteRepository.isFavoriteMovie(movieId)
        return movieDetailRepository.getMovieById(movieId, isFavorite)
    }

}