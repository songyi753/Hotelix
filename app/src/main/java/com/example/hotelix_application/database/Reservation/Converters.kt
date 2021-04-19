package com.example.hotelix_application.database

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromReserveDate (value: Long?) : Date?{
        return value?.let{Date(it)}
    }

    @TypeConverter
    fun toReserveDate(date: Date?) : Long?{
        return date?.time?.toLong()
    }
}