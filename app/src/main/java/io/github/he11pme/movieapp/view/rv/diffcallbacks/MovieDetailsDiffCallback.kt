package io.github.he11pme.movieapp.view.rv.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import io.github.he11pme.movieapp.view.model.MovieDetailsUi

class MovieDetailsDiffCallback : DiffUtil.ItemCallback<MovieDetailsUi>() {
    override fun areItemsTheSame(
        oldItem: MovieDetailsUi,
        newItem: MovieDetailsUi
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MovieDetailsUi,
        newItem: MovieDetailsUi
    ): Boolean {
        return oldItem == newItem
    }
}