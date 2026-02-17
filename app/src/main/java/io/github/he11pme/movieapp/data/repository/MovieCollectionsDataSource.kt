package io.github.he11pme.movieapp.data.repository

import android.content.Context
import io.github.he11pme.movieapp.data.local.assets.dto.SelectionDTO
import kotlinx.serialization.json.Json
import javax.inject.Inject


class MovieCollectionsDataSource @Inject constructor(val context: Context) {
    fun getCollections(): List<SelectionDTO> {
        val movieCollectionsString = context.assets.open("movie_collections.json")
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(movieCollectionsString)
    }
}