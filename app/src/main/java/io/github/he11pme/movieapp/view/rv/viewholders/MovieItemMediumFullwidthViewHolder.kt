package io.github.he11pme.movieapp.view.rv.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.MovieItemMediumFullwidthBinding
import io.github.he11pme.movieapp.data.network.dto.MovieDetails
import io.github.he11pme.movieapp.view.rv.utils.enums.PosterSizes

class MovieItemMediumFullwidthViewHolder(
    private val binding: MovieItemMediumFullwidthBinding,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: MovieDetails) {
        binding.movie = movie

        Glide.with(binding.posterMovieItem)
            .load(movie.posterUrl(PosterSizes.SMALL))
            .into(binding.posterMovieItem)

        binding.posterMovieItem.transitionName = "poster_${movie.id}"

        binding.parametersMovieItem.text = compoundParameters(movie)

        binding.root.setOnClickListener { toMovieDetails(binding.posterMovieItem, movie.id) }

    }

    fun compoundParameters(movie: MovieDetails): String {
        val separator = " • "
        val genres = movie.genres.take(3).joinToString(separator) { genre -> genre.name }
        val runtime = binding.root.context.getString(R.string.runtime, movie.runtime)

        return "$genres$separator$runtime"
    }
}