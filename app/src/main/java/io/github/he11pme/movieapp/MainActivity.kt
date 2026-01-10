package io.github.he11pme.movieapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.databinding.ActivityMainBinding
import io.github.he11pme.movieapp.fragments.detail.DetailInfoFragment
import io.github.he11pme.movieapp.managers.AppBarManager
import io.github.he11pme.movieapp.utils.extensions.doOnApplyWindowInsets
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var appBarManager: AppBarManager
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.contentContainer) as NavHostFragment).navController
    }

    private var currentMenuRes: Int = 0
    private var currentMenuProvider: MenuProvider? = null
    private var favoriteItemMenu: MenuItem? = null
    private var currentFlagFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        bindToAppBarManager()
        fixHeightToolbar()
        configureSystemBars()
        setInsets()
        setupViews()
        observeDestinationChanges()

        setContentView(binding.root)
    }

    private fun bindToAppBarManager() {
        bindAppBarState()
        bindMenuActions()
    }

    private fun bindAppBarState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appBarManager.appBarState.collect(::handleAppBarState)
            }
        }
    }

    private fun bindMenuActions() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appBarManager.menuActions.collect(::handleMenuActions)
            }
        }
    }

    private fun handleMenuActions(action: AppBarManager.MenuAction) {
        when (action) {
            AppBarManager.MenuAction.ToProfileClicked -> {
                navController.navigate(R.id.profileFragment)
            }

            else -> {}
        }
    }

    private fun handleAppBarState(appBarState: AppBarManager.AppBarState) {
        binding.appBar.apply {
            visibility = appBarState.visibilityAppBar
            alpha = appBarState.alphaAppBar
        }

        if (appBarState.menuAppBar != currentMenuRes) {
            currentMenuRes = appBarState.menuAppBar
            setupMenu(appBarState.menuAppBar)
        }

        favoriteItemMenu?.let { toggleFavoriteMenu(appBarState.isFavorite, it) }

        binding.toolbar.apply {
            appBarState.heightToolbar?.let { layoutParams.height = it }
            title = appBarState.titleToolbar
        }
        binding.searchBar.apply {
            visibility = appBarState.visibilitySearchBar
        }
        binding.bottomAppBar.apply {
            visibility = appBarState.visibilityBottomAppBar
        }
        (binding.contentContainer.layoutParams as CoordinatorLayout.LayoutParams).behavior =
            appBarState.scrollingViewBehavior
    }

    private fun toggleFavoriteMenu(isFavorite: Boolean, item: MenuItem) {
        if (isFavorite == currentFlagFavorite) return

        item.setIcon(
            if (isFavorite) DetailInfoFragment.ID_DRAWABLE_FAVORITE
            else DetailInfoFragment.ID_DRAWABLE_UNFAVORITE
        )

        currentFlagFavorite = !currentFlagFavorite
    }

    // Fixed height of toolbar when a SearchBar is present,
    // to prevent size changes and visual "jank"
    // during navigation between fragments
    private fun fixHeightToolbar() {
        binding.toolbar.post { appBarManager.fixHeightToolbar(binding.toolbar.height) }
    }

    private fun configureSystemBars() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            binding.bottomNavigationView.background = null
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            @Suppress("DEPRECATION")
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setInsets() {
        binding.bottomAppBar.doOnApplyWindowInsets { v, _ ->
            v.updatePadding(bottom = 0)
        }

        binding.toolbar.doOnApplyWindowInsets { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
        }
    }

    private fun setupViews() {
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.searchFragment,
                R.id.favoritesFragment,
                R.id.randomFragment,
                R.id.settingsFragment
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfig)

    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun observeDestinationChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.searchFragment -> appBarManager.setSearchBar()
                R.id.detailInfoFragment -> appBarManager.setPosterBar()
                R.id.profileFragment -> appBarManager.setProfileBar(destination.label ?: "")
                else -> appBarManager.setDefaultBar(destination.label ?: "")
            }

        }
    }

    private fun setupMenu(@MenuRes menuRes: Int) {
        currentMenuProvider?.let { removeMenuProvider(it) }

        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(menuRes, menu)

                favoriteItemMenu = menu.findItem(R.id.favoriteBtn)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.profileFragment -> {
                        dispatchMenuAction(AppBarManager.MenuAction.ToProfileClicked)
                    }

                    R.id.shareBtn -> {
                        dispatchMenuAction(AppBarManager.MenuAction.ShareBtnClicked)
                    }

                    R.id.downloadBtn -> {
                        dispatchMenuAction(AppBarManager.MenuAction.DownloadBtnClicked)
                    }

                    R.id.favoriteBtn -> {
                        dispatchMenuAction(AppBarManager.MenuAction.FavoriteBtnClicked)
                    }

                    else -> false
                }
            }

            private fun dispatchMenuAction(action: AppBarManager.MenuAction): Boolean {
                lifecycleScope.launch { appBarManager.dispatchMenuAction(action) }
                return true
            }

        }

        currentMenuProvider = menuProvider
        addMenuProvider(menuProvider, this, Lifecycle.State.RESUMED)
    }

}