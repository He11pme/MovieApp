package io.github.he11pme.movieapp.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.databinding.FragmentSettingsBinding
import io.github.he11pme.movieapp.utils.AnimationHelper
import io.github.he11pme.movieapp.view.model.ThemeMode
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var animationHelper: AnimationHelper
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        bindToViewModel()
        setupViews()

        return binding.root

    }

    private fun bindToViewModel() {
        binding.viewModel = viewModel
    }

    private fun setupViews() {
        binding.toggleThemeMode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            toggleThemeModeListener(isChecked, checkedId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationHelper.currentAnimation?.invoke(binding.root, requireActivity())
    }

    private fun toggleThemeModeListener(isChecked: Boolean, checkedId: Int) {
        if (!isChecked) return

        val theme = mapIdToTheme(checkedId)
        viewModel.saveDefaultTheme(theme.nightMode)
    }

    private fun mapIdToTheme(@IdRes checkedId: Int): ThemeMode {
        return when (checkedId) {
            R.id.toggleButtonLight -> ThemeMode.LIGHT
            R.id.toggleButtonSystem -> ThemeMode.SYSTEM
            R.id.toggleButtonNight -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }
}