package com.example.exercisetracker.backend.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) : ViewModel() {


    val bodyParts = mutableStateListOf(

        BodyPart("Biceps", context.getString(R.string.biceps)),
        BodyPart("Triceps", context.getString(R.string.triceps)),
        BodyPart("Shoulders", context.getString(R.string.shoulders)),
        BodyPart("Chest", context.getString(R.string.chest)),
        BodyPart("Abs", context.getString(R.string.abs)),
        BodyPart("Back", context.getString(R.string.back)),
        BodyPart("Legs", context.getString(R.string.legs)),
    )
    var details: SnapshotStateList<ExerciseDetails> = mutableStateListOf()
    var exercises: SnapshotStateList<Exercise> = mutableStateListOf()
    var detailsLoading = mutableStateOf(true)
    var exercisesLoading = mutableStateOf(true)

    private fun saveExercises(exercise: Exercise) {

        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = true
            repo.saveList(exercise.bodyPart, exercises.toList())
            exercisesLoading.value = false
        }
    }

    fun getExercises(bodyPart: String) {
        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = true
            exercises = repo.readList<Exercise>(bodyPart).toMutableStateList()
            exercisesLoading.value = false
        }
    }

    fun addExercise(exercise: Exercise) {
        if (exercise.name !in exercises.map { it.name }) {
            exercises.add(exercise)
            saveExercises(exercise)
        }
    }

    fun editExercise(newName: String, exercise: Exercise) {
        val index = exercises.indexOfFirst {
            it.name == exercise.name
        }
        if (index != -1) {
            exercises[index] = exercises[index].copy(name = newName)
            saveExercises(exercise)
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

    private fun saveDetails(path: Path) {
        viewModelScope.launch(ioDispatcher) {
            detailsLoading.value = true
            details.sortByDescending {
                it.timestamp
            }
            repo.saveList(path.get(), details.toList())

            val index = exercises.indexOfFirst {
                it.id == path.exerciseId
            }

            exercises[index] = exercises[index].copy(latestDetails = details.firstOrNull())
            saveExercises(exercises[index])
            detailsLoading.value = false
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
        loading: MutableState<Boolean> = detailsLoading,
        _details: SnapshotStateList<ExerciseDetails> = details
    ) {
        viewModelScope.launch(ioDispatcher) {
            loading.value = true
            _details.clear()
            _details.addAll(repo.readList<ExerciseDetails>(path.get()).toMutableStateList())
            loading.value = false
        }
    }
}