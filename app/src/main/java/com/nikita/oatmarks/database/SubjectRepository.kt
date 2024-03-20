package com.nikita.oatmarks.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.nikita.oatmarks.models.Subject
import com.nikita.oatmarks.models.SubjectWithMarks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DATABASE_NAME = "subject-database"

class SubjectRepository private constructor(context: Context) {

    private val database : SubjectDatabase = Room.databaseBuilder(
        context.applicationContext,
        SubjectDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val subjectDao = database.subjectDao()

    fun insertSubjectsWithMarks(subjects: List<Subject>) {
        CoroutineScope(Dispatchers.Default).launch {
            subjectDao.clearTable()
            subjectDao.insertSubjects(subjects)
            for (subject in subjects) {
                subjectDao.insertMarks(subject.marks)
            }
        }
    }

    fun getAllSubjectsWithMarks(): LiveData<List<SubjectWithMarks>> = subjectDao.getAllSubjectsWithMarks()

    companion object {
        private var INSTANCE: SubjectRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SubjectRepository(context)
            }
        }

        fun get(): SubjectRepository {
            return INSTANCE ?: throw java.lang.IllegalStateException("SubjectRepository must be initialized")
        }
    }
}