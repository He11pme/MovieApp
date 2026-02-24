package io.github.he11pme.movieapp.data.repository

import io.github.he11pme.movieapp.data.local.preference.PreferenceProvider
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val preferences: PreferenceProvider
) : PreferenceRepository {
    override fun saveDefaultTheme(theme: Int) {
        preferences.saveDefaultTheme(theme)
    }

    override fun getDefaultTheme(): Int {
        return preferences.getDefaultTheme()
    }

}