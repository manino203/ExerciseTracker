package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.example.exercisetracker.backend.data.ExerciseDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repo: ExerciseDataRepository)
    : ViewModel() {

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
    var exercises: SnapshotStateList<String> = mutableStateListOf()
    var detailsLoading = mutableStateOf(true)
    var exercisesLoading = mutableStateOf(true)


    fun getExercises(bodyPart: String){
        viewModelScope.launch(Dispatchers.IO) {
            exercisesLoading.value = true
            exercises = repo.readList<String>(bodyPart).toMutableStateList()
            exercisesLoading.value = false
        }
    }

    fun addExercise(bodyPart: String, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            exercises.add(name)
            repo.saveList(bodyPart, exercises.toList())
        }
    }

    fun exerciseItemOnClick(path: String){
        getDetails(path)
    }

    fun addDetail(path: String, weight: Float, reps: Int, time: Long = Calendar.getInstance().timeInMillis){
        viewModelScope.launch(Dispatchers.IO) {
            details.add(
                ExerciseDetails(
                    weight,
                    reps,
                    time
                )
            )
            repo.saveList(path, details.toList())

        }
    }

    private fun getDetails(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            detailsLoading.value = true
            details = repo.readDetailsList(path).toMutableStateList()
            detailsLoading.value = false
        }
    }
}