package com.towerowl.workin.data

import javax.inject.Inject

class SettingsRepository
@Inject constructor(private val settingsDao : SettingsDao){
    fun isAutoWifiActive() : Boolean =
        settingsDao.getSettings().first().wifiActive
}