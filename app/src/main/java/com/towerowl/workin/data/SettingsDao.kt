package com.towerowl.workin.data

import androidx.room.*

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings")
    fun getSettings() : List<Settings>

    @Insert
    fun insertSettings(settings : Settings)

    @Delete
    fun deleteSettings(settings: Settings)

    @Update
    fun updateSettings(settings : Settings)
}