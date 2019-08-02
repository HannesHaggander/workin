package com.towerowl.workin.data

import androidx.room.*

@Dao
interface WorkSessionDao {
    @Query("SELECT * FROM `work-sessions`")
    fun getWorkSessions() : List<WorkSession>

    @Query("SELECT * FROM `work-sessions` WHERE _id = :id")
    fun getWorkSession(id : String) : WorkSession

    @Insert
    fun insertWorkSession(workSession : WorkSession)

    @Delete
    fun deleteWorkSession(workSession : WorkSession)

    @Update
    fun updateWorkSession(workSession : WorkSession)
}