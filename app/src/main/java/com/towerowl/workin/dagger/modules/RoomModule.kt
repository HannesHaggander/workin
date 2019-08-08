package com.towerowl.workin.dagger.modules

import android.content.Context
import androidx.room.Room
import com.towerowl.workin.data.AppRoom
import com.towerowl.workin.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module (includes = [ContextModule::class])
class RoomModule {

    @Provides @Singleton
    fun provideRoom(context: Context) : AppRoom
            = Room.databaseBuilder(context, AppRoom::class.java, DATABASE_NAME).build()

}