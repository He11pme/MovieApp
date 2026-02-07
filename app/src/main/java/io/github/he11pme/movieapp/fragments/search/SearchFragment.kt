package io.github.he11pme.movieapp.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.he11pme.movieapp.databinding.FragmentSearchBinding
import io.github.he11pme.movieapp.fragments.detail.Source
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.utils.SearchItemOffsetsDecoration
import io.github.he11pme.movieapp.utils.extensions.hideKeyboard
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
        postponeEnterTransition()
        binding.searchRv.doOnPreDraw { startPostponedEnterTransition() }

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
        binding.searchRv.apply {
            adapter = searchAdapter

            addItemDecoration(SearchItemOffsetsDecoration())

            addOnScrollListener(hideKeyboardWhenRvScrolled())
        }
    }

    private fun hideKeyboardWhenRvScrolled(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        }
    }


    private fun toMovieDetails(sharedPoster: View, movieId: Int) {
        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(sharedPoster, sharedPoster.transitionName)
            .build()

        val action = SearchFragmentDirections.navigateFromSearchFragmentToNavigation(
            movieId = movieId,
            transitionName = sharedPoster.transitionName,
            source = Source.SEARCH
        )

        hideKeyboard()
        findNavController().navigate(action, extras)
    }
}