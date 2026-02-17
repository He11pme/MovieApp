package io.github.he11pme.movieapp.view.fragments.home

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
import io.github.he11pme.movieapp.App
import io.github.he11pme.movieapp.databinding.FragmentHomeBinding
import io.github.he11pme.movieapp.utils.AnimationHelper
import io.github.he11pme.movieapp.view.model.SelectionState
import io.github.he11pme.movieapp.view.model.SelectionUi
import io.github.he11pme.movieapp.view.rv.adapters.ContentAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var animationHelper: AnimationHelper

    init {
        App.instance.dagger.inject(this)
    }
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = ContentAdapter(::toMovieDetails, ::toSelection)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initHomeScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setupViews()
        bindToViewModel()
        return binding.root
    }

    private fun setupViews() {
        setupRvContent()
    }

    private fun setupRvContent() {
        binding.rvContent.adapter = adapter
    }

    private fun bindToViewModel() {
        bindState()
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bindSelectionState()
            }
        }
    }

    private suspend fun bindSelectionState() {
        viewModel.selectionsState.collect(::handleSelectionState)
    }

    private fun handleSelectionState(selections: List<SelectionUi>) {
        adapter.submitList(selections)

        if (isAllItemLoaded(selections)) {
            binding.rvContent.doOnPreDraw { startEnterAnimation() }
        }

    }

    private fun startEnterAnimation() {
        startPostponedEnterTransition()

        animationHelper.currentAnimation?.invoke(binding.root, requireActivity())
    }

    private fun isAllItemLoaded(selections: List<SelectionUi>): Boolean =
        selections.all { selection -> selection.state is SelectionState.Loaded || selection.state is SelectionState.Error }

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

    private fun toSelection(selectionId: String) {}
}