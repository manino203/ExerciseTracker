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

@Suppress("PrivatePropertyName")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) : ViewModel() {

    companion object {
        private const val BODY_PARTS_PATH =
            "7466e74f83c475325ee39d4a5aefda0d3878bcca04dedc9fbd81d13429e9c2c2"
    }

    private val DEFAULT_BODY_PARTS = mutableStateListOf(
        BodyPart("Biceps", context.getString(R.string.biceps)),
        BodyPart("Triceps", context.getString(R.string.triceps)),
        BodyPart("Shoulders", context.getString(R.string.shoulders)),
        BodyPart("Chest", context.getString(R.string.chest)),
        BodyPart("Abs", context.getString(R.string.abs)),
        BodyPart("Back", context.getString(R.string.back)),
        BodyPart("Legs", context.getString(R.string.legs)),
    )

    val bodyParts = mutableStateListOf<BodyPart>()

    var details: SnapshotStateList<ExerciseDetails> = mutableStateListOf()
    var exercises: SnapshotStateList<Exercise> = mutableStateListOf()
    var bodyPartsLoading = mutableStateOf(true)
    var detailsLoading = mutableStateOf(true)
    var exercisesLoading = mutableStateOf(true)

    private fun <T> onItemMove(items: SnapshotStateList<T>, fromIndex: Int, toIndex: Int) {

        if (fromIndex != toIndex && fromIndex in 0 until items.size && toIndex in 0 until items.size) {
            items.apply {
                add(toIndex, removeAt(fromIndex))


            }
        }
    }

    fun getBodyParts() {
        viewModelScope.launch(ioDispatcher) {
            bodyPartsLoading.value = true
            bodyParts.clear()
            bodyParts.addAll(repo.readList<BodyPart>(BODY_PARTS_PATH).toMutableStateList())
            if (bodyParts.isEmpty()) {
                bodyParts.addAll(DEFAULT_BODY_PARTS)
                saveBodyParts()
            }
            bodyPartsLoading.value = false
        }
    }

    private fun saveBodyParts(loading: Boolean = true) {
        viewModelScope.launch(ioDispatcher) {
            bodyPartsLoading.value = loading
            repo.saveList(BODY_PARTS_PATH, bodyParts)
            bodyPartsLoading.value = false
        }
    }

    fun onBodyPartMove(
        fromIndex: Int,
        toIndex: Int
    ) {
        onItemMove(bodyParts, fromIndex, toIndex)
        saveBodyParts(false)
    }

    fun onExerciseMove(
        path: String,
        fromIndex: Int,
        toIndex: Int
    ) {
        onItemMove(exercises, fromIndex, toIndex)
        saveExercises(path, false)
    }

    fun saveExercises(path: String, loading: Boolean = true) {

        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = loading
            repo.saveList(path, exercises.toList())
            exercisesLoading.value = false
        }
    }

    fun getExercises(bodyPart: String) {
        viewModelScope.launch(ioDispatcher) {
            exercisesLoading.value = true
            exercises.clear()
            exercises.addAll(repo.readList<Exercise>(bodyPart).toMutableStateList())
            exercisesLoading.value = false
        }
    }

    fun addExercise(exercise: Exercise) {
        if (exercise.name !in exercises.map { it.name }) {
            exercises.add(0, exercise)
            saveExercises(exercise.bodyPart)
        }
    }

    fun editExercise(newName: String, exercise: Exercise) {
        val index = exercises.indexOfFirst {
            it.name == exercise.name
        }
        if (index != -1) {
            exercises[index] = exercises[index].copy(name = newName)
            saveExercises(exercise.bodyPart)
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
            saveExercises(exercises[index].bodyPart)
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