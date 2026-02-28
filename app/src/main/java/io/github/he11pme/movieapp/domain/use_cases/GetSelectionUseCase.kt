package io.github.he11pme.movieapp.domain.use_cases

import io.github.he11pme.movieapp.domain.models.Selection
import io.github.he11pme.movieapp.domain.repository.SelectionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSelectionUseCase @Inject constructor(
    private val selectionRepository: SelectionRepository
) {

    operator fun invoke(): Result<List<Selection>> {
        return selectionRepository.getCollections()
    }

}