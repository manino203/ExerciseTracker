package com.example.exercisetracker.backend.viewmodels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.backend.data.Path
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("PrivatePropertyName")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {






    var details: SnapshotStateList<ExerciseDetails> = mutableStateListOf()
    var exercises: SnapshotStateList<Exercise> = mutableStateListOf()

    var detailsLoading = mutableStateOf(true)
    var exercisesLoading = mutableStateOf(true)

    private fun <T> onItemMove(items: SnapshotStateList<T>, fromIndex: Int, toIndex: Int) {
        if (fromIndex != toIndex && fromIndex in 0 until items.size && toIndex in 0 until items.size) {
            items.apply {
                add(toIndex, removeAt(fromIndex))
            }
        }
    }


    fun onExerciseMove(
        path: String,
        fromIndex: Int,
        toIndex: Int
    ) {
        onItemMove(exercises, fromIndex, toIndex)
        saveExercises(path, false)
    }
    fun getExerciseById(id: String): Exercise?{
        return try{
            exercises.first {
                it.id == id
            }
        }catch (e: NoSuchElementException){
            null
        }
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

    val exportFileUri = mutableStateOf<Uri?>(null)

//    fun exportData(){
//        bodyPartsLoading.value = true
//        viewModelScope.launch(ioDispatcher){
//            exportFileUri.value = null
//            delay(100L)
//            exportFileUri.value = repo.getDataUri()
//            bodyPartsLoading.value = false
//        }
//    }

//    fun importData(){
//        val initialUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            MediaStore.Downloads.EXTERNAL_CONTENT_URI
//        } else {
//            Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
//        }
//        remember
//        context.startActivity(Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "*/*"
//            putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
//        })
//    }
}

