package com.towerowl.workin.events

sealed class NetworkObserverEvent {
    class WorkNetworkAvailable : NetworkObserverEvent()
    class WorkNetworkLost : NetworkObserverEvent()
}