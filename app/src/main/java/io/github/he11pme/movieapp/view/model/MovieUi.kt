package io.github.he11pme.movieapp.view.model

import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

data class MovieUi(
    val id: Int,
    val title: String,
    private val posterPath: String,
    val overview: String,
    val vote: Double
): Identifiable {

    val posterUrl: (PosterSizes) -> String = {
        "https://image.tmdb.org/t/p/${it.size}$posterPath"
    }

    override fun getIdentifier() = id.toString()
}
