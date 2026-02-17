package io.github.he11pme.movieapp.di.modules

import dagger.Binds
import dagger.Module
import io.github.he11pme.movieapp.data.repository.SearchRepositoryImpl
import io.github.he11pme.movieapp.domain.repository.SearchRepository

@Module
interface RepositoryModule {

    @Binds
    fun bindRepository(searchRepositoryImpl: SearchRepositoryImpl) : SearchRepository

}