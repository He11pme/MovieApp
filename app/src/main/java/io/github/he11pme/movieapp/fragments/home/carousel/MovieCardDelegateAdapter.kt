package io.github.he11pme.movieapp.fragments.home.carousel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import io.github.he11pme.movieapp.databinding.MovieItemMediumBinding
import io.github.he11pme.movieapp.model.Identifiable
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.model.PosterSizes
import java.util.UUID

class MovieCardDelegateAdapter(private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit) :
    AbsListItemAdapterDelegate<Movie, Identifiable, MovieCardDelegateAdapter.ViewHolder>() {
    override fun isForViewType(
        item: Identifiable,
        items: List<Identifiable?>,
        position: Int
    ): Boolean {
        return item is Movie
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            MovieItemMediumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        item: Movie,
        holder: ViewHolder,
        payloads: List<Any?>
    ) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: MovieItemMediumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.titleMovieItem.text = movie.title
            binding.voteMovieItem.text = movie.vote.toString()

            Glide.with(binding.root)
                .load(movie.posterUrl(PosterSizes.MEDIUM))
                .into(binding.posterMovieItem)

            // There may be multiple Views displaying the same movie on the screen.
            // Therefore, a random transitionName is generated and passed explicitly
            binding.posterMovieItem.transitionName = UUID.randomUUID().toString()

            binding.root.setOnClickListener { toMovieDetails(binding.posterMovieItem,movie.id) }

        }

    }
}