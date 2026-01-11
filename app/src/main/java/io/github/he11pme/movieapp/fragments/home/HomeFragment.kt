package io.github.he11pme.movieapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.FragmentHomeBinding
import io.github.he11pme.movieapp.model.Selection
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

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

    private fun handleSelectionState(selections: List<Selection>) {
        adapter.submitList(selections)
    }

    private fun toMovieDetails(sharedPoster: View, movieId: Int) {
        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(sharedPoster, sharedPoster.transitionName)
            .build()

        val bundle = Bundle().apply {
            putString("transitionName", sharedPoster.transitionName)
            putInt("movieId", movieId)
        }

        binding.root.findNavController().navigate(R.id.detailInfoFragment, bundle, null, extras)

    }

    private fun toSelection(selectionId: String) {}
}