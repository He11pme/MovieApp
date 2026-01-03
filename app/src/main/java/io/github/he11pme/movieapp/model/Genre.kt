package io.github.he11pme.movieapp.model

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
