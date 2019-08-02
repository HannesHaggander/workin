package com.towerowl.workin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.towerowl.workin.utils.DATABASE_NAME

@Database(entities = [WorkSession::class], version = 1, exportSchema = false)
@TypeConverters(DataConverters::class)
abstract class AppRoom : RoomDatabase(){
    abstract fun workSessionDao() : WorkSessionDao

    companion object{
        @Volatile private var instance: AppRoom? = null

        fun getInstance(context: Context) : AppRoom {
            return instance ?: synchronized(this){
                instance ?: buildRoom(context).also { instance = it }
            }
        }

        private fun buildRoom(context: Context) : AppRoom {
            return Room.databaseBuilder(context, AppRoom::class.java, DATABASE_NAME)
                    //make on create callback with worker?
                .build()
        }
    }
}