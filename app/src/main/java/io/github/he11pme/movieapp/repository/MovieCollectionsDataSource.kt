package io.github.he11pme.movieapp.repository

import android.content.Context
import io.github.he11pme.movieapp.model.Selection
import kotlinx.serialization.json.Json

class MovieCollectionsDataSource (private val context: Context) {

    fun getCollections(): List<Selection> {
        val movieCollectionsString = context.assets.open("movie_collections.json")
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(movieCollectionsString)
    }

}