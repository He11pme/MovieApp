package io.github.he11pme.movieapp.data.local.assets.dto

import android.util.Log
import io.github.he11pme.movieapp.data.network.dto.Genre
import io.github.he11pme.movieapp.data.network.dto.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Locale

@Serializable
data class Selection(
    val id: String,
    val priority: Int = Int.MAX_VALUE,
    private val titles: Map<String, String>,
    val type: SelectionType,
    @Transient
    val state: SelectionState = SelectionState.Loading
) {
    fun getLocaleTitle(locale: Locale = Locale.getDefault()): String {
        val language = locale.language

        return titles[language]
            ?: run {
                Log.w(TAG, "Missing title for locale = $language")
                titles[BASE_LANGUAGE]
            }
            ?: run {
                Log.w(TAG, "Missing title for base locale = $BASE_LANGUAGE")
                titles.values.firstOrNull()
            }
            ?: run {
                Log.e(TAG, "titles is empty")
                throw RuntimeException("Title not found")
            }

    }
    companion object {
        const val TAG = "Selection"
        private const val BASE_LANGUAGE = "en"
    }
}

@Serializable
sealed interface SelectionState {
    object Loading : SelectionState
    data class Loaded(val movies: List<Movie>) : SelectionState
    data class Error(val message: String) : SelectionState

}

@Serializable
sealed interface SelectionType {
    @Serializable
    @SerialName("popular")
    object Popular : SelectionType

    @Serializable
    @SerialName("now_playing")
    object NowPlaying : SelectionType

    @Serializable
    @SerialName("by_genres")
    data class OfGenres(val genres: List<Genre>) : SelectionType

}
