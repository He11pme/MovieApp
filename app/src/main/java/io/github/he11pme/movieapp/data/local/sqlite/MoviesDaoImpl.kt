package io.github.he11pme.movieapp.data.local.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import io.github.he11pme.movieapp.data.network.dto.MovieDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesDaoImpl @Inject constructor(databaseHelper: DatabaseHelper) : MoviesDao {

    private val sqlDb = databaseHelper.readableDatabase

    override fun addMovie(movie: MovieEntity) {

        var oldValuePopular = false
        var oldValueNowPlaying = false

        val cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_ID} = ${movie.id}",
            null
        )
        cursor.use {
            if (cursor.moveToFirst()) {
                oldValuePopular = cursor.getInt(6) == 1
                oldValueNowPlaying = cursor.getInt(7) == 1
            }
        }

        val cv = createContentValues(movie, oldValuePopular, oldValueNowPlaying)

        sqlDb.insertWithOnConflict(
            DatabaseHelper.TABLE_NAME,
            null,
            cv,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    override fun addMovies(movies: List<MovieEntity>) {
        movies.forEach { addMovie(it) }
    }

    override fun removeMovie(id: Int) {
        sqlDb.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=" + "$id", null)
    }

    private fun createContentValues(
        movie: MovieEntity,
        oldValuePopular: Boolean,
        oldValueNowPlaying: Boolean
    ): ContentValues = ContentValues().apply {
        put(DatabaseHelper.COLUMN_ID, movie.id)
        put(DatabaseHelper.COLUMN_TITLE, movie.title)
        put(DatabaseHelper.COLUMN_POSTER, movie.posterPath)
        put(DatabaseHelper.COLUMN_DESCRIPTION, movie.overview)
        put(DatabaseHelper.COLUMN_RATING, movie.vote)
        put(DatabaseHelper.COLUMN_GENRES, movie.genreIds.joinToString(","))
        put(
            DatabaseHelper.COLUMN_IS_POPULAR,
            if (movie.isPopular || oldValuePopular) 1 else 0
        )
        put(
            DatabaseHelper.COLUMN_IS_NOW_PLAYING,
            if (movie.isNowPlaying || oldValueNowPlaying) 1 else 0
        )
    }

    override fun getAllMovies(): List<MovieDTO> {
        val cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME}",
            null
        )

        return getMoviesFromDatabase(cursor)
    }

    override fun getPopularMovies(): List<MovieDTO> {
        val cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_IS_POPULAR} = ?",
            arrayOf("1")
        )

        return getMoviesFromDatabase(cursor)
    }

    override fun getNowPlayingMovies(): List<MovieDTO> {
        val cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_IS_NOW_PLAYING} = ?",
            arrayOf("1")
        )

        return getMoviesFromDatabase(cursor)
    }

    override fun getMoviesByGenres(genres: List<Int>): List<MovieDTO> {
        val cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_GENRES} = ?",
            arrayOf(genres.joinToString(","))
        )

        return getMoviesFromDatabase(cursor)
    }

    private fun getMoviesFromDatabase(cursor: Cursor): List<MovieDTO> {
        val result = mutableListOf<MovieDTO>()

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(0)
                    val title = cursor.getString(1)
                    val poster = cursor.getString(2)
                    val description = cursor.getString(3)
                    val rating = cursor.getDouble(4)

                    result.add(
                        MovieDTO(
                            id = id,
                            title = title,
                            posterPath = poster,
                            overview = description,
                            vote = rating
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        return result
    }
}