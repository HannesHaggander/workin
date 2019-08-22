package com.towerowl.workin.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import com.towerowl.workin.events.NetworkObserverCallback
import com.towerowl.workin.utils.APPTAG

class NetworkObserverService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null
    private var callback : NetworkObserverCallback? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerNetworkObserver()
        return START_STICKY
    }

    override fun onDestroy() {
        unregisterNetworkObserver()
        super.onDestroy()
    }

    private fun registerNetworkObserver(){
        getConnectivityManager()
            .registerNetworkCallback(NetworkRequest.Builder().build(),
                NetworkObserverCallback(this)
                .also { callback = it })
    }

    private fun unregisterNetworkObserver(){
        callback?.let { getConnectivityManager()
            .unregisterNetworkCallback(it)
            .also { callback = null }
        }
    }

    fun isConnectedNetworkWifi(network: Network){
        if(!isNetworkWifiConnection(network)) { return }

        (getSystemService(Context.WIFI_SERVICE) as WifiManager)
            .connectionInfo
            .also {
                Log.d(APPTAG, "Connected to ssid: ${it.ssid}")
            }
    }

    fun isLostNetworkWifi(network : Network){
        if(!isNetworkWifiConnection(network)){ return }

        Log.d(APPTAG, "Lost wifi connection")
    }

    private fun isNetworkWifiConnection(network: Network) : Boolean =
        getConnectivityManager().getNetworkCapabilities(network)
            ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

    private fun getConnectivityManager() =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}