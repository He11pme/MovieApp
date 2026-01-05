package io.github.he11pme.movieapp.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.databinding.FragmentSearchBinding
import io.github.he11pme.movieapp.model.Selection
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = ContentAdapter(::toMovieDetails)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initHomeScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

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

    private fun toMovieDetails(movieId: Int) {}
}