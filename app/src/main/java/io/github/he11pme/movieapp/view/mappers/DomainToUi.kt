package io.github.he11pme.movieapp.view.mappers

import android.content.Context
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.view.model.MovieDetailsUi

fun MovieDetails.toUi(context: Context, separator: String = " • "): MovieDetailsUi {

    fun compoundParameters(): String {
        val year = releaseDate.take(4)
        val genres = genres.take(3).joinToString(separator)
        val country = countries.firstOrNull() ?: context.getString(R.string.unknown_country)
        val runtime = context.getString(R.string.runtime, runtime)

        return "$year$separator$genres\n$country$separator$runtime"
    }

    fun compoundShortParameters(): String {
        val genres = genres.take(2).joinToString(separator)
        val runtime = context.getString(R.string.runtime, runtime)

        return "$genres$separator$runtime"
    }

    return MovieDetailsUi(
        id = id,
        title = title,
        tagline = tagline,
        overview = overview,
        posterPath = posterPath,
        vote = vote.toFloat(),
        parameters = compoundParameters(),
        shortParameters = compoundShortParameters()
    ).apply {
        isFavorite = this@toUi.isFavorite
    }
}