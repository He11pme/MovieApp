package io.github.he11pme.movieapp.fragments.search

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.model.Movie

class SearchDiffCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }
}
