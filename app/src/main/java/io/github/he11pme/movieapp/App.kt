package io.github.he11pme.movieapp

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private val shared by lazy { this.getSharedPreferences("app_settings", MODE_PRIVATE) }

    private val defaultTheme get() = preferenceRepository.getDefaultTheme()

    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                "default_theme" -> setNightMode()
            }
        }

    override fun onCreate() {
        super.onCreate()
        setNightMode()
        shared.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(defaultTheme)
    }

}