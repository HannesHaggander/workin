package com.towerowl.workin

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Intent
import com.towerowl.workin.dagger.components.AppComponent
import com.towerowl.workin.dagger.components.DaggerAppComponent
import com.towerowl.workin.dagger.modules.ContextModule
import com.towerowl.workin.data.WorkSessionRepository
import com.towerowl.workin.events.NetworkObserverCallback
import com.towerowl.workin.services.NetworkObserverService

class App : Application() {

    lateinit var appComponent : AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        startService(Intent(this, NetworkObserverService::class.java))
    }

    companion object {
        fun get(activity : Activity) : App = activity.application as App
        fun get(service : Service) : App = service.application as App
    }

}