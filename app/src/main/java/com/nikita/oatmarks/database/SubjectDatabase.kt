package com.nikita.oatmarks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nikita.oatmarks.models.Mark
import com.nikita.oatmarks.models.Subject

@Database(entities = [Subject::class, Mark::class], version = 1)
@TypeConverters(SubjectTypeConverters::class)
abstract class SubjectDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao

}