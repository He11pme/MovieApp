package io.github.he11pme.movieapp.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val genres: List<Genre>
)
@Serializable
data class Genre(
    val id: Int,
    val name: String
)
