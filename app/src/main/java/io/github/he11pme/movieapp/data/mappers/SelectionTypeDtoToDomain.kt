package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.assets.dto.SelectionTypeDTO
import io.github.he11pme.movieapp.domain.models.SelectionType

fun SelectionTypeDTO.toDomain(): SelectionType {
    return when (this) {
        SelectionTypeDTO.NowPlaying -> SelectionType.NowPlaying
        is SelectionTypeDTO.OfGenres -> SelectionType.OfGenres(this.genres.map { it.id })
        SelectionTypeDTO.Popular -> SelectionType.Popular
    }
}