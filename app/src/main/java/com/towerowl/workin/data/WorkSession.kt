package com.towerowl.workin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "work-sessions")
data class WorkSession(
    @PrimaryKey @ColumnInfo(name = "_id") val id: UUID,
    val description: String,
    var createdAt : Calendar = Calendar.getInstance(),
    var closedAt : Calendar? = null
){
    override fun toString(): String {
        return "$id:$description"
    }
}

sealed class WorkSessionEvent {
    class Inserted(val data : WorkSession) : WorkSessionEvent()
    class Removed(val data : WorkSession) : WorkSessionEvent()
    class Updated(val data : WorkSession) : WorkSessionEvent()
}