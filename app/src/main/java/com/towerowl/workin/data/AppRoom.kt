package com.towerowl.workin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.towerowl.workin.typeConverters.DataConverter
import com.towerowl.workin.typeConverters.DateConverter

@Database(entities = [WorkSession::class, Settings::class], version = 4, exportSchema = false)
@TypeConverters(DataConverter::class, DateConverter::class)
abstract class AppRoom : RoomDatabase() {
    abstract fun workSessionDao() : WorkSessionDao
    abstract fun settingsDao() : SettingsDao
}