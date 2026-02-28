package io.github.he11pme.movieapp.data.repository

import io.github.he11pme.movieapp.data.local.room.dao.MoviesDao
import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.data.mappers.toEntity
import io.github.he11pme.movieapp.data.mappers.toSelectionEntity
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.models.SelectionType
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository
import java.lang.Exception
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesLocalRepository @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val localMoviesDao: MoviesDao,
) {
    val saveTime get() = preferenceRepository.getDefaultLastLoadTimeToCache()
    private var cacheIsNotClear = true

    suspend fun getMoviesBySelection(type: SelectionType): Result<List<Movie>> {
        return safeApiCall {
            when (type) {
                SelectionType.NowPlaying -> localMoviesDao.getMoviesBySelection(type.name)
                SelectionType.Popular -> localMoviesDao.getMoviesBySelection(type.name)
                is SelectionType.OfGenres -> localMoviesDao.getMoviesBySelectionWithGenre(
                    type.name,
                    type.genresIds
                )
            }.map { it.toDomain() }
        }
    }

    suspend fun saveSelectionMovieToCache(type: SelectionType, movies: List<Movie>) {
        if (cacheIsNotClear) clearCache()

        localMoviesDao.addMovies(
            selectionEntity = movies.flatMap { it.toSelectionEntity(type.name) },
            movieEntity = movies.map { it.toEntity() }
        )

        preferenceRepository.saveLastLoadTimeToCache(Calendar.getInstance().timeInMillis)
    }

    private suspend fun clearCache() {
        localMoviesDao.clearAll()
        cacheIsNotClear = false
    }

    private suspend fun <T> safeApiCall(onSuccess: suspend () -> T): Result<T> {
        return try {
            Result.success(onSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}