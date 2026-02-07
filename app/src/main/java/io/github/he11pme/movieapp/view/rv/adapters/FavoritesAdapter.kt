package io.github.he11pme.movieapp.view.rv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.he11pme.movieapp.databinding.MovieItemMediumFullwidthBinding
import io.github.he11pme.movieapp.data.network.dto.MovieDetails
import io.github.he11pme.movieapp.view.rv.diffcallbacks.MovieDetailsDiffCallback
import io.github.he11pme.movieapp.view.rv.viewholders.MovieItemMediumFullwidthViewHolder

class FavoritesAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : androidx.recyclerview.widget.ListAdapter<MovieDetails, MovieItemMediumFullwidthViewHolder>(MovieDetailsDiffCallback()) {
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