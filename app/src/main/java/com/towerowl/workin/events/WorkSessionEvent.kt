package com.towerowl.workin.events

import com.towerowl.workin.data.WorkSession

sealed class WorkSessionEvent {
    class Inserted(val data : WorkSession) : WorkSessionEvent()
    class Removed(val data : WorkSession) : WorkSessionEvent()
    class Updated(val data : WorkSession) : WorkSessionEvent()
}