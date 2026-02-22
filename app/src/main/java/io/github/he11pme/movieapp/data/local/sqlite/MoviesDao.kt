package io.github.he11pme.movieapp.data.local.sqlite

import io.github.he11pme.movieapp.data.network.dto.MovieDTO
import javax.inject.Singleton

@Singleton
interface MoviesDao {

    fun addMovie(movie: MovieEntity)
    fun addMovies(movies: List<MovieEntity>)
    fun removeMovie(id: Int)
    fun removePopularMovies()
    fun removeNowPlayingMovies()
    fun getAllMovies(): List<MovieDTO>
    fun getPopularMovies(): List<MovieDTO>
    fun getNowPlayingMovies(): List<MovieDTO>
    fun getMoviesByGenres(genres: List<Int>): List<MovieDTO>

}