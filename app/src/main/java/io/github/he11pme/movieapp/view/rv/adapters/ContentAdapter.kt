package io.github.he11pme.movieapp.view.rv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.github.he11pme.movieapp.databinding.SelectionMoviesBinding
import io.github.he11pme.movieapp.data.local.assets.dto.Selection
import io.github.he11pme.movieapp.view.rv.diffcallbacks.SelectionDiffCallback
import io.github.he11pme.movieapp.view.rv.viewholders.SelectionMoviesViewHolder

class ContentAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit,
    private val toSelections: (selectionId: String) -> Unit
) :
    ListAdapter<Selection, SelectionMoviesViewHolder>(SelectionDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectionMoviesViewHolder {
        val binding = SelectionMoviesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SelectionMoviesViewHolder(
            binding = binding,
            toMovieDetails = toMovieDetails,
            toSelections = toSelections
        )
    }

    override fun onBindViewHolder(
        holder: SelectionMoviesViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}