package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.example.exercisetracker.backend.data.Path
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    repo: ExerciseDataRepository,
    ioDispatcher: CoroutineDispatcher
): ScreenViewModel(repo, ioDispatcher) {


    var exercises: SnapshotStateList<Exercise> = mutableStateListOf()
    fun onExerciseMove(
        path: String,
        fromIndex: Int,
        toIndex: Int
    ) {
        onItemMove(exercises, fromIndex, toIndex)
        saveExercises(path, exercises)
    }

    fun getExercises(bodyPart: String) {
        viewModelScope.launch(ioDispatcher) {
            exercises.clear()
            exercises.addAll(getItems<Exercise>(bodyPart))
        }
    }

    fun addExercise(exercise: Exercise) {
        if (exercise.name !in exercises.map { it.name }) {
            exercises.add(0, exercise)
            saveExercises(exercise.bodyPart, exercises)
        }
    }

    fun editExercise(newName: String, exercise: Exercise) {
        val index = exercises.indexOfFirst {
            it.name == exercise.name
        }
        if (index != -1) {
            exercises[index] = exercises[index].copy(name = newName)
            saveExercises(exercise.bodyPart, exercises)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch(ioDispatcher) {
            isLoading.value = true
            repo.deleteValue(Path(exercise.bodyPart, exercise.id).get())
            exercises.remove(exercise)
            repo.saveList(exercise.bodyPart, exercises)
            isLoading.value = false
        }
    }



}