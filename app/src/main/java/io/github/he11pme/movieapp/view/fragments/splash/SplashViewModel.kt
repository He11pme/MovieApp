package io.github.he11pme.movieapp.view.fragments.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.he11pme.movieapp.App
import io.github.he11pme.movieapp.managers.AppBarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
//    val appBarManager: AppBarManager
) : ViewModel() {

//    @Inject
//    lateinit var repository: AppRepository

    @Inject
    lateinit var appBarManager: AppBarManager

    init {
        App.instance.dagger.inject(this)
    }

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