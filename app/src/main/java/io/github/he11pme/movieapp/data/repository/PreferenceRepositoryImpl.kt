package io.github.he11pme.movieapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferenceRepository {

    private val preference: SharedPreferences =
        context.getSharedPreferences(FILE_NAME_SETTING, Context.MODE_PRIVATE)

    init {
        if (preference.getBoolean(KEY_FIRST_LAUNCH, true)) {
            preference.edit { putInt(KEY_DEFAULT_THEME, DEFAULT_THEME) }
            preference.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
    }

    override fun saveDefaultTheme(theme: Int) {
        preference.edit { putInt(KEY_DEFAULT_THEME, theme) }
    }

    override fun getDefaultTheme(): Int {
        return preference.getInt(
            KEY_DEFAULT_THEME,
            DEFAULT_THEME
        )
    }

    override fun saveLastLoadTimeToCache(time: Long) {
        preference.edit { putLong(KEY_LAST_LOAD_TIME, time) }
    }

    override fun getDefaultLastLoadTimeToCache(): Long {
        return preference.getLong(KEY_LAST_LOAD_TIME, Long.MAX_VALUE)
    }

    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_THEME = "default_theme"
        private const val KEY_LAST_LOAD_TIME = "last_load_time"
        private const val FILE_NAME_SETTING = "app_settings"
        private const val DEFAULT_THEME = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

}