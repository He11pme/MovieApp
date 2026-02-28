package io.github.he11pme.movieapp.domain.repository

import javax.inject.Singleton

@Singleton
interface PreferenceRepository {

    fun saveDefaultTheme(theme: Int)
    fun getDefaultTheme(): Int

    fun saveLastLoadTimeToCache(time: Long)

    fun getDefaultLastLoadTimeToCache(): Long

}