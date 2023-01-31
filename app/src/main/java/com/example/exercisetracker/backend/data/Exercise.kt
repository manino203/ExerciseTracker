package com.example.exercisetracker.backend.data

import java.util.*

data class Exercise(
    val name: String,
    val bodyPart: String,
    val latestDetails: ExerciseDetails? = null,
    val id: String = UUID.randomUUID().toString()
)


