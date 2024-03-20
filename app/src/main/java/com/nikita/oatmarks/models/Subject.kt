package com.nikita.oatmarks.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Subject(
    @NonNull
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var title: String = "",
    @Ignore
    var marks: List<Mark> = emptyList()
)