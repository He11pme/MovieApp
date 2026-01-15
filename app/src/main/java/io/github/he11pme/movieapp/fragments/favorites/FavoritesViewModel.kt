package io.github.he11pme.movieapp.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.data.repository.AppRepository
import io.github.he11pme.movieapp.model.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _action = MutableSharedFlow<Action>()
    val action: Flow<Action> get() = _action

    fun getFavoritesMovie() {
        viewModelScope.launch {
            val loadedMovies: MutableList<MovieDetails> = mutableListOf()

            _state.emit(State.Loading)

            appRepository.getAllFavorites().let { allResults ->
                if (allResults.isEmpty()) {
                    _state.emit(State.Empty)
                    return@launch
                }

                allResults.forEach { result ->
                    result.apply {
                        onSuccess { loadedMovies.add(it) }
                        onFailure { _action.emit(Action.ErrorLoad(it)) }
                    }
                }

                if (loadedMovies.isEmpty()) {
                    _state.emit(State.Error("No movies hase been loaded"))
                    return@launch
                }

                _state.emit(State.Loaded(loadedMovies))

            }
        }
    }

    sealed interface State {
        object Loading : State
        object Empty : State
        data class Loaded(val moviesDetails: List<MovieDetails>) : State
        data class Error(val message: String): State
    }

    sealed interface Action {
        data class ErrorLoad(val e: Throwable): Action
    }

}