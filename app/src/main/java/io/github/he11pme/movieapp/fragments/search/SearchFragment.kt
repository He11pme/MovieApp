package io.github.he11pme.movieapp.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.he11pme.movieapp.databinding.FragmentSearchBinding
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.utils.SearchItemOffsetsDecoration
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private val searchAdapter = SearchAdapter(::toMovieDetails)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        setupViews()
        bindToViewModel()

        return binding.root
    }

    private fun bindToViewModel() {
        bindState()
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bindSearchState()
            }
        }
    }

    private suspend fun bindSearchState() {
        viewModel.searchState.collect(::handleSearchState)
    }

    private fun handleSearchState(movies: List<Movie>) {
        searchAdapter.submitList(movies)
    }

    private fun setupViews() {
        setupSearchRv()
    }

    private fun setupSearchRv() {
        binding.searchRv.adapter = searchAdapter
        binding.searchRv.addItemDecoration(SearchItemOffsetsDecoration())
    }

    private fun toMovieDetails(sharedPoster: View, movieId: Int) {}
}