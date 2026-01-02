package io.github.he11pme.movieapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import io.github.he11pme.movieapp.databinding.ActivityMainBinding
import io.github.he11pme.movieapp.utils.extensions.doOnApplyWindowInsets

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        configureSystemBars()
        setInsets()

        setContentView(binding.root)
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
}