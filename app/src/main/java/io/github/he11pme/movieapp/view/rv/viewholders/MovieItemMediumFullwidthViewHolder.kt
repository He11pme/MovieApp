package io.github.he11pme.movieapp.view.rv.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.databinding.MovieItemMediumFullwidthBinding
import io.github.he11pme.movieapp.view.model.MovieDetailsUi
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

class MovieItemMediumFullwidthViewHolder(
    private val binding: MovieItemMediumFullwidthBinding,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: MovieDetailsUi) {
        binding.movie = movie

        Glide.with(binding.posterMovieItem)
            .load(movie.posterUrl(PosterSizes.SMALL))
            .into(binding.posterMovieItem)

        binding.posterMovieItem.transitionName = "poster_${movie.id}"

        binding.root.setOnClickListener { toMovieDetails(binding.posterMovieItem, movie.id) }

    }
}