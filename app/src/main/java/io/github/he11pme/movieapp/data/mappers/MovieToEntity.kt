package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.room.entity.MovieEntity
import io.github.he11pme.movieapp.data.local.room.entity.MoviesBySelectionEntity
import io.github.he11pme.movieapp.domain.models.Movie

fun Movie.toEntity(
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

fun Movie.toSelectionEntity(selection: String): List<MoviesBySelectionEntity> {
    return List(genreIds.size) {
        MoviesBySelectionEntity(
            movieId = id,
            genreId = genreIds[it],
            typeSelection = selection
        )
    }
}