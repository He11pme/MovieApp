package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.view.model.MovieUi

class MovieDiffCallback: DiffUtil.ItemCallback<MovieUi>() {
    override fun areItemsTheSame(
        oldItem: MovieUi,
        newItem: MovieUi
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MovieUi,
        newItem: MovieUi
    ): Boolean {
        return oldItem == newItem
    }
}