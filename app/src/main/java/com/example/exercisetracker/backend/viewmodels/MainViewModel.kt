package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
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
class MainViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    val bodyPartNames = mutableStateListOf(
        "Biceps",
        "Triceps",
        "Shoulders",
        "Chest",
        "Abs",
        "Back",
        "Legs"
    )
    var details: SnapshotStateList<ExerciseDetails> = mutableStateListOf()
    var exercises: SnapshotStateList<Exercise> = mutableStateListOf()
    var detailsLoading = mutableStateOf(true)
    var exercisesLoading = mutableStateOf(true)


    fun getExercises(bodyPart: String) {
        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = true
            exercises = repo.readList<Exercise>(bodyPart).toMutableStateList()
            exercisesLoading.value = false
        }
    }

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch(ioDispatcher) {
            if (exercise.name !in exercises.map { it.name }) {
                exercises.add(exercise)
                repo.saveList(exercise.bodyPart, exercises.toList())
            }
        }
    }

    fun editExercise(newName: String, exercise: Exercise) {

        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = true
            val index = exercises.indexOfFirst {
                it.name == exercise.name
            }
            if (index != -1) {
                exercises[index] = exercises[index].copy(name = newName)
                repo.saveList(exercise.bodyPart, exercises.toList())
            }

            exercisesLoading.value = false

        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = true
            repo.deleteValue(Path(exercise.bodyPart, exercise.id).get())
            exercises.remove(exercise)
            repo.saveList(exercise.bodyPart, exercises)
            exercisesLoading.value = false
        }
    }

    fun addDetail(
        path: Path,
        detail: ExerciseDetails
    ) {
        viewModelScope.launch(ioDispatcher) {
            details.add(
                detail
            )
            details.sortByDescending {
                it.timestamp
            }
            repo.saveList(path.get(), details.toList())

        }
    }

    fun editDetail(detail: ExerciseDetails, index: Int, path: Path) {
        viewModelScope.launch(ioDispatcher) {
            details[index] = detail
            details.sortByDescending {
                it.timestamp
            }
            repo.saveList(path.get(), details.toList())
        }
    }

    fun deleteDetail(index: Int, path: Path) {
        viewModelScope.launch(ioDispatcher) {
            details.removeAt(index)
            repo.saveList(path.get(), details.toList())
        }
    }

    fun getDetails(path: Path) {
        viewModelScope.launch(ioDispatcher) {
            detailsLoading.value = true
            details = repo.readList<ExerciseDetails>(path.get()).toMutableStateList()
            detailsLoading.value = false
        }
    }
}