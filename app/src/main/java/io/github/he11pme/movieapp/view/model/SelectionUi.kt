package io.github.he11pme.movieapp.view.model

import io.github.he11pme.movieapp.domain.models.SelectionType

data class SelectionUi(
    val id: String,
    val priority: Int,
    val title: String,
    val type: SelectionType,
    val state: SelectionState = SelectionState.Loading
)

sealed interface SelectionState {
    object Loading : SelectionState
    data class Loaded(val movies: List<MovieUi>) : SelectionState
    data class Error(val message: String) : SelectionState

}