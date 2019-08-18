package com.towerowl.workin.events

import android.net.ConnectivityManager
import android.net.Network
import com.towerowl.workin.dagger.components.AppComponent
import com.towerowl.workin.services.NetworkObserverService

class NetworkObserverCallback constructor(val networkObserverService: NetworkObserverService):
    ConnectivityManager.NetworkCallback() {


    override fun onAvailable(network: Network) {
        networkObserverService.isConnectedNetworkWifi(network)
        super.onAvailable(network)
    }

    override fun onLost(network: Network) {
        networkObserverService.isLostNetworkWifi(network)
        super.onLost(network)
    }
}