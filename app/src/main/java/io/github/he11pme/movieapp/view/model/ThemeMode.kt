package io.github.he11pme.movieapp.view.model

import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode(val nightMode: Int) {

    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    DARK(AppCompatDelegate.MODE_NIGHT_YES)

}