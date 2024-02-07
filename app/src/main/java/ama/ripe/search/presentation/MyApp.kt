package ama.ripe.search.presentation

import ama.ripe.search.di.DaggerApplicationComponent
import android.app.Application


class MyApp : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}