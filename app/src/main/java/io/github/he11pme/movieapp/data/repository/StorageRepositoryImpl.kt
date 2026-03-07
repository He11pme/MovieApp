package io.github.he11pme.movieapp.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.he11pme.movieapp.view.fragments.detail.MovieSave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    suspend fun saveMovieScopedStorage(movie: MovieSave): Result<Unit> {

        return saveMovieToStorage {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, movie.filename)
                put(MediaStore.Images.Media.MIME_TYPE, movie.mimeType)
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    movie.path
                )
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            uri?.let {
                resolver.openOutputStream(uri)?.use {
                    movie.poster.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        it
                    )
                }
            }
        }
    }

    suspend fun saveMovieLegacyStorage(movie: MovieSave): Result<Unit> {

        return saveMovieToStorage {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                movie.poster,
                movie.filename,
                movie.description
            )
        }

    }

    private suspend fun saveMovieToStorage(saving: () -> Unit): Result<Unit> {

        return try {
            withContext(Dispatchers.IO) {
                saving()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}