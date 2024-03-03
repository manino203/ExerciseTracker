package com.example.exercisetracker.backend.viewmodels

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


data class DetailsUiState(
    val loading: Boolean = true,
    val exercise: Exercise? = null,
    val details: List<ExerciseDetails> = emptyList()
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    var detailsUiState by mutableStateOf(DetailsUiState())
    fun addDetail(
        detail: ExerciseDetails
    ) {
        editDetail(detail)
    }

    fun editDetail(detail: ExerciseDetails) {
        viewModelScope.launch(ioDispatcher) {
            repo.updateExerciseDetails(detail)
            repo.getExercise(detail.exerciseId).also{
                repo.updateExercise(it.copy(latestDetailsId = repo.getLatestDetailsId(it.id!!)))
            }
        }
    }

    fun deleteDetail(details: ExerciseDetails) {
        viewModelScope.launch(ioDispatcher) {
            repo.deleteDetails(details)
        }
    }

    fun getDetails(exerciseId: Int) {
        viewModelScope.launch(ioDispatcher) {
            detailsUiState = detailsUiState.copy(loading = true)
            repo.getExerciseDetails(exerciseId).collect{
                detailsUiState = detailsUiState.copy(details = it, exercise = repo.getExercise(exerciseId))
                detailsUiState = detailsUiState.copy(loading = false)
            }
        }
    }
}