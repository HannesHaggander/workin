package com.towerowl.workin.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule
constructor(private val context : Context){
    @Provides
    fun provideContext() : Context { return context.applicationContext }
}