package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.assets.dto.SelectionDTO
import io.github.he11pme.movieapp.data.local.assets.dto.SelectionTypeDTO
import io.github.he11pme.movieapp.data.network.dto.MovieDTO
import io.github.he11pme.movieapp.data.network.dto.MovieDetailsDTO
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.models.Selection
import io.github.he11pme.movieapp.domain.models.SelectionType
import java.util.Locale

fun MovieDTO.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        vote = "%.1f".format(Locale.US, vote).toDouble()
    )
}

fun SelectionDTO.toDomain(): Selection {
    return Selection(
        id = id,
        priority = priority,
        title = getLocaleTitle(),
        type = type.toDomain()
    )
}

fun SelectionTypeDTO.toDomain(): SelectionType {
    return when (this) {
        SelectionTypeDTO.NowPlaying -> SelectionType.NowPlaying
        is SelectionTypeDTO.OfGenres -> SelectionType.OfGenres(this.genres.map { it.id })
        SelectionTypeDTO.Popular -> SelectionType.Popular
    }
}

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