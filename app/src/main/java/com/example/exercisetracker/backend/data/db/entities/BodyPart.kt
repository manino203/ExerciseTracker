package com.example.exercisetracker.backend.data.db.entities

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["position"], unique = true)]
)
data class BodyPart(
    val path: String,
    val position: Int,
    @StringRes val label: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
}