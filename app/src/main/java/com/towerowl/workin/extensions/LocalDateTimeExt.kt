package com.towerowl.workin.extensions

import com.towerowl.workin.data.WorkSession
import java.time.LocalDateTime

fun LocalDateTime.startOfDay(date : LocalDateTime, workSession : WorkSession) : LocalDateTime =
    LocalDateTime.of(date.year, date.month, date.dayOfMonth, 0,0,0)