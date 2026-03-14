package io.github.he11pme.movieapp.view.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class DefaultFragment: Fragment() {

    /**
     * Extension for Flow that safely collects values respecting the Fragment lifecycle.
     *
     * The collection starts when the viewLifecycleOwner lifecycle
     * is at least in the STARTED state and automatically stops
     * when the lifecycle falls below STARTED.
     *
     * @param collector a function that is called every time
     * the StateFlow emits a new value.
     */
    protected fun <T> Flow<T>.collectWithLifecycle(collector: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@collectWithLifecycle.collect {
                    collector(it)
                }
            }
        }
    }

}