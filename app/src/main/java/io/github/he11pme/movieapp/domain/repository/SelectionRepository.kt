package io.github.he11pme.movieapp.domain.repository

import io.github.he11pme.movieapp.domain.models.Selection
import javax.inject.Singleton

@Singleton
interface SelectionRepository {

    fun getCollections(): Result<List<Selection>>

}