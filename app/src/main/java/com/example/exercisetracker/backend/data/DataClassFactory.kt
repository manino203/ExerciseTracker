package com.example.exercisetracker.backend.data

import com.example.exercisetracker.frontend.composables.utils.EditDataWrapper

class DataClassFactory {
    companion object {

        fun createExercise(data: List<EditDataWrapper>, bodyPartPath: String): Exercise {
            return Exercise(data[0].state.value, bodyPartPath)
        }

        fun createExerciseDetails(data: List<EditDataWrapper>, timestamp: Long): ExerciseDetails {
            return ExerciseDetails(
                data[0].state.value.toFloat(),
                data[1].state.value.toInt(),
                timestamp
            )
        }
    }
}