package io.github.he11pme.movieapp.data.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceProvider @Inject constructor(@ApplicationContext context: Context) {

    private val preference: SharedPreferences =
        context.getSharedPreferences(FILE_NAME_SETTING, Context.MODE_PRIVATE)

    init {
        if (preference.getBoolean(KEY_FIRST_LAUNCH, true)) {
            preference.edit { putInt(KEY_DEFAULT_THEME, DEFAULT_THEME) }
            preference.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
    }

    fun saveDefaultTheme(theme: Int) {
        preference.edit { putInt(KEY_DEFAULT_THEME, theme) }
    }

    fun getDefaultTheme(): Int {
        return preference.getInt(KEY_DEFAULT_THEME, DEFAULT_THEME)
    }

    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_THEME = "default_theme"
        private const val FILE_NAME_SETTING = "app_settings"
        private const val DEFAULT_THEME = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

}