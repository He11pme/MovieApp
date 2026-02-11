package io.github.he11pme.movieapp.view.mappers

import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.view.model.MovieUi

fun Movie.toUi(): MovieUi {
    return MovieUi(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        vote = vote
    )
}