package com.towerowl.workin.dagger.components

import android.app.Activity
import android.app.admin.NetworkEvent
import com.towerowl.workin.dagger.modules.ContextModule
import com.towerowl.workin.dagger.modules.FlowableModule
import com.towerowl.workin.dagger.modules.RoomModule
import com.towerowl.workin.data.AppRoom
import com.towerowl.workin.data.SettingsRepository
import com.towerowl.workin.data.WorkSessionRepository
import com.towerowl.workin.events.NetworkObserverEvent
import com.towerowl.workin.events.WorkSessionEvent
import dagger.Component
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class, ContextModule::class, FlowableModule::class])
interface AppComponent {
    fun database() : AppRoom
    fun workSessionRepository() : WorkSessionRepository
    fun networkObserverPublisher() : FlowableProcessor<NetworkObserverEvent>
    fun networkObserverStream() : Flowable<NetworkObserverEvent>
    fun settingsRepository() : SettingsRepository
}

