package io.github.he11pme.movieapp.view.model

import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

data class MovieDetailsUi(
    val id: Int,
    val title: String,
    val tagline: String,
    val overview: String,
    val posterPath: String,
    val vote: Float,
    val parameters: String,
    val shortParameters: String
) {
    var isFavorite = false

    val posterUrl: (PosterSizes) -> String = {
        "https://image.tmdb.org/t/p/${it.size}$posterPath"
    }
}