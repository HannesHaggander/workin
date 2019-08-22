package com.towerowl.workin.events

sealed class NetworkObserverEvent {
    object WorkNetworkAvailable : NetworkObserverEvent()
    object WorkNetworkLost : NetworkObserverEvent()
}