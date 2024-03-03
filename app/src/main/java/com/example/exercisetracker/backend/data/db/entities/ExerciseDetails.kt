package com.example.exercisetracker.backend.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    ]
)
data class ExerciseDetails(
    val weight: Float,
    val reps: Int,
    val series: Int,
    val timestamp: Long,
    val exerciseId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)