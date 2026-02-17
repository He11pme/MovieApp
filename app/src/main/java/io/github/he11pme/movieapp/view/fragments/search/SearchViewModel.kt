package io.github.he11pme.movieapp.view.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.he11pme.movieapp.App
import io.github.he11pme.movieapp.domain.models.Movie
import io.github.he11pme.movieapp.domain.repository.SearchRepository
import io.github.he11pme.movieapp.view.mappers.toUi
import io.github.he11pme.movieapp.view.model.MovieUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel @Inject constructor(
//    val searchRepository: SearchRepository
) : ViewModel() {

    @Inject
    lateinit var searchRepository: SearchRepository

    init {
        App.instance.dagger.inject(this)
    }
    private val query = MutableStateFlow("")

    val searchResult: Flow<PagingData<MovieUi>> =
        query
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { title ->
                if (title.isBlank()) getPopularMovies()
                else searchMovie(title)
            }
            .cachedIn(viewModelScope)

    private fun searchMovie(title: String): Flow<PagingData<MovieUi>> =
        searchRepository.findMovie(title).map { it.toMovieUi() }

    private fun getPopularMovies(): Flow<PagingData<MovieUi>> {
        return searchRepository.getPopularMoviesPaging().map { it.toMovieUi() }
    }

    fun onQueryChanged(text: String) {
        query.value = text
    }

}

private fun PagingData<Movie>.toMovieUi(): PagingData<MovieUi> = map { it.toUi() }