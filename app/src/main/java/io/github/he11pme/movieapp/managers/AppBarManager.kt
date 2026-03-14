package io.github.he11pme.movieapp.managers

import android.util.Log
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityRetainedScoped
class AppBarManager @Inject constructor() {

    private val _appBarState = MutableStateFlow(AppBarState())
    val appBarState = _appBarState.asStateFlow()

    private val _menuAppBarState = MutableStateFlow(MenuAppBarState())
    val menuAppBarState = _menuAppBarState.asStateFlow()

    private val _menuActions = MutableSharedFlow<MenuAction>()
    val menuActions: SharedFlow<MenuAction> get() = _menuActions

    var isLockAppBar = false
    var bufferBarState: MutableList<(() -> Unit)> = mutableListOf()

    fun lockAppBar() {
        if (isLockAppBar) Log.e(TAG, "AppBar already is lock")
        else isLockAppBar = true
    }

    fun unlockAppBar() {
        if (isLockAppBar) {
            isLockAppBar = false
            bufferBarState.forEach { it() }
            bufferBarState.clear()
        } else Log.e(TAG, "AppBar already is unlock")
    }


    fun setDefaultBar(title: CharSequence) {
        updateAppBarState {
            it.copy(
                visibilityAppBar = View.VISIBLE,
                alphaAppBar = 1f,
                visibilitySearchBar = View.GONE,
                titleToolbar = title.toString(),
                visibilityBottomAppBar = View.VISIBLE,
                scrollingViewBehavior = AppBarLayout.ScrollingViewBehavior()
            )
        }

        _menuAppBarState.update { it.copy(
            menuAppBar = R.menu.app_bar_menu,
            isFavorite = false
        )}
    }

    fun setSearchBar(title: CharSequence = "") {
        setDefaultBar(title)
        updateAppBarState {
            it.copy(
                visibilitySearchBar = View.VISIBLE,
            )
        }
    }

    fun setPosterBar(title: CharSequence = "") {
        setDefaultBar(title)
        updateAppBarState {
            it.copy(
                visibilityAppBar = View.GONE,
                alphaAppBar = 0f,
                visibilitySearchBar = View.GONE,
                visibilityBottomAppBar = View.GONE,
                scrollingViewBehavior = null
            )
        }
        _menuAppBarState.update { it.copy(
            menuAppBar = R.menu.detail_info_menu
        )}
    }

    fun setProfileBar(title: CharSequence = "") {
        setDefaultBar(title)
        updateAppBarState {
            it.copy(
                visibilityBottomAppBar = View.GONE
            )
        }
    }

    private fun updateAppBarState(doUpdate: (AppBarState) -> AppBarState) {
        setNewStateBar { _appBarState.update(doUpdate) }
    }

    private fun setNewStateBar(doSet: () -> Unit) {
        if (isLockAppBar) bufferBarState.add(doSet)
        else doSet()
    }

    suspend fun dispatchMenuAction(action: MenuAction) {
        _menuActions.emit(action)
    }

    fun updateHeightToolbar(height: Int) {
        if (_appBarState.value.heightToolbar != height) {
            _appBarState.update { it.copy(heightToolbar = height) }
        }
    }

    fun showAppBar() {
        updateVisibleAppBar(View.VISIBLE)
    }

    fun hideAppBar() {
        updateVisibleAppBar(View.GONE)
    }

    private fun updateVisibleAppBar(visibility: Int) {
        _appBarState.update { it.copy(visibilityAppBar = visibility) }
    }

    fun updateAlphaAppBar(alpha: Float) {
        _appBarState.update { it.copy(alphaAppBar = alpha) }
    }

    fun updateTitleAppBar(title: String) {
        _appBarState.update { it.copy(titleToolbar = title) }
    }

    fun updateFavoriteState(isFavorite: Boolean) {
        _menuAppBarState.update { it.copy(isFavorite = isFavorite) }
    }

    data class AppBarState(
        val visibilityAppBar: Int = View.VISIBLE,
        val alphaAppBar: Float = 1f,

        val titleToolbar: String = "",
        val heightToolbar: Int? = null,

        val visibilitySearchBar: Int = View.VISIBLE,

        val visibilityBottomAppBar: Int = View.VISIBLE,

        // Behavior for the content placed under the AppBar
        val scrollingViewBehavior: AppBarLayout.ScrollingViewBehavior? = AppBarLayout.ScrollingViewBehavior(),

    )

    data class MenuAppBarState(
        val menuAppBar: Int = R.menu.app_bar_menu,

        // Flag indicating which favorite icon should be shown in the menu
        val isFavorite: Boolean = false
    )

    sealed interface MenuAction {
        object ToProfileClicked : MenuAction
        object FavoriteBtnClicked : MenuAction
        object DownloadBtnClicked : MenuAction
        object ShareBtnClicked : MenuAction
    }

    companion object {
        private const val TAG = "APP_BAR"
    }

}