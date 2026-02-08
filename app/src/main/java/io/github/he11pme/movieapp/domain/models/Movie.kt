package io.github.he11pme.movieapp.domain.models

import io.github.he11pme.movieapp.view.model.Identifiable
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

data class Movie(
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
