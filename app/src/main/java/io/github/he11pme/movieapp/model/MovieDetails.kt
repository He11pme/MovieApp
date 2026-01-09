package io.github.he11pme.movieapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class MovieDetails(
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
    val _vote: Double,
    val genres: List<Genre>,
    @SerialName("production_countries")
    val countries: List<Country>
) {
    val vote: Double
        get() = "%.1f".format(Locale.US, _vote).toDouble()

    val posterUrl: (PosterSizes) -> String = {
        "https://image.tmdb.org/t/p/${it.size}$posterPath"
    }
}

@Serializable
data class Country(
    @SerialName("iso_3166_1")
    val iso: String,
    val name: String
)
