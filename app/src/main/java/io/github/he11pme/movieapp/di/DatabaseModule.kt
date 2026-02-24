package io.github.he11pme.movieapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.he11pme.movieapp.data.local.sqlite.MoviesDao
import io.github.he11pme.movieapp.data.local.sqlite.MoviesDaoImpl

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    @Binds
    fun bindMovieDao(impl: MoviesDaoImpl): MoviesDao

}