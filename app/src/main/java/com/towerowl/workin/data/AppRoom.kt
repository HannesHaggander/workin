package com.towerowl.workin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WorkSession::class], version = 1, exportSchema = false)
@TypeConverters(DataConverters::class)
abstract class AppRoom : RoomDatabase() {
    abstract fun workSessionDao() : WorkSessionDao
}