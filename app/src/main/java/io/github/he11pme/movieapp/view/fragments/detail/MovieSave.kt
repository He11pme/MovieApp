package io.github.he11pme.movieapp.view.fragments.detail

import android.graphics.Bitmap

data class MovieSave(
    val filename: String,
    val poster: Bitmap,
    val mimeType: String,
    val path: String,
    val description: String
)
