package com.nikita.oatmarks.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(foreignKeys = arrayOf(ForeignKey(
    entity = Subject::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("subjectId"),
    onDelete = ForeignKey.CASCADE)))
data class Mark(
    @NonNull
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    @NonNull
    var subjectId: UUID = UUID.randomUUID(),
    var value: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var comment: String = ""
)