package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.network.dto.MovieDetailsDTO
import io.github.he11pme.movieapp.domain.models.MovieDetails
import java.util.Locale

fun MovieDetailsDTO.toDomain(isFavorite: Boolean): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        tagline = tagline,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        runtime = runtime,
        vote = "%.1f".format(Locale.US, vote).toDouble(),
        genres = genres.map { it.name },
        countries = countries.map { it.name },
    ).apply {
        this.isFavorite = isFavorite
    }
}