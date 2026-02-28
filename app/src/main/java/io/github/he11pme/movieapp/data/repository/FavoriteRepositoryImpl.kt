package io.github.he11pme.movieapp.data.repository

import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import io.github.he11pme.movieapp.data.local.room.entity.FavoriteMovieEntity
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.repository.FavoriteRepository
import io.github.he11pme.movieapp.domain.repository.MovieDetailRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteMoviesDao: FavoriteMoviesDao,
    private val movieDetailRepository: MovieDetailRepository
) : FavoriteRepository {

    override suspend fun isFavoriteMovie(id: Int) = favoriteMoviesDao.isFavorite(id)

    /**
     * Toggles the favorite status of a movie with the given [id]
     *
     * If the movie is already in the favorites table, it will be removed.
     * If the movie is not in favorites, it will be added.
     *
     * @return `true` if the movie is now marked as favorite after the call
     *         `false` if the movie was removed from favorites
     */
    override suspend fun toggleFavorite(id: Int): Boolean {
        val favorite = FavoriteMovieEntity(id)
        if (favoriteMoviesDao.isFavorite(id)) {
            favoriteMoviesDao.removeFavorite(favorite)
            return false
        } else {
            favoriteMoviesDao.addFavorite(favorite)
            return true
        }
    }

    override suspend fun getAllFavorites(): List<Result<MovieDetails>> {
        return favoriteMoviesDao.getAllFavorites()
            .map { movieDetailRepository.getMovieById(it.id, true) }
    }

    override suspend fun removeFavoriteById(movieId: Int) {
        favoriteMoviesDao.removeFavorite(FavoriteMovieEntity(movieId))
    }

}