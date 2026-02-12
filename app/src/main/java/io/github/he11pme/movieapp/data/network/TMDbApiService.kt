package io.github.he11pme.movieapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.he11pme.movieapp.BuildConfig
import io.github.he11pme.movieapp.data.network.dto.GenreResponse
import io.github.he11pme.movieapp.data.network.dto.MovieDetailsDTO
import io.github.he11pme.movieapp.data.network.dto.MovieResponse
import io.github.he11pme.movieapp.utils.extensions.getFormatLocale
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val POPULAR_MOVIE = "movie/popular"
private const val NOW_PLAYING_MOVIE = "movie/now_playing"
private const val DISCOVER_MOVIE = "discover/movie"
private const val GENRES = "genre/movie/list"

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface TMDbApiService {

    /**
     * Return the list of official genres for movies
     */
    @GET(GENRES)
    suspend fun getGenres(): GenreResponse

    /**
     * Return a list of movies ordered by popularity.
     */
    @GET(POPULAR_MOVIE)
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
    ): MovieResponse

    /**
     * Returns a list of movies that are currently in theatres
     */
    @GET(NOW_PLAYING_MOVIE)
    suspend fun getNowPlayingMovies(
        @Query("region") region: String = Locale.getDefault().country
    ): MovieResponse

    /**
     * Returns a list of movies filtered by genres
     *
     * Genres support separation by commas (,) or vertical bars (|).
     * Comma's are treated like an AND query while pipe's are treated like an OR.
     * e.g., "12,28" or "12|28"
     */
    @GET(DISCOVER_MOVIE)
    suspend fun getMoviesByGenres(
        @Query("with_genres") genres: String
    ): MovieResponse

    /**
     * Returns details of a movie by ID.
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") append: String = "credits,videos,images,similar,recommendations"
    ): MovieDetailsDTO

    @GET("search/movie")
    suspend fun findMovieByTitle(
        @Query("query") title: String = "",
        @Query("page") page: Int = 1,
        @Query("region") region: String = Locale.getDefault().country
    ): MovieResponse

}

object TMDbApi {
    val retrofitService: TMDbApiService by lazy {
        retrofit.newBuilder()
            .client(createClient())
            .build()
            .create(TMDbApiService::class.java)
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            chain.request().let { original ->
                val url = original.url.newBuilder()
                    .addQueryParameter("language", Locale.getDefault().getFormatLocale())
                    .build()

                val req = original.newBuilder()
                    .url(url)
                    .addHeader("Authorization", BuildConfig.TMDB_API_KEY)
                    .build()
                chain.proceed(req)
            }
        }.build()
    }
}
