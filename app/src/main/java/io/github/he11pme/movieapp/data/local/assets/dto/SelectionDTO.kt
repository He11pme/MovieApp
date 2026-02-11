package io.github.he11pme.movieapp.data.local.assets.dto

import android.util.Log
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class SelectionDTO(
    val id: String,
    val priority: Int = Int.MAX_VALUE,
    private val titles: Map<String, String>,
    val type: SelectionTypeDTO,
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
