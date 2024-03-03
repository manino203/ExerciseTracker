package com.example.exercisetracker.backend.data.db

import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.DateFormatter

class DataClassFactory {
    companion object {

        private var exercisePosition = 0
        fun createExercise(data: List<String>, bodyPartId: Int, id: Int? = null): Exercise {
            return Exercise(
                name = data.first(),
                bodyPartId = bodyPartId,
                position = exercisePosition,
                latestDetailsId = null,
                id = id
            ).also {
                exercisePosition += 1
            }
        }

        fun createExerciseDetails(data: List<String>, exerciseId: Int, id: Int? = null): ExerciseDetails {
            return ExerciseDetails(
                weight = data[0].toFloat(),
                reps = data[1].toInt(),
                series = data[2].toInt(),
                timestamp = DateFormatter.toTimestamp(data[3]),
                exerciseId = exerciseId,
                id = id
            )
        }
    }
}