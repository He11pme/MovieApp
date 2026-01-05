package io.github.he11pme.movieapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.databinding.ActivityMainBinding
import io.github.he11pme.movieapp.utils.extensions.doOnApplyWindowInsets

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.contentContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        fixHeightToolbar()
        configureSystemBars()
        setInsets()
        setupViews()
        observeDestinationChanges()

        setContentView(binding.root)
    }

    // Fixed height of toolbar when a SearchBar is present,
    // to prevent size changes and visual "jank"
    // during navigation between fragments
    private fun fixHeightToolbar() {
        binding.toolbar.post {
            binding.toolbar.layoutParams.height = binding.toolbar.height
        }
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

            binding.searchBar.visibility =
                if (destination.id == R.id.searchFragment) View.VISIBLE else View.GONE

            binding.bottomAppBar.visibility = when (destination.id) {
                R.id.profileFragment, R.id.detailInfoFragment -> View.GONE
                else -> View.VISIBLE
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle navigation manually to ensure the Up button works correctly
        return when (item.itemId) {
            R.id.profileFragment -> {
                navController.navigate(R.id.profileFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}