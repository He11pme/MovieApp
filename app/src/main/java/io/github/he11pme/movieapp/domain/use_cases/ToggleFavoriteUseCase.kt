package io.github.he11pme.movieapp.domain.use_cases

import io.github.he11pme.movieapp.domain.repository.FavoriteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(movieId: Int): Boolean {
        return favoriteRepository.toggleFavorite(movieId)
    }

}