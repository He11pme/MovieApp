package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.data.network.dto.MovieDetails

class MovieDetailsDiffCallback : DiffUtil.ItemCallback<MovieDetails>() {
    override fun areItemsTheSame(
        oldItem: MovieDetails,
        newItem: MovieDetails
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MovieDetails,
        newItem: MovieDetails
    ): Boolean {
        return oldItem == newItem
    }
}