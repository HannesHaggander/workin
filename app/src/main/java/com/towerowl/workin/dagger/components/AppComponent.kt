package com.towerowl.workin.dagger.components

import com.towerowl.workin.dagger.modules.ContextModule
import com.towerowl.workin.dagger.modules.RoomModule
import com.towerowl.workin.data.AppRoom
import com.towerowl.workin.data.WorkSessionRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class, ContextModule::class])
interface AppComponent {
    fun database() : AppRoom
}