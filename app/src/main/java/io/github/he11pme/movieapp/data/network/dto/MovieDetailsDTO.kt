package io.github.he11pme.movieapp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDTO(
    val id: Int,
    val title: String,
    val tagline: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    val runtime: Int,
    @SerialName("vote_average")
    val vote: Double,
    val genres: List<GenreDTO>,
    @SerialName("production_countries")
    val countries: List<CountryDTO>
)

@Serializable
data class CountryDTO(
    @SerialName("iso_3166_1")
    val iso: String,
    val name: String
)
