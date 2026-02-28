package io.github.he11pme.movieapp.view.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.repository.FavoriteRepository
import io.github.he11pme.movieapp.view.model.MovieDetailsUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _action = MutableSharedFlow<Action>()
    val action: Flow<Action> get() = _action

    fun loadFavoriteMovies() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            getFavoriteMovies()
        }
    }

    private suspend fun getFavoriteMovies() {
        val loadedMovies: MutableList<MovieDetails> = mutableListOf()

        repository.getAllFavorites().let { allResults ->
            if (allResults.isEmpty()) {
                _state.emit(State.Empty)
                return
            }

            allResults.forEach { result ->
                result.apply {
                    onSuccess { loadedMovies.add(it) }
                    onFailure { _action.emit(Action.ErrorLoad(it)) }
                }
            }

            if (loadedMovies.isEmpty()) {
                _state.emit(State.Error("No movies hase been loaded"))
                return
            }

            _state.emit(State.Loaded(loadedMovies))

        }
    }

    fun movieSwiped(movie: MovieDetailsUi) = removeFavoriteMovieById(movie.id)

    private fun removeFavoriteMovieById(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavoriteById(movieId)

            (_state.value as? State.Loaded)?.let {
                val updatedList = it.moviesDetails.filterNot { movie ->
                    movie.id == movieId
                }
                _state.emit(
                    if (updatedList.isEmpty()) State.Empty
                    else State.Loaded(updatedList)
                )
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