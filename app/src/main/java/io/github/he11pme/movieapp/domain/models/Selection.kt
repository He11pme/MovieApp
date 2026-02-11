package io.github.he11pme.movieapp.domain.models

data class Selection(
    val id: String,
    val priority: Int,
    val title: String,
    val type: SelectionType,
)

sealed interface SelectionType {
    object Popular : SelectionType
    object NowPlaying : SelectionType
    data class OfGenres(val genresIds: List<Int>) : SelectionType

}
