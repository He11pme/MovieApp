package io.github.he11pme.movieapp.di.modules

import dagger.Module

@Module(includes = [AppDatabaseModule::class, RepositoryModule::class])
class AppModule