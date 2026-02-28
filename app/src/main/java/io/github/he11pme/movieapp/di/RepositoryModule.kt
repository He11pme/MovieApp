package io.github.he11pme.movieapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.he11pme.movieapp.data.repository.MoviesRepositoryImpl
import io.github.he11pme.movieapp.data.repository.PreferenceRepositoryImpl
import io.github.he11pme.movieapp.domain.repository.MoviesRepository
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindPreferenceRepository(impl: PreferenceRepositoryImpl): PreferenceRepository

    @Binds
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

}