package io.github.he11pme.movieapp.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.he11pme.movieapp.databinding.MovieItemSmallBinding
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.model.PosterSizes

class SearchAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) : ListAdapter<Movie, SearchAdapter.ViewHolder>(SearchDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            MovieItemSmallBinding.inflate(
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

    inner class ViewHolder(private val binding: MovieItemSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.titleMovie.text = movie.title
            binding.voteMovie.text = movie.vote.toString()

            binding.smallPoster.transitionName = "poster_${movie.id}"
            Glide.with(binding.smallPoster)
                .load(movie.posterUrl(PosterSizes.SMALL))
                .into(binding.smallPoster)

            binding.root.setOnClickListener { toMovieDetails(binding.smallPoster, movie.id) }

        }

    }


}