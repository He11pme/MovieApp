package io.github.he11pme.movieapp.view.rv.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.databinding.MovieItemMediumBinding
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

class MovieItemMediumViewHolder(
    private val binding: MovieItemMediumBinding,
    private val selectionId: String,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.movie = movie

        Glide.with(binding.root)
            .load(movie.posterUrl(PosterSizes.MEDIUM))
            .into(binding.posterMovieItem)

        binding.posterMovieItem.transitionName = "$selectionId::${movie.id}"

        binding.root.setOnClickListener { toMovieDetails(binding.posterMovieItem, movie.id) }

    }

}