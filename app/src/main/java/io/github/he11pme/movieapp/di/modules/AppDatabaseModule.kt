package io.github.he11pme.movieapp.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.he11pme.movieapp.data.local.room.AppDatabase
import io.github.he11pme.movieapp.data.local.room.dao.FavoriteMoviesDao
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideFavoriteMovieDao(database: AppDatabase): FavoriteMoviesDao {
        return database.favoriteMoviesDao
    }


}