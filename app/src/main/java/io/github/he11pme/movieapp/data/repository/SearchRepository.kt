package io.github.he11pme.movieapp.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.data.network.TMDbApi
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor() {
    private val api = TMDbApi.retrofitService
    fun findMovie(title: String) = MovieSearchPagingSource(title = title, api = api)

}