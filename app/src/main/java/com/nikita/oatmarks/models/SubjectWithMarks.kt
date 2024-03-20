package com.nikita.oatmarks.models

import androidx.room.Embedded
import androidx.room.Relation

data class SubjectWithMarks(
    @Embedded
    val subject: Subject,
    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val marks: List<Mark>
)