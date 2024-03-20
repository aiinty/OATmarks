package com.nikita.oatmarks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.nikita.oatmarks.models.Mark
import com.nikita.oatmarks.models.Subject
import com.nikita.oatmarks.models.SubjectWithMarks
import java.util.*

@Dao
abstract class SubjectDao {

    @Query("DELETE FROM subject")
    abstract fun clearTable()

    @Insert
    abstract fun insertSubject(subject: Subject)

    @Insert
    abstract fun insertSubjects(subject: List<Subject>)

    @Insert
    abstract fun insertMarks(marks: List<Mark>)

    @Query("SELECT * FROM subject")
    abstract fun getSubjects(): LiveData<List<Subject>>

    @Transaction
    @Query("SELECT * FROM subject")
    abstract fun getAllSubjectsWithMarks(): LiveData<List<SubjectWithMarks>>

    @Query("SELECT * FROM subject WHERE id IS:id")
    abstract fun getSubject(id: UUID): Subject?

    @Query("SELECT * FROM mark WHERE subjectId IS :subjectId")
    abstract fun getMarksList(subjectId: UUID): List<Mark>

}