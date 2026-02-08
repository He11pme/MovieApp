package io.github.he11pme.movieapp.domain.models

data class Selection(
    val id: String,
    val priority: Int,
    val title: String,
    val type: SelectionType,
    val state: SelectionState = SelectionState.Loading
)

sealed interface SelectionState {
    object Loading : SelectionState
    data class Loaded(val movies: List<Movie>) : SelectionState
    data class Error(val message: String) : SelectionState

}

sealed interface SelectionType {
    object Popular : SelectionType
    object NowPlaying : SelectionType
    data class OfGenres(val genresIds: List<Int>) : SelectionType

}
