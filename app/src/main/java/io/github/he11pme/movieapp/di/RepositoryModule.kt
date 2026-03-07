package io.github.he11pme.movieapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.he11pme.movieapp.data.repository.FavoriteRepositoryImpl
import io.github.he11pme.movieapp.data.repository.MovieDetailRepositoryImpl
import io.github.he11pme.movieapp.data.repository.MoviesRepositoryImpl
import io.github.he11pme.movieapp.data.repository.PreferenceRepositoryImpl
import io.github.he11pme.movieapp.data.repository.SelectionRepositoryImpl
import io.github.he11pme.movieapp.data.repository.StorageRepositoryImpl
import io.github.he11pme.movieapp.domain.repository.FavoriteRepository
import io.github.he11pme.movieapp.domain.repository.MovieDetailRepository
import io.github.he11pme.movieapp.domain.repository.MoviesRepository
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository
import io.github.he11pme.movieapp.domain.repository.SelectionRepository
import io.github.he11pme.movieapp.domain.repository.StorageRepository

// Used by Hilt (Dagger) through annotation processing.
// The "unused" warning is a false positive.
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindPreferenceRepository(impl: PreferenceRepositoryImpl): PreferenceRepository

    @Binds
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    fun bindDetailRepository(impl: MovieDetailRepositoryImpl): MovieDetailRepository

    @Binds
    fun bindSelectionRepository(impl: SelectionRepositoryImpl): SelectionRepository

    @Binds
    fun bindStorageRepository(impl: StorageRepositoryImpl): StorageRepository

}