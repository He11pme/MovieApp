package io.github.he11pme.movieapp.data.local.sqlite

data class MovieEntity(
    val id: Int,
    val title: String,
    val posterPath: String = "",
    val overview: String,
    val vote: Double = 0.0,
    val genreIds: List<Int> = emptyList(),
    val isPopular: Boolean = false,
    val isNowPlaying: Boolean = false,
)
