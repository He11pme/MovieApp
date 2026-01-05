package io.github.he11pme.movieapp.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.databinding.MovieItemMediumBinding
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.model.PosterSizes

class CarouselAdapter(val toMovieDetails: (movieId: Int) -> Unit) :
    ListAdapter<Movie, CarouselAdapter.ViewHolder>(CarouselDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            MovieItemMediumBinding.inflate(
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

    inner class ViewHolder(private val binding: MovieItemMediumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.titleMovieItem.text = movie.title
            binding.voteMovieItem.text = movie.vote.toString()

            Glide.with(binding.root)
                .load(movie.posterUrl(PosterSizes.MEDIUM))
                .into(binding.posterMovieItem)

            binding.root.setOnClickListener { toMovieDetails(movie.id) }

        }

    }
}