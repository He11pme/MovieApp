package io.github.he11pme.movieapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class MovieResponse(
    val page: Int,
    @SerialName("total_pages")
    val totalPage: Int,
    @SerialName("results")
    val movies: List<Movie>
)
@Serializable
data class Movie(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    private val posterPath: String,
    val overview: String,
    @SerialName("vote_average")
    private val _vote: Double
): Identifiable {
    val vote: Double
        get() = "%.1f".format(Locale.US, _vote).toDouble()
    val posterUrl: (PosterSizes) -> String = {
        "https://image.tmdb.org/t/p/${it.size}$posterPath"
    }

    override fun getIdentifier() = id.toString()
}
