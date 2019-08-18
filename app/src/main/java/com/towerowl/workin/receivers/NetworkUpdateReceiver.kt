package com.towerowl.workin.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build

class NetworkUpdateReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        when (cm.getNetworkCapabilities(cm.activeNetwork)){

        }
    }

}