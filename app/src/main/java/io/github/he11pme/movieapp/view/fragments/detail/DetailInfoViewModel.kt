package io.github.he11pme.movieapp.fragments.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.data.repository.AppRepository
import io.github.he11pme.movieapp.managers.AppBarManager
import io.github.he11pme.movieapp.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val repository: AppRepository,
    val appBarManager: AppBarManager
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: Flow<Action> get() = _actions

    private var movie: MovieDetails? = null

    fun loadDetails(movieId: Int) {
        _state.value = State.Loading
        viewModelScope.launch {
            tryLoadDetails(movieId).apply {
                onSuccess { handleSuccessLoadMovie(it) }
                onFailure {  }
            }
        }
    }

    private fun handleSuccessLoadMovie(details: MovieDetails) {
        movie = details.also { appBarManager.updateFavoriteState(it.isFavorite) }

        _state.value = State.Loaded(details)
    }

    private suspend fun tryLoadDetails(movieId: Int): Result<MovieDetails> =
        repository.getMovieById(movieId)

    fun onAppBarScrolled(collapseRatio: Float) {
        if (collapseRatio > 0.25f) appBarManager.showAppBar() else appBarManager.hideAppBar()
        appBarManager.updateAlphaAppBar(collapseRatio)
        appBarManager.updateTitleAppBar(if (collapseRatio > 0.75f) movie?.title ?: "" else "")
    }

    fun onFavoriteBtnClicked() = toggleFavorite()

    private fun toggleFavorite() {
        movie?.let {
            viewModelScope.launch {
                if (repository.toggleFavorite(it.id)) addFavorite()
                else removeFavorite()

                appBarManager.updateFavoriteState(it.isFavorite)
            }
        }
    }

    private suspend fun removeFavorite() {
        _actions.emit(Action.RemoveFavorite)
        movie?.isFavorite = false
    }

    private suspend fun addFavorite() {
        _actions.emit(Action.AddFavorite)
        movie?.isFavorite = true
    }

    fun onShareBtnClicked() = shareMovie()

    private fun shareMovie() {
        movie?.let {
            viewModelScope.launch {
                _actions.emit(Action.ShareMovie(it))
            }
        }
    }

    fun onDownloadBtnClicked() = downloadMovie()

    private fun downloadMovie() {
        movie?.let {
            viewModelScope.launch {
                _actions.emit(Action.DownloadMovie(it))
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: Int) : State
        data class Loaded(val movieDetails: MovieDetails): State
    }

    sealed interface Action {
        data class ShareMovie(val movie: MovieDetails): Action
        data class DownloadMovie(val movie: MovieDetails): Action

        object RemoveFavorite: Action
        object AddFavorite: Action
    }

}