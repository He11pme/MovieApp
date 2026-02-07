package io.github.he11pme.movieapp.view.rv.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.databinding.MovieItemSmallBinding
import io.github.he11pme.movieapp.data.network.dto.Movie
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

class MovieItemSmallViewHolder(
    private val binding: MovieItemSmallBinding,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.movie = movie

        binding.smallPoster.transitionName = "poster_${movie.id}"
        Glide.with(binding.smallPoster)
            .load(movie.posterUrl(PosterSizes.SMALL))
            .into(binding.smallPoster)

        binding.root.setOnClickListener { toMovieDetails(binding.smallPoster, movie.id) }

    }

}