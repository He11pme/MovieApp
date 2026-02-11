package io.github.he11pme.movieapp.domain.models

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,
    val vote: Double
)
