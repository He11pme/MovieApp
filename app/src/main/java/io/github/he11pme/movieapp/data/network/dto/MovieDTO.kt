package io.github.he11pme.movieapp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val page: Int,
    @SerialName("total_pages")
    val totalPage: Int,
    @SerialName("results")
    val movies: List<MovieDTO>
)

@Serializable
data class MovieDTO(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val posterPath: String = "",
    val overview: String,
    @SerialName("vote_average")
    val vote: Double = 0.0
)