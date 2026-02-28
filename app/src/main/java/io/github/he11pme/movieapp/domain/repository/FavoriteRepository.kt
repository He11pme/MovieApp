package io.github.he11pme.movieapp.domain.repository

import io.github.he11pme.movieapp.domain.models.MovieDetails
import javax.inject.Singleton

@Singleton
interface FavoriteRepository {

    suspend fun isFavoriteMovie(id: Int): Boolean
    suspend fun getAllFavorites(): List<Result<MovieDetails>>

    suspend fun removeFavoriteById(movieId: Int)

    /**
     * Toggles the favorite status of a movie with the given [id]
     *
     * If the movie is already in the favorites table, it will be removed.
     * If the movie is not in favorites, it will be added.
     *
     * @return `true` if the movie is now marked as favorite after the call
     *         `false` if the movie was removed from favorites
     */
    suspend fun toggleFavorite(id: Int): Boolean

}