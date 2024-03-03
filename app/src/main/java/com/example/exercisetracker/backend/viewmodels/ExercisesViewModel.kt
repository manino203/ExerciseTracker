package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.db.ExerciseDataRepository
import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExercisesUiState(
    val loading: Boolean = true,
    val exercises: List<Pair<Exercise, ExerciseDetails?>> = emptyList()
)

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {


    var uiState by mutableStateOf(ExercisesUiState())

    fun getExercises(bodyPartId: Int) {
        viewModelScope.launch(ioDispatcher){
            uiState = uiState.copy(loading = true)
            repo.getExercises(bodyPartId).collect{ exs ->
                uiState = uiState.copy(exercises = exs.map {
                    Pair(it, repo.getLatestDetails(it.id!!))
                })
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun onExerciseMove(
        fromIndex: Int,
        toIndex: Int
    ) {
        viewModelScope.launch(ioDispatcher) {
            val tempPos = -1
            val fromPos = uiState.exercises[fromIndex].first.position
            val toPos = uiState.exercises[toIndex].first.position
            repo.updateExercise(uiState.exercises[fromIndex].first.copy(position = tempPos))
            repo.updateExercise(uiState.exercises[toIndex].first.copy(position = fromPos))
            repo.updateExercise(uiState.exercises[fromIndex].first.copy(position = toPos))
        }
    }

    fun addExercise(exercise: Exercise): Boolean {
        if (exercise.name !in uiState.exercises.map { it.first.name }) {
            viewModelScope.launch(ioDispatcher) {
                repo.updateExercise(exercise)
            }
            return true
        }
        return false
    }

    fun editExercise(newName: String, exercise: Exercise): Boolean {
        return addExercise(exercise.copy(name = newName))
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch(ioDispatcher) {
            repo.deleteExercise(exercise)
        }
    }

    fun getDetailsForGraph(exercise: Exercise, loading: MutableState<Boolean>, details: MutableState<List<ExerciseDetails>>){
        viewModelScope.launch(ioDispatcher) {
            loading.value = true
            repo.getExerciseDetails(exercise.id!!).collect{
                details.value = it
                loading.value = false
            }
        }
    }

    fun updateTitle(bpId: Int, onComplete: (Int) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            onComplete(repo.getBodyPart(bpId).label)
        }
    }

}