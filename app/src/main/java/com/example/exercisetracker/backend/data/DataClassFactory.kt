package com.example.exercisetracker.backend.data

import com.example.exercisetracker.frontend.composables.utils.DialogFormDataList

class DataClassFactory {
    companion object {

        fun createExercise(data: DialogFormDataList, bodyPartPath: String): Exercise {
            return Exercise(data.items[0].state.value, bodyPartPath)
        }

        fun createExerciseDetails(data: DialogFormDataList, timestamp: Long): ExerciseDetails {
            return ExerciseDetails(
                data.items[0].state.value.toFloat(),
                data.items[1].state.value.toInt(),
                timestamp
            )
        }
    }
}