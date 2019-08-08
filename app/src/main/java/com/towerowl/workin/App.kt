package com.towerowl.workin

import android.app.Activity
import android.app.Application
import com.towerowl.workin.dagger.components.AppComponent
import com.towerowl.workin.dagger.components.DaggerAppComponent
import com.towerowl.workin.dagger.modules.ContextModule
import com.towerowl.workin.data.WorkSessionRepository

class App : Application() {

    lateinit var contextModule : ContextModule
        private set
    lateinit var appComponent : AppComponent
        private set
    lateinit var workSessionRepo : WorkSessionRepository
        private set

    override fun onCreate() {
        super.onCreate()

        contextModule = ContextModule(this)

        appComponent = DaggerAppComponent.builder()
            .contextModule(contextModule)
            .build()

        workSessionRepo = WorkSessionRepository(appComponent.database()
            .workSessionDao())
    }

    companion object {
        fun get(activity : Activity) : App = activity.application as App
    }

}