package com.towerowl.workin.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towerowl.workin.App
import com.towerowl.workin.data.WorkSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewModel : ViewModel() {
    lateinit var activity : Activity

    val workSessionsToday = MutableLiveData<List<WorkSession>>()

    fun getWorkSessionsFromToday() {
        viewModelScope.launch(Dispatchers.IO) {
            val sessions = App.get(activity)
                .appComponent
                .workSessionRepository()
                .getWorkSessionsFromToday()

            withContext(Dispatchers.Main){
                workSessionsToday.value = sessions
            }
        }
    }
}