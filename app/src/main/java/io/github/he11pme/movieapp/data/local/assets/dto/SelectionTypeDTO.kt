package io.github.he11pme.movieapp.data.local.assets.dto

import io.github.he11pme.movieapp.data.network.dto.GenreDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SelectionTypeDTO {
    @Serializable
    @SerialName("popular")
    object Popular : SelectionTypeDTO

    @Serializable
    @SerialName("now_playing")
    object NowPlaying : SelectionTypeDTO

    @Serializable
    @SerialName("by_genres")
    data class OfGenres(val genres: List<GenreDTO>) : SelectionTypeDTO

}