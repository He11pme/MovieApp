package io.github.he11pme.movieapp.view.fragments.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.data.repository.AppRepository
import io.github.he11pme.movieapp.data.repository.SearchRepository
import io.github.he11pme.movieapp.domain.models.SelectionType
import io.github.he11pme.movieapp.view.mappers.toUi
import io.github.he11pme.movieapp.view.model.MovieUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow<List<MovieUi>>(emptyList())
    val searchState = _searchState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    init {
        viewModelScope.launch {
            query
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { title -> searchMovie(title) }
                .collect { movies -> _searchState.value = movies }
        }
    }

    private fun searchMovie(title: String) = flow {
        searchRepository.findMovie(title).apply {
            onSuccess {
                if (it.isNotEmpty()) {
                    emit(it.map { movie -> movie.toUi() })
                    return@onSuccess
                }
                emit(getPopularMovies())
            }
            onFailure { e ->
                emit(emptyList())
            }
        }

    }

    private suspend fun getPopularMovies(): List<MovieUi> {
        return appRepository.getSelectionMovies(SelectionType.Popular).fold(
            onSuccess = { it.map { movie -> movie.toUi() } },
            onFailure = { emptyList() }
        )
    }

    fun onQueryChanged(text: String) {
        Log.d("SEARCH", "query: $text")
        _query.value = text
    }


}