package io.github.he11pme.movieapp.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.SelectionMoviesBinding
import io.github.he11pme.movieapp.fragments.search.carousel.CarouselAdapter
import io.github.he11pme.movieapp.fragments.search.decoration.ItemOffsetsDecoration
import io.github.he11pme.movieapp.fragments.search.decoration.StartLinearSnapHelper
import io.github.he11pme.movieapp.model.Identifiable
import io.github.he11pme.movieapp.model.Selection
import io.github.he11pme.movieapp.model.SelectionState
import io.github.he11pme.movieapp.model.ShowAllMoviesButton

class ContentAdapter(
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit,
    private val toSelections: (selectionId: String) -> Unit
) :
    ListAdapter<Selection, ContentAdapter.ViewHolder>(ContentDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            SelectionMoviesBinding.inflate(
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

    inner class ViewHolder(private val binding: SelectionMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val adapter = CarouselAdapter(toMovieDetails, toSelections)
        fun bind(selection: Selection) {
            try {
                binding.titleSelection.text = selection.getLocaleTitle()
            } catch (e: Exception) {
                binding.titleSelection.text =
                    binding.titleSelection.context.getString(R.string.title_not_found)
            }

            binding.showAll.setOnClickListener { toSelections(selection.id) }

            if (selection.state is SelectionState.Loaded)
                setupSelection(
                    rv = binding.rvSelection,
                    selectionId = selection.id,
                    movies = selection.state.movies
                )
        }

        private fun setupSelection(rv: RecyclerView, selectionId: String, movies: List<Identifiable>) {

            if (rv.adapter == null) rv.adapter = adapter

            if (rv.onFlingListener == null) StartLinearSnapHelper().attachToRecyclerView(rv)

            if (rv.itemDecorationCount == 0) rv.addItemDecoration(ItemOffsetsDecoration())

            adapter.submitList(
                movies.toMutableList().also {
                    it.add(ShowAllMoviesButton(selectionId))
                }
            )
        }

    }

}