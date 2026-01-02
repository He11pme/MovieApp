package io.github.he11pme.movieapp.utils.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * An extension for [View] that makes it easy to respond to system window changes (window insets).
 *
 * @param apply A lambda that is called whenever [WindowInsetsCompat] are applied. It takes two parameters:
 *   - [View] — the view to which the insets are applied.
 *   - [WindowInsetsCompat] — the current system window insets.
 *
 * Example usage:
 * ```
 * toolbar.doOnApplyWindowInsets { view, insets ->
 *     val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
 *     view.updatePadding(top = systemBars.top)
 * }
 * ```
 *
 * This function simplifies working with [ViewCompat.setOnApplyWindowInsetsListener],
 * automatically returning the original insets after processing.
 */
fun View.doOnApplyWindowInsets(apply: (View, WindowInsetsCompat) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        apply(v, insets)
        insets
    }
}