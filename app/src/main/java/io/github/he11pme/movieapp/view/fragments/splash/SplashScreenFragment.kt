package io.github.he11pme.movieapp.view.fragments.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.he11pme.movieapp.R
import io.github.he11pme.movieapp.utils.AnimationHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    @Inject
    lateinit var animationHelper: AnimationHelper
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onResume() {
        super.onResume()
        splashViewModel.appBarManager.lockAppBar()

        appWaitUntilReady()
    }

    private fun appWaitUntilReady() {
        viewLifecycleOwner.lifecycleScope.launch {
            splashViewModel.isReady.collect {
                if (it) {
                    animationHelper.source = AnimationHelper.NavigationSource.SPLASH
                    findNavController().navigate(R.id.navigateFromSplashScreenFragmentToHomeFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        splashViewModel.appBarManager.unlockAppBar()
    }
}