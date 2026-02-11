package io.github.he11pme.movieapp.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val tagline: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val runtime: Int,
    val vote: Double,
    val genres: List<String>,
    val countries: List<String>,
) {
    var isFavorite: Boolean = false
}