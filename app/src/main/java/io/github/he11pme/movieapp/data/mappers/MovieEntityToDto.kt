package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity
import io.github.he11pme.movieapp.data.network.dto.MovieDTO

fun MovieEntity.toDto(): MovieDTO {
    return MovieDTO(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        vote = vote,
        genreIds = genreIds,
    )
}