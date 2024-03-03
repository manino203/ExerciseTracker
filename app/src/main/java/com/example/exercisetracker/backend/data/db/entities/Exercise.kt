package com.example.exercisetracker.backend.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["position"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = BodyPart::class, parentColumns = ["id"], childColumns = ["bodyPartId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = ExerciseDetails::class, parentColumns = ["id"], childColumns = ["latestDetailsId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    ]
)
data class Exercise(
    val name: String,
    val bodyPartId: Int,
    val position: Int,
    val latestDetailsId: Int? = null,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)


