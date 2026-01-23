package io.github.he11pme.movieapp.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.databinding.FragmentFavoritesBinding
import io.github.he11pme.movieapp.fragments.home.HomeFragmentDirections
import io.github.he11pme.movieapp.utils.AnimationHelper
import io.github.he11pme.movieapp.utils.SearchItemOffsetsDecoration
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter = FavoritesAdapter(::toMovieDetails)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadFavoriteMovies()
        postponeEnterTransition()
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)

        bindToViewModel()
        setupViews()

        return binding.root
    }

    private fun setupViews() {
        setupFavoritesRv()
    }

    private fun setupFavoritesRv() {
        val touchHelper = ItemTouchHelper(
            FavoritesTouchHelperCallback { position ->
                viewModel.movieSwiped(adapter.currentList[position])
            }
        )

        binding.favoritesRv.apply {
            adapter = this@FavoritesFragment.adapter
            addItemDecoration(SearchItemOffsetsDecoration())
            doOnPreDraw {
                startPostponedEnterTransition()
                AnimationHelper.currentAnimation?.invoke(binding.root, requireActivity())
            }
            touchHelper.attachToRecyclerView(this)
        }
    }

    private fun bindToViewModel() {
        bindState()
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bindFavoriteMoviesState()
            }
        }
    }

    private suspend fun bindFavoriteMoviesState() {
        viewModel.state.collect(::handleFavoriteMoviesState)
    }

    private fun handleFavoriteMoviesState(favorites: FavoritesViewModel.State) {
        if (favorites is FavoritesViewModel.State.Loaded) {
            adapter.submitList(favorites.moviesDetails)
        }

    }

    private fun toMovieDetails(sharedPoster: View, movieId: Int) {
        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(sharedPoster, sharedPoster.transitionName)
            .build()

        val action = HomeFragmentDirections.navigateFromGlobalToDetailInfoFragment(
            movieId = movieId,
            transitionName = sharedPoster.transitionName
        )

        findNavController().navigate(action, extras)

    }
}