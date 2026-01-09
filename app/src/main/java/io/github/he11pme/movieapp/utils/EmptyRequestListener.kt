package io.github.he11pme.movieapp.utils

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * A flexible Glide [RequestListener] implementation with default no-op callbacks.
 *
 * This class allows you to handle image load success and failure events with either full callbacks
 * (provide all parameters from Glide) or simplified callbacks without parameters.
 */
class EmptyRequestListener<T> : RequestListener<T> {

    var doLoadFailed: (
        e: GlideException?,
        model: Any?,
        target: Target<T?>?,
        isFirstResource: Boolean
    ) -> Boolean = { e, model, target, isFirstResource ->
        doSimpleLoadFailed()
        false
    }

    var doSimpleLoadFailed: () -> Unit = {  }

    var doResourceReady: (
        resource: T?,
        model: Any?,
        target: Target<T?>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ) -> Boolean = { resource, model, target, dataSource, isFirstResource ->
        doSimpleResourceReady()
        false
    }

    var doSimpleResourceReady: () -> Unit = { }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<T?>?,
        isFirstResource: Boolean
    ): Boolean {
        return doLoadFailed(e, model, target, isFirstResource)
    }

    override fun onResourceReady(
        resource: T?,
        model: Any?,
        target: Target<T?>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        return doResourceReady(resource, model, target, dataSource, isFirstResource)
    }
}