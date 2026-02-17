package io.github.he11pme.movieapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.he11pme.movieapp.di.modules.AppModule
import io.github.he11pme.movieapp.view.MainActivity
import io.github.he11pme.movieapp.view.fragments.detail.DetailInfoViewModel
import io.github.he11pme.movieapp.view.fragments.favorites.FavoritesFragment
import io.github.he11pme.movieapp.view.fragments.favorites.FavoritesViewModel
import io.github.he11pme.movieapp.view.fragments.home.HomeFragment
import io.github.he11pme.movieapp.view.fragments.home.HomeViewModel
import io.github.he11pme.movieapp.view.fragments.search.SearchViewModel
import io.github.he11pme.movieapp.view.fragments.splash.SplashScreenFragment
import io.github.he11pme.movieapp.view.fragments.splash.SplashViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(viewModel: SearchViewModel)
    fun inject(viewModel: DetailInfoViewModel)
    fun inject(viewModel: FavoritesViewModel)
    fun inject(viewModel: HomeViewModel)
    fun inject(viewModel: SplashViewModel)
    fun inject(fragment: SplashScreenFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: FavoritesFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

}