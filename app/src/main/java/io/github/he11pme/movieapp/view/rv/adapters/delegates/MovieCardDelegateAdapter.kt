package io.github.he11pme.movieapp.fragments.home.carousel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import io.github.he11pme.movieapp.databinding.MovieItemMediumBinding
import io.github.he11pme.movieapp.view.model.Identifiable
import io.github.he11pme.movieapp.data.network.dto.Movie
import io.github.he11pme.movieapp.view.rv.viewholders.MovieItemMediumViewHolder

class MovieCardDelegateAdapter(
    private val selectionId: String,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) :
    AbsListItemAdapterDelegate<Movie, Identifiable, MovieItemMediumViewHolder>() {
    override fun isForViewType(
        item: Identifiable,
        items: List<Identifiable?>,
        position: Int
    ): Boolean {
        return item is Movie
    }

    override fun onCreateViewHolder(parent: ViewGroup): MovieItemMediumViewHolder {
        val binding = MovieItemMediumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieItemMediumViewHolder(
            binding = binding,
            selectionId = selectionId,
            toMovieDetails = toMovieDetails
        )
    }

    override fun onBindViewHolder(
        item: Movie,
        holder: MovieItemMediumViewHolder,
        payloads: List<Any?>
    ) {
        holder.bind(item)
    }

//    inner class ViewHolder(private val binding: MovieItemMediumBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(movie: Movie) {
//            binding.movie = movie
//
//            Glide.with(binding.root)
//                .load(movie.posterUrl(PosterSizes.MEDIUM))
//                .into(binding.posterMovieItem)
//
//            binding.posterMovieItem.transitionName = "$selectionId::${movie.id}"
//
//            binding.root.setOnClickListener { toMovieDetails(binding.posterMovieItem, movie.id) }
//
//        }
//
//    }
}