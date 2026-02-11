package io.github.he11pme.movieapp.view.fragments.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.data.repository.AppRepository
import io.github.he11pme.movieapp.data.repository.SearchRepository
import io.github.he11pme.movieapp.domain.models.SelectionType
import io.github.he11pme.movieapp.view.mappers.toUi
import io.github.he11pme.movieapp.view.model.MovieUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val searchResult: Flow<PagingData<MovieUi>> =
        query
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { title ->
                if (title.isBlank()) popularPagingData()
                else searchMovie(title)
            }
            .cachedIn(viewModelScope)


    private fun searchMovie(title: String): Flow<PagingData<MovieUi>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { searchRepository.findMovie(title) }
    ).flow.map { it.map { movie -> movie.toUi() } }

    private suspend fun getPopularMovies(): List<MovieUi> {
        return appRepository.getSelectionMovies(SelectionType.Popular).fold(
            onSuccess = { it.map { movie -> movie.toUi() } },
            onFailure = { emptyList() }
        )
    }

    private suspend fun popularPagingData(): Flow<PagingData<MovieUi>> =
        flowOf(PagingData.from(getPopularMovies()))

    fun onQueryChanged(text: String) {
        Log.d("SEARCH", "query: $text")
        _query.value = text
    }


}