package com.example.exercisetracker.backend.data

import com.example.exercisetracker.frontend.composables.utils.DateFormatter

class DataClassFactory {
    companion object {

        fun createExercise(data: List<String>, bodyPartPath: String): Exercise {
            return Exercise(
                name = data.first(),
                bodyPart = bodyPartPath
            )
        }

        fun createExerciseDetails(data: List<String>): ExerciseDetails {
            return ExerciseDetails(
                weight = data[0].toFloat(),
                reps = data[1].toInt(),
                series = data[2].toInt(),
                timestamp = DateFormatter.toTimestamp(data[3])
            )
        }
    }
}