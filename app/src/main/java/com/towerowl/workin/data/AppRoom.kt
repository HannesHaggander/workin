package com.towerowl.workin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.towerowl.workin.utils.DATABASE_NAME
import javax.inject.Inject

@Database(entities = [WorkSession::class], version = 1, exportSchema = false)
@TypeConverters(DataConverters::class)
abstract class AppRoom : RoomDatabase() {
    abstract fun workSessionDao() : WorkSessionDao
}