package com.towerowl.workin.typeConverters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class DataConverter{

    @TypeConverter fun uuidToString(uuid : UUID) : String = uuid.toString()

    @TypeConverter fun stringToUuid(value : String) : UUID = UUID.fromString(value)

    @TypeConverter fun booleanToInt(value : Boolean) : Int = if(value) 1 else 0

    @TypeConverter fun IntToBoolean(value : Int) : Boolean = value > 0

}