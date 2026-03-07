package io.github.he11pme.movieapp.domain.use_cases

import io.github.he11pme.movieapp.domain.models.DownloadMovie
import io.github.he11pme.movieapp.domain.repository.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadMovieUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(downloadMovie: DownloadMovie, isLegacy: Boolean = false): Result<Unit> {

        return if (isLegacy) storageRepository.saveMovieLegacyStorage(downloadMovie)
        else storageRepository.saveMovieScopedStorage(downloadMovie)

    }

}