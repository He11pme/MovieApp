package io.github.he11pme.movieapp.domain.repository

import io.github.he11pme.movieapp.domain.models.DownloadMovie
import javax.inject.Singleton

@Singleton
interface StorageRepository {

    suspend fun saveMovieScopedStorage(movie: DownloadMovie): Result<Unit>
    suspend fun saveMovieLegacyStorage(movie: DownloadMovie): Result<Unit>

}