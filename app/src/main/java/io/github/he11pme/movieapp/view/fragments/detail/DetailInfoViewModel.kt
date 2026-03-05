package io.github.he11pme.movieapp.view.fragments.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.managers.AppBarManager
import io.github.he11pme.movieapp.domain.models.MovieDetails
import io.github.he11pme.movieapp.domain.use_cases.GetMovieDetailUseCase
import io.github.he11pme.movieapp.domain.use_cases.ToggleFavoriteUseCase
import io.github.he11pme.movieapp.utils.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val getMovieDetail: GetMovieDetailUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    val appBarManager: AppBarManager
) : ViewModel() {

    private val _state = SingleLiveEvent<State>()
    val state: LiveData<State> get() = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: Flow<Action> get() = _actions

    private var movie: MovieDetails? = null

    init {
        scheduleTransitionTimeout()
    }

    private fun scheduleTransitionTimeout() {
        viewModelScope.launch {
            delay(200)
            if (_state.value == State.Loading) _actions.emit(Action.Release)
        }
    }

    fun loadDetails(movieId: Int) {
        _state.value = State.Loading
        viewModelScope.launch {
            tryLoadDetails(movieId).apply {
                onSuccess { handleSuccessLoadMovie(it) }
                onFailure { handleErrorLoadMovie(it) }
            }
        }
    }

    private fun handleSuccessLoadMovie(details: MovieDetails) {
        movie = details.also { appBarManager.updateFavoriteState(it.isFavorite) }

        _state.value = State.Loaded(details)
    }

    private fun handleErrorLoadMovie(e: Throwable) {
        when (e) {
            is IOException -> handleIOException()
            is HttpException -> handleHttpException(e)
            else -> handleUnexpectedError()
        }
    }

    private fun handleIOException() {
        _state.value = State.Error(TypeError.InternetConnectionError)
    }

    private fun handleHttpException(e: HttpException) {
        when (e.code()) {
            403 -> _state.value = State.Error(TypeError.RequestLimitError)
            404 -> _state.value = State.Error(TypeError.NotFoundError)
            else -> _state.value = State.Error(TypeError.ServerConnectionError)
        }
    }

    private fun handleUnexpectedError() {
        _state.value = State.Error(TypeError.UnexpectedError)
    }

    private suspend fun tryLoadDetails(movieId: Int) = getMovieDetail(movieId)

    fun onAppBarScrolled(collapseRatio: Float) {
        if (collapseRatio > 0.25f) appBarManager.showAppBar() else appBarManager.hideAppBar()
        appBarManager.updateAlphaAppBar(collapseRatio)
        appBarManager.updateTitleAppBar(if (collapseRatio > 0.75f) movie?.title ?: "" else "")
    }

    fun onFavoriteBtnClicked() = toggleFavorite()

    private fun toggleFavorite() {
        movie?.let {
            viewModelScope.launch {
                if (toggleFavorite(it.id)) addFavorite()
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
        data class Error(val error: TypeError) : State
        data class Loaded(val movieDetails: MovieDetails) : State
    }

    sealed interface Action {
        data class ShareMovie(val movie: MovieDetails) : Action
        data class DownloadMovie(val movie: MovieDetails) : Action
        object RemoveFavorite : Action
        object AddFavorite : Action

        object Release: Action
    }

    sealed interface TypeError {
        object ServerConnectionError : TypeError
        object NotFoundError : TypeError
        object RequestLimitError : TypeError
        object InternetConnectionError : TypeError

        object UnexpectedError: TypeError
    }

}