package io.github.he11pme.movieapp.data.repository

import io.github.he11pme.movieapp.data.mappers.toDomain
import io.github.he11pme.movieapp.domain.models.Selection
import io.github.he11pme.movieapp.domain.repository.SelectionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectionRepositoryImpl @Inject constructor(
    private val collectionsDataSource: MovieCollectionsDataSource,
): SelectionRepository {
    override fun getCollections(): Result<List<Selection>> {
        return try {
            Result.success(collectionsDataSource.getCollections().map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}