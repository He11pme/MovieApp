package io.github.he11pme.movieapp.view.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.data.repository.AppRepository
import io.github.he11pme.movieapp.view.mappers.toUi
import io.github.he11pme.movieapp.view.model.MovieUi
import io.github.he11pme.movieapp.view.model.SelectionState
import io.github.he11pme.movieapp.view.model.SelectionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    private var availableSelection: List<SelectionUi> = emptyList()

    private val _selectionsState = MutableStateFlow<List<SelectionUi>>(emptyList())
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
            onSuccess { availableSelection = it.map { selection -> selection.toUi() } }
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

    private fun loadMoviesForSelection(selection: SelectionUi) {
        viewModelScope.launch {
            val result = repository.getSelectionMovies(selection.type)

            updateSelectionStateById(selection.id) { selection ->
                result.fold(
                    onSuccess = { movies ->
                        markSelectionAsLoaded(selection, movies.map { it.toUi() })
                    },
                    onFailure = { e ->
                        markSelectionAsError(selection, e.message.toString())
                    }
                )
            }
        }
    }

    private fun updateSelectionStateById(id: String, doUpdate: (SelectionUi) -> SelectionUi) {
        _selectionsState.update { selections ->
            selections.map {
                if (it.id == id) doUpdate(it)
                else it
            }
        }
    }

    private fun markSelectionAsLoaded(selection: SelectionUi, movies: List<MovieUi>): SelectionUi {
        return selection.copy(state = SelectionState.Loaded(movies))
    }

    private fun markSelectionAsError(selection: SelectionUi, message: String): SelectionUi {
        return selection.copy(state = SelectionState.Error(message))
    }

}