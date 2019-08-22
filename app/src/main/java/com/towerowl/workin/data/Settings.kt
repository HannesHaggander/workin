package com.towerowl.workin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "settings")
data class Settings (
    @PrimaryKey @ColumnInfo(name = "_id") val id: UUID = UUID.randomUUID(),
    val wifiActive: Boolean = false
)