package io.github.he11pme.movieapp.view.rv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.github.he11pme.movieapp.databinding.MovieItemSmallBinding
import io.github.he11pme.movieapp.view.rv.diffcallbacks.MovieDiffCallback
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.view.rv.viewholders.MovieItemSmallViewHolder

class SearchAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : ListAdapter<Movie, MovieItemSmallViewHolder>(MovieDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieItemSmallViewHolder {

        val binding = MovieItemSmallBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieItemSmallViewHolder(
            binding = binding,
            toMovieDetails = toMovieDetails
        )
    }

    override fun onBindViewHolder(
        holder: MovieItemSmallViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}