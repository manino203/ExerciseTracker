package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.backend.data.Path
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    repo: ExerciseDataRepository,
    ioDispatcher: CoroutineDispatcher
): ScreenViewModel(repo, ioDispatcher) {

    val details: SnapshotStateList<ExerciseDetails> = mutableStateListOf()

    private fun saveDetails(path: Path) {
        viewModelScope.launch(ioDispatcher) {
            isLoading.value = true
            details.sortByDescending {
                it.timestamp
            }
            repo.saveList(path.get(), details.toList())

            repo.readList<Exercise>(path.bodyPart).ifEmpty { return@launch }.also { exercises ->
                val mutableExercises = exercises.toMutableList()
                val index = exercises.indexOfFirst {
                    it.id == path.exerciseId
                }
                mutableExercises[index] = exercises[index].copy(latestDetails = details.firstOrNull())
                saveExercises(exercises[index].bodyPart, mutableExercises)
            }
        }
    }

    fun addDetail(
        path: Path,
        detail: ExerciseDetails
    ) {
        details.add(
            detail
        )
        saveDetails(path)
    }

    fun editDetail(detail: ExerciseDetails, index: Int, path: Path) {
        details[index] = detail
        saveDetails(path)
    }

    fun deleteDetail(index: Int, path: Path) {
        viewModelScope.launch(ioDispatcher) {
            details.removeAt(index)
            repo.saveList(path.get(), details.toList())
        }
    }

    fun getDetails(
        path: Path,
    ) {
        getDetails(path, isLoading, details)
    }
}