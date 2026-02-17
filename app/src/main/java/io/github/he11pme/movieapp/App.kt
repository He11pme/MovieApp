package io.github.he11pme.movieapp

import android.app.Application
import io.github.he11pme.movieapp.di.AppComponent
import io.github.he11pme.movieapp.di.DaggerAppComponent

class App: Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        dagger = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}