package com.towerowl.workin

import android.app.Activity
import android.app.Application
import com.towerowl.workin.dagger.components.AppComponent
import com.towerowl.workin.dagger.components.DaggerAppComponent
import com.towerowl.workin.dagger.modules.ContextModule
import com.towerowl.workin.data.WorkSessionRepository

class App : Application() {

    lateinit var appComponent : AppComponent
        private set
    
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {
        fun get(activity : Activity) : App = activity.application as App
    }

}