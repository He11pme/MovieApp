package io.github.he11pme.movieapp.view.mappers

import io.github.he11pme.movieapp.domain.models.Selection
import io.github.he11pme.movieapp.view.model.SelectionUi

fun Selection.toUi(): SelectionUi {
    return SelectionUi(
        id = id,
        priority = priority,
        title = title,
        type = type,
    )
}