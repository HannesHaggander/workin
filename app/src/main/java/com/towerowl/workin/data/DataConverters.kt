package com.towerowl.workin.data

import androidx.room.TypeConverter
import java.util.*

class DataConverters{
    @TypeConverter fun nullableCalendarToTimestamp(calendar : Calendar?) : Long? = calendar?.timeInMillis

    @TypeConverter fun nullableDateToTimestamp(value : Long?) : Calendar? =
        value?.let { Calendar.getInstance().apply { timeInMillis = value } }

    @TypeConverter fun calendarToTimestamp(calendar : Calendar) : Long = calendar.timeInMillis

    @TypeConverter fun timestampToCalendar(value : Long) : Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter fun uuidToString(uuid : UUID) : String = uuid.toString()

    @TypeConverter fun stringToUuid(value : String) : UUID = UUID.fromString(value)
}