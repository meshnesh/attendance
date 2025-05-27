package com.mw.churchattendance.data.converters

import androidx.room.TypeConverter
import com.mw.churchattendance.data.local.entity.TagType

class Converters {
    @TypeConverter
    fun fromTagType(value: TagType): String = value.name

    @TypeConverter
    fun toTagType(value: String): TagType = TagType.valueOf(value)
}