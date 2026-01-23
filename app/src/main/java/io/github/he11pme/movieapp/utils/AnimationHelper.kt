package io.github.he11pme.movieapp.utils

import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.math.hypot
import kotlin.math.roundToInt

@ActivityRetainedScoped
class AnimationHelper @Inject constructor() {
    private val menuItems = 4
    var source: NavigationSource? = null
    var position: Int = 1
        set(value) {
            field = if (value in 0..<menuItems) value else 0
        }

    val currentAnimation: ((View, Activity) -> Unit)?
        get() {
            val currentSource = source
            source = null

            return when (currentSource) {
                NavigationSource.SPLASH -> ::performFragmentCircularRevealAnimation

                NavigationSource.BOTTOM_NAV -> { rootView: View, activity: Activity ->
                    performFragmentCircularRevealAnimation(
                        rootView,
                        activity
                    ) { startAnimationFromMenuItem(it, position) }
                }

                null -> null
            }

        }

    fun performFragmentCircularRevealAnimation(
        rootView: View,
        activity: Activity,
        fromStartAnimation: (View) -> Pair<Int, Int> = ::startAnimationFromCenterRootView
    ) {

        Executors.newSingleThreadExecutor().execute {
            while (true) {
                if (rootView.isAttachedToWindow) {
                    activity.runOnUiThread {
                        val (x, y) = fromStartAnimation(rootView)
                        val startRadius = 0f
                        val endRadius =
                            hypot(rootView.width.toDouble(), rootView.height.toDouble()).toFloat()

                        ViewAnimationUtils.createCircularReveal(
                            rootView,
                            x,
                            y,
                            startRadius,
                            endRadius
                        ).apply {
                            duration = 500
                            interpolator = AccelerateDecelerateInterpolator()
                            start()
                        }
                    }
                    return@execute
                }
            }
        }


    }

    fun startAnimationFromCenterRootView(rootView: View): Pair<Int, Int> {
        return Pair(
            rootView.width / 2,
            rootView.height / 2
        )
    }

    fun startAnimationFromMenuItem(rootView: View, position: Int): Pair<Int, Int> {
        val itemCenter = rootView.width / (menuItems * 2)
        val x = (itemCenter * 2) * position + itemCenter
        return Pair(x, rootView.y.roundToInt() + rootView.height)
    }

    enum class NavigationSource {
        SPLASH, BOTTOM_NAV
    }

}