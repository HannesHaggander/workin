package com.towerowl.workin.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towerowl.workin.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsModel : ViewModel(){
    val autoWifiActive = MutableLiveData<Boolean>()

    lateinit var activity : Activity

    fun fetchWifiAutoStatus(){
        viewModelScope.launch(Dispatchers.IO) {
            autoWifiActive.value = App.get(activity)
                .appComponent
                .settingsRepository()
                .isAutoWifiActive()
        }
    }
}