package io.github.he11pme.movieapp.fragments.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.AppStartViewModel
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.utils.AnimationHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    @Inject
    lateinit var animationHelper: AnimationHelper
    private val appStartViewModel: AppStartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onResume() {
        super.onResume()
        appStartViewModel.appBarManager.lockAppBar()

        appWaitUntilReady()
    }

    private fun appWaitUntilReady() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appStartViewModel.isReady.collect {
                    if (it) {
                        animationHelper.source = AnimationHelper.NavigationSource.SPLASH
                        findNavController().navigate(R.id.navigateFromSplashScreenFragmentToHomeFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        appStartViewModel.appBarManager.unlockAppBar()
    }
}