package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.network.dto.MovieDTO
import io.github.he11pme.movieapp.domain.models.Movie
import java.util.Locale

fun MovieDTO.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        vote = "%.1f".format(Locale.US, vote).toDouble(),
        genreIds = genreIds
    )
}