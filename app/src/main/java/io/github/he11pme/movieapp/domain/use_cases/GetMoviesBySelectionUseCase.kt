package io.github.he11pme.movieapp.domain.use_cases

import io.github.he11pme.movieapp.domain.models.SelectionType
import io.github.he11pme.movieapp.domain.repository.MoviesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMoviesBySelectionUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(type: SelectionType) = moviesRepository.getMoviesBySelection(type)
}