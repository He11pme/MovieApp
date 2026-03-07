package io.github.he11pme.movieapp.domain.models

import android.graphics.Bitmap

data class DownloadMovie(
    val filename: String,
    val poster: Bitmap,
    val mimeType: String,
    val path: String,
    val description: String
)