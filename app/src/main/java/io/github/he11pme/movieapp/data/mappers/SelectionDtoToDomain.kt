package io.github.he11pme.movieapp.data.mappers

import io.github.he11pme.movieapp.data.local.assets.dto.SelectionDTO
import io.github.he11pme.movieapp.domain.models.Selection

fun SelectionDTO.toDomain(): Selection {
    return Selection(
        id = id,
        priority = priority,
        title = getLocaleTitle(),
        type = type.toDomain()
    )
}