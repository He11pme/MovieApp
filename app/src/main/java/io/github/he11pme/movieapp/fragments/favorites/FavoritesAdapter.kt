package io.github.he11pme.movieapp.fragments.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.MovieItemMediumFullwidthBinding

import io.github.he11pme.movieapp.model.MovieDetails
import io.github.he11pme.movieapp.model.PosterSizes

class FavoritesAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : ListAdapter<MovieDetails, FavoritesAdapter.ViewHolder>(FavoritesDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            MovieItemMediumFullwidthBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: MovieItemMediumFullwidthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDetails) {

            Glide.with(binding.posterMovieItem)
                .load(movie.posterUrl(PosterSizes.SMALL))
                .into(binding.posterMovieItem)

            binding.posterMovieItem.transitionName = "poster_${movie.id}"

            binding.titleMovieItem.text = movie.title
            binding.voteMovieItem.text = binding.root.context.getString(R.string.tmdb_vote, movie.vote)
            binding.overviewMovieItem.text = movie.overview
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

}