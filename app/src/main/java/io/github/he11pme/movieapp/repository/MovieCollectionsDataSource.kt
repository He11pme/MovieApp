package io.github.he11pme.movieapp.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.model.Selection
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ActivityRetainedScoped
class MovieCollectionsDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun getCollections(): List<Selection> {
        val movieCollectionsString = context.assets.open("movie_collections.json")
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(movieCollectionsString)
    }
}