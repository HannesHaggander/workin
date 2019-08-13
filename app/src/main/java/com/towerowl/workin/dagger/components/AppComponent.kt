package com.towerowl.workin.dagger.components

import android.app.Activity
import com.towerowl.workin.dagger.modules.ContextModule
import com.towerowl.workin.dagger.modules.FlowableModule
import com.towerowl.workin.dagger.modules.RoomModule
import com.towerowl.workin.data.AppRoom
import com.towerowl.workin.data.WorkSessionRepository
import com.towerowl.workin.events.WorkSessionEvent
import dagger.Component
import io.reactivex.Flowable
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class, ContextModule::class, FlowableModule::class])
interface AppComponent {
    fun database() : AppRoom
    fun workSessionStream() : Flowable<WorkSessionEvent>
}