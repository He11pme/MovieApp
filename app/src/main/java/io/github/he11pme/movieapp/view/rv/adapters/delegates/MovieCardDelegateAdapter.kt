package io.github.he11pme.movieapp.view.rv.adapters.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import io.github.he11pme.movieapp.databinding.MovieItemMediumBinding
import io.github.he11pme.movieapp.view.model.Identifiable
import io.github.he11pme.movieapp.view.model.MovieUi
import io.github.he11pme.movieapp.view.rv.viewholders.MovieItemMediumViewHolder

class MovieCardDelegateAdapter(
    private val selectionId: String,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit
) :
    AbsListItemAdapterDelegate<MovieUi, Identifiable, MovieItemMediumViewHolder>() {
    override fun isForViewType(
        item: Identifiable,
        items: List<Identifiable?>,
        position: Int
    ): Boolean {
        return item is MovieUi
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
        item: MovieUi,
        holder: MovieItemMediumViewHolder,
        payloads: List<Any?>
    ) {
        holder.bind(item)
    }
}