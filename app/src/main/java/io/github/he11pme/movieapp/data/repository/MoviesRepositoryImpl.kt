package io.github.he11pme.movieapp.data.repository

import android.icu.util.Calendar
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.models.SelectionType
import io.github.he11pme.movieapp.domain.repository.MoviesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val localRepository: MoviesLocalRepository,
    private val remoteRepository: MoviesRemoteRepository
) : MoviesRepository {

    override suspend fun getMoviesBySelection(type: SelectionType): Result<List<Movie>> {

        val diffTime = TimeHelper.differentTimeMinutes(localRepository.saveTime)
            ?: return getRemoteMovies(type)

        return if (diffTime < TIME_UPDATE_CACHE) getLocalMovies(type)
        else {
            getRemoteMovies(type).let { result ->
                if (result.isFailure) getLocalMovies(type) else result
            }
        }

    }

    private suspend fun getLocalMovies(type: SelectionType): Result<List<Movie>> {
        return localRepository.getMoviesBySelection(type)
    }

    private suspend fun getRemoteMovies(type: SelectionType): Result<List<Movie>> {
        val result = remoteRepository.getMoviesBySelection(type)
        result.getOrNull()?.let {
            localRepository.saveSelectionMovieToCache(type, it)
        }

        return result
    }

    companion object {
        private const val TIME_UPDATE_CACHE = 10
    }

}

class TimeHelper() {

    companion object {
        private inline val currentTime get() = Calendar.getInstance().timeInMillis
        private val differentTime: (Long) -> Long = { currentTime - it }

        /**
         * Calculates the difference between the current time and the specified time in minutes.
         *
         * @return The difference in minutes, or null if the result is negative.
         */
        val differentTimeMinutes: (Long) -> Long? = {
            val diff = differentTime(it)
            if (diff < 0) null else TimeUnit.MILLISECONDS.toMinutes(diff)
        }
    }

}