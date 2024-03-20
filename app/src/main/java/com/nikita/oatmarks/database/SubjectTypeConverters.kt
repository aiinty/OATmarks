package com.nikita.oatmarks.database

import androidx.room.TypeConverter
import java.util.UUID

class SubjectTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}