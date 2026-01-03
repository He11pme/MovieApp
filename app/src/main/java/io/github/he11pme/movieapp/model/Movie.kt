package io.github.he11pme.movieapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val page: Int,
    @SerialName("total_page")
    val totalPage: Int,
    @SerialName("results")
    val movies: List<Movie>
)
@Serializable
data class Movie(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val posterPath: String,
    val overview: String,
    @SerialName("vote_average")
    val vote: Double
)
