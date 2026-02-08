package io.github.he11pme.movieapp.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val genres: List<GenreDTO>
)
@Serializable
data class GenreDTO(
    val id: Int,
    val name: String
)
