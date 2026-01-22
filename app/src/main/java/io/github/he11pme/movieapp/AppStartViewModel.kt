package io.github.he11pme.movieapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.he11pme.movieapp.managers.AppBarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppStartViewModel @Inject constructor(
    val appBarManager: AppBarManager
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady get() = _isReady.asStateFlow()

    init {
        appIsReady()
    }

    fun appIsReady() {
        viewModelScope.launch {
            _isReady.emit(true)
        }
    }

}