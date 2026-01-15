package io.github.he11pme.movieapp.fragments.favorites

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.model.MovieDetails

class FavoritesDiffCallback : DiffUtil.ItemCallback<MovieDetails>() {
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