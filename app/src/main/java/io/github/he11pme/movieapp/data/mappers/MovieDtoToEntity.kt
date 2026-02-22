package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.sqlite.MovieEntity
import io.github.he11pme.movieapp.data.network.dto.MovieDTO

fun MovieDTO.toEntity(
    isPopular: Boolean = false,
    isNowPlaying: Boolean = false
): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        vote = vote,
        genreIds = genreIds,
        isPopular = isPopular,
        isNowPlaying = isNowPlaying
    )
}