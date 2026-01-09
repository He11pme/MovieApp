package io.github.he11pme.movieapp.managers

import android.util.Log
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.github.he11pme.movieapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityRetainedScoped
class AppBarManager @Inject constructor() {

    private val _appBarState = MutableStateFlow(AppBarState())
    val appBarState = _appBarState.asStateFlow()

    private val _menuActions = MutableSharedFlow<MenuAction>()

    val menuActions: Flow<MenuAction> get() = _menuActions
    private var isFixHeightToolbar = false

    fun setDefaultBar(title: CharSequence) {
        _appBarState.update {
            it.copy(
                visibilityAppBar = View.VISIBLE,
                alphaAppBar = 1f,
                menuAppBar = R.menu.app_bar_menu,
                visibilitySearchBar = View.GONE,
                titleToolbar = title.toString(),
                visibilityBottomAppBar = View.VISIBLE,
                scrollingViewBehavior = AppBarLayout.ScrollingViewBehavior()
            )
        }
    }

    fun setSearchBar(title: CharSequence = "") {
        setDefaultBar(title)
        _appBarState.update {
            it.copy(
                visibilitySearchBar = View.VISIBLE,
            )
        }
    }

    fun setPosterBar(title: CharSequence = "") {
        setDefaultBar(title)
        _appBarState.update {
            it.copy(
                visibilityAppBar = View.GONE,
                alphaAppBar = 0f,
                menuAppBar = R.menu.detail_info_menu,
                visibilitySearchBar = View.GONE,
                visibilityBottomAppBar = View.GONE,
                scrollingViewBehavior = null
            )
        }
    }

    fun setProfileBar(title: CharSequence = "") {
        setDefaultBar(title)
        _appBarState.update {
            it.copy(
                visibilityBottomAppBar = View.GONE
            )
        }
    }

    suspend fun dispatchMenuAction(action: MenuAction) {
        _menuActions.emit(action)
    }

    fun fixHeightToolbar(height: Int) {
        if (isFixHeightToolbar) Log.e(TAG, "Height of the toolbar is already fixed")
        else {
            _appBarState.update { it.copy(heightToolbar = height) }
            isFixHeightToolbar = true
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

    data class AppBarState(
        val visibilityAppBar: Int = View.VISIBLE,
        val alphaAppBar: Float = 1f,
        val menuAppBar: Int = R.menu.app_bar_menu,

        val titleToolbar: String = "",
        val heightToolbar: Int? = null,

        val visibilitySearchBar: Int = View.VISIBLE,

        val visibilityBottomAppBar: Int = View.VISIBLE,

        val scrollingViewBehavior: AppBarLayout.ScrollingViewBehavior? = AppBarLayout.ScrollingViewBehavior()
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