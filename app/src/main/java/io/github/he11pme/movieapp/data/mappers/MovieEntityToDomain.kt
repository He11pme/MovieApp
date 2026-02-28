package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity
import io.github.he11pme.movieapp.domain.models.Movie

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        vote = vote,
        genreIds = genreIds,
    )
}