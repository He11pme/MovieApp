package io.github.he11pme.movieapp.view.fragments.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.domain.repository.PreferenceRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    fun saveDefaultTheme(theme: Int) {
        preferenceRepository.saveDefaultTheme(theme)
    }

    fun getDefaultTheme(): Int {
        return preferenceRepository.getDefaultTheme()
    }

}