package com.example.exercisetracker.backend.data

import java.util.UUID

data class Exercise(
    val name: String,
    val bodyPart: String,
    val latestDetails: ExerciseDetails? = null,
    val id: String = UUID.randomUUID().toString()
)


