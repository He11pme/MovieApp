package io.github.he11pme.movieapp.utils.extensions

import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

val MovieDetails.posterUrl: (PosterSizes) -> String
    get() = {
        "https://image.tmdb.org/t/p/${it.size}$posterPath"
    }