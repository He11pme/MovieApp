package io.github.he11pme.movieapp.model

data class ShowAllMoviesButton(
    val selectionId: String
) : Identifiable {
    override fun getIdentifier(): String = selectionId
}