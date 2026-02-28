package io.github.he11pme.movieapp.domain.models

data class Selection(
    val id: String,
    val priority: Int,
    val title: String,
    val type: SelectionType,
)

sealed class SelectionType(val name: String) {
    object Popular : SelectionType("popular")
    object NowPlaying : SelectionType("now_playing")
    data class OfGenres(val genresIds: List<Int>) : SelectionType("of_genres")

}
