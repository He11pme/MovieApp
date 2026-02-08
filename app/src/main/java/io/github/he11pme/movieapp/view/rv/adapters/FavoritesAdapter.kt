package io.github.he11pme.movieapp.view.rv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.he11pme.movieapp.databinding.MovieItemMediumFullwidthBinding
import io.github.he11pme.movieapp.view.model.MovieDetailsUi
import io.github.he11pme.movieapp.view.rv.diffcallbacks.MovieDetailsDiffCallback
import io.github.he11pme.movieapp.view.rv.viewholders.MovieItemMediumFullwidthViewHolder

class FavoritesAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : androidx.recyclerview.widget.ListAdapter<MovieDetailsUi, MovieItemMediumFullwidthViewHolder>(MovieDetailsDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieItemMediumFullwidthViewHolder {

        val binding = MovieItemMediumFullwidthBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieItemMediumFullwidthViewHolder(
            binding = binding,
            toMovieDetails = toMovieDetails
        )
    }

    override fun onBindViewHolder(
        holder: MovieItemMediumFullwidthViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}