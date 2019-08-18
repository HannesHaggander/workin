package com.towerowl.workin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.*
import java.util.*

@Entity(tableName = "work_sessions")
data class WorkSession(
    @PrimaryKey @ColumnInfo(name = "_id") val id: UUID = UUID.randomUUID(),
    val description: String = "",
    var createdAt : OffsetDateTime = OffsetDateTime.now(),
    var closedAt : OffsetDateTime? = null
){
    override fun toString(): String =
        "$id:${hashCode()}:$description"


    fun isDay(localDate: LocalDate) : Boolean =
        createdAt.toLocalDateTime()
            .toLocalDate()
            .toEpochDay() == localDate.toEpochDay()
}