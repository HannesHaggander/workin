package com.towerowl.workin.utils

import android.content.Context
import com.towerowl.workin.data.AppRoom
import com.towerowl.workin.data.WorkSessionRepository

object Injector {

    fun injectWorkSessionRepository(context : Context) : WorkSessionRepository {
        return WorkSessionRepository.getInstance(AppRoom.getInstance(context.applicationContext).workSessionDao())
    }
}