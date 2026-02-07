package io.github.he11pme.movieapp.view.rv.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.SelectionMoviesBinding
import io.github.he11pme.movieapp.view.model.Identifiable
import io.github.he11pme.movieapp.data.local.assets.dto.Selection
import io.github.he11pme.movieapp.data.local.assets.dto.SelectionState
import io.github.he11pme.movieapp.view.model.ShowAllMoviesButton
import io.github.he11pme.movieapp.utils.extensions.dp
import io.github.he11pme.movieapp.view.rv.adapters.CarouselAdapter
import io.github.he11pme.movieapp.view.rv.utils.ItemOffsetsDecoration
import io.github.he11pme.movieapp.view.rv.utils.StartLinearSnapHelper

class SelectionMoviesViewHolder(
    private val binding: SelectionMoviesBinding,
    private val toMovieDetails: (sharedView: View, movieId: Int) -> Unit,
    private val toSelections: (selectionId: String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var adapter: CarouselAdapter
    fun bind(selection: Selection) {
        if (!::adapter.isInitialized) adapter =
            CarouselAdapter(selection.id, toMovieDetails, toSelections)

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

    private fun setupSelection(
        rv: RecyclerView,
        selectionId: String,
        movies: List<Identifiable>
    ) {

        if (rv.adapter == null) rv.adapter = adapter

        if (rv.onFlingListener == null) StartLinearSnapHelper().attachToRecyclerView(rv)

        if (rv.itemDecorationCount == 0) rv.addItemDecoration(ItemOffsetsDecoration(5.dp))

        adapter.submitList(
            movies.toMutableList().also {
                it.add(ShowAllMoviesButton(selectionId))
            }
        )
    }

}