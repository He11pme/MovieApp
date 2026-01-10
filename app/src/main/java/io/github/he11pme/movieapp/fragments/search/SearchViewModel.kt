package io.github.he11pme.movieapp.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.model.Movie
import io.github.he11pme.movieapp.model.SelectionState
import io.github.he11pme.movieapp.model.Selection
import io.github.he11pme.movieapp.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    private var availableSelection: List<Selection> = emptyList()

    private val _selectionsState = MutableStateFlow<List<Selection>>(emptyList())
    val selectionsState = _selectionsState.asStateFlow()

    fun initHomeScreen() {
        if (!fetchAvailableSelections()) return

        setAllSelectionsInLoadingState()

        loadMoviesForAllSelections()
    }

    /**
     * Loads available movie selections
     * @return true if successful or false on failure
     */
    private fun fetchAvailableSelections(): Boolean {
        repository.getCollections().apply {
            onSuccess { availableSelection = it }
            onFailure { return false }
        }
        return true
    }

    private fun setAllSelectionsInLoadingState() {
        _selectionsState.value = availableSelection
            .sortedBy { it.priority }
            .map {
                if (it.state != SelectionState.Loading) {
                    it.copy(state = SelectionState.Loading)
                } else it
            }
    }

    private fun loadMoviesForAllSelections() {
        availableSelection.forEach { loadMoviesForSelection(it) }
    }

    private fun loadMoviesForSelection(selection: Selection) {
        viewModelScope.launch {
            val result = repository.getSelectionMovies(selection.type)

            updateSelectionStateById(selection.id) {
                result.fold(
                    onSuccess = { movies ->
                        markSelectionAsLoaded(it, movies)
                    },
                    onFailure = { e ->
                        markSelectionAsError(it, e.message.toString())
                    }
                )
            }
        }
    }

    private fun updateSelectionStateById(id: String, doUpdate: (Selection) -> Selection) {
        _selectionsState.update { selections ->
            selections.map {
                if (it.id == id) doUpdate(it)
                else it
            }
        }
    }

    private fun markSelectionAsLoaded(selection: Selection, movies: List<Movie>): Selection {
        return selection.copy(state = SelectionState.Loaded(movies))
    }

    private fun markSelectionAsError(selection: Selection, message: String): Selection {
        return selection.copy(state = SelectionState.Error(message))
    }

}