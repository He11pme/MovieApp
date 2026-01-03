package io.github.he11pme.movieapp.utils.extensions

import java.util.Locale

/**
 * Returns the locale as a string in the "language-country" format (e.g., "en-US"),
 * which is required for TMDb API requests.
 */
fun Locale.getFormatLocale(): String {
    return "$language-$country"
}