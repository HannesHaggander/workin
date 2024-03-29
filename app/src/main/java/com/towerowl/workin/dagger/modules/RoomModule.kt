package com.towerowl.workin.dagger.modules

import android.content.Context
import androidx.room.Room
import com.towerowl.workin.data.AppRoom
import com.towerowl.workin.data.SettingsRepository
import com.towerowl.workin.data.WorkSessionRepository
import com.towerowl.workin.events.WorkSessionEvent
import com.towerowl.workin.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import io.reactivex.processors.FlowableProcessor
import javax.inject.Singleton

@Module (includes = [ContextModule::class])
class RoomModule {

    private var appRoom : AppRoom? = null

    @Provides @Singleton
    fun provideRoom(context: Context) : AppRoom =
        appRoom ?:
        Room.databaseBuilder(context, AppRoom::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
            .also { appRoom = it }

    @Provides @Singleton
    fun provideWorkSessionRepository(context: Context, publishStream : FlowableProcessor<WorkSessionEvent>) =
        WorkSessionRepository(provideRoom(context).workSessionDao(), publishStream)

    @Provides @Singleton
    fun providesSetingsRepository(context: Context) =
        SettingsRepository(provideRoom(context).settingsDao())
}