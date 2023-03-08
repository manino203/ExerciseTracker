package com.example.exercisetracker.backend.data

import com.example.exercisetracker.frontend.composables.utils.DateFormatter
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogFormDataList

class DataClassFactory {
    companion object {

        fun createExercise(data: DialogFormDataList, bodyPartPath: String): Exercise {
            return Exercise(
                name = data.items[0].state.value,
                bodyPart = bodyPartPath
            )
        }

        fun createExerciseDetails(data: DialogFormDataList): ExerciseDetails {
            return ExerciseDetails(
                weight = data.items[0].state.value.toFloat(),
                reps = data.items[1].state.value.toInt(),
                series = data.items[2].state.value.toInt(),
                timestamp = DateFormatter.toTimestamp(data.items[3].state.value)
            )
        }
    }
}