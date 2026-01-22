package io.github.he11pme.movieapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.databinding.ActivityMainBinding
import io.github.he11pme.movieapp.fragments.detail.DetailInfoFragment
import io.github.he11pme.movieapp.fragments.search.SearchViewModel
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

    private val navControllerSearch by lazy {
        (supportFragmentManager.findFragmentById(R.id.searchFragmentContainer) as NavHostFragment).navController
    }
    private val searchViewModel: SearchViewModel by viewModels()
    private var currentMenuRes: Int = 0
    private var currentMenuProvider: MenuProvider? = null
    private var favoriteItemMenu: MenuItem? = null

    private var backPressed = 0L


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            // Disables the default exit animation between the system SplashScreen
            // and the app's custom SplashScreen
            setOnExitAnimationListener { splashScreenViewProvider ->
                splashScreenViewProvider.remove()
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        handleBack()

        setSupportActionBar(binding.toolbar)
        bindToAppBarManager()

        configureSystemBars()
        setInsets()
        setupViews()
        observeDestinationChanges()

        setContentView(binding.root)
    }

    private fun handleBack() {
        onBackPressedDispatcher.addCallback(this) {
            if (hideSearchView()) return@addCallback

            doubleClickForExit()
        }
    }

    private fun doubleClickForExit() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) finish()
            else Snackbar.make(
                binding.contentContainer,
                getString(R.string.double_tap_for_exit),
                Snackbar.LENGTH_SHORT
            ).show()

            backPressed = System.currentTimeMillis()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun hideSearchView(): Boolean {
        if (binding.searchView.isShowing) {
            binding.searchView.hide()
            return true
        }
        return false
    }

    private fun bindToAppBarManager() {
        bindAppBarState()
        bindMenuState()
        bindMenuActions()
    }

    private fun bindAppBarState() {
        collectState { appBarManager.appBarState.collect(::handleAppBarState) }
    }

    private fun collectState(doCollect: suspend () -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                doCollect()
            }
        }
    }

    private fun handleAppBarState(appBarState: AppBarManager.AppBarState) {
        binding.appBar.apply {
            visibility = appBarState.visibilityAppBar
            alpha = appBarState.alphaAppBar
        }

        binding.toolbar.apply {
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

    private fun bindMenuState() {

        collectState { appBarManager.menuAppBarState.collect(::handleMenuState) }

    }

    private fun handleMenuState(menuState: AppBarManager.MenuAppBarState) {
        if (menuState.menuAppBar != currentMenuRes) {
            currentMenuRes = menuState.menuAppBar
            setupMenu(menuState.menuAppBar)
        }

        favoriteItemMenu?.let { toggleFavoriteIcon(menuState.isFavorite, it) }
    }

    private fun bindMenuActions() {
        collectState { appBarManager.menuActions.collect(::handleMenuActions) }
    }

    private fun handleMenuActions(action: AppBarManager.MenuAction) {
        when (action) {
            AppBarManager.MenuAction.ToProfileClicked -> {
                navController.navigate(R.id.profileFragment)
            }

            else -> {}
        }
    }

    private fun toggleFavoriteIcon(isFavorite: Boolean, item: MenuItem) {
        item.setIcon(
            if (isFavorite) DetailInfoFragment.ID_DRAWABLE_FAVORITE
            else DetailInfoFragment.ID_DRAWABLE_UNFAVORITE
        )
    }

    private fun updateHeightToolbar() {
        binding.appBar.post { appBarManager.updateHeightToolbar(binding.appBar.height) }
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

        binding.appBar.doOnApplyWindowInsets { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            updateHeightToolbar()
        }
    }

    private fun setupViews() {
        setupToolbar()
        setupBottomNavigation()
        setupSearchView()
    }

    private fun setupSearchView() {
        fun tryHideDetailInfoFragment() {
            if (navControllerSearch.currentDestination?.id == R.id.detailInfoFragment) {
                navControllerSearch.popBackStack(R.id.searchFragment, false)
            }
        }

        binding.searchView.editText.setOnClickListener { tryHideDetailInfoFragment() }

        binding.searchView.editText.doOnTextChanged { s: CharSequence?, _, _, _ ->
            searchViewModel.onQueryChanged(s.toString())
            tryHideDetailInfoFragment()
        }
    }

    private fun setupToolbar() {
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
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
                R.id.homeFragment -> appBarManager.setSearchBar()
                R.id.detailInfoFragment, R.id.splashScreenFragment -> appBarManager.setPosterBar()
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

    companion object {
        const val TIME_INTERVAL = 2000
    }

}