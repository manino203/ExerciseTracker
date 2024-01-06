package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.backend.data.Path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

open class ScreenViewModel(
    val repo: ExerciseDataRepository,
    val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    var isLoading = mutableStateOf(true)
    protected fun <T> onItemMove(items: SnapshotStateList<T>, fromIndex: Int, toIndex: Int) {
        if (fromIndex != toIndex && fromIndex in 0 until items.size && toIndex in 0 until items.size) {
            items.apply {
                add(toIndex, removeAt(fromIndex))
            }
        }
    }

    protected suspend inline fun <reified type> getItems(path: String): List<type> {
        val items = mutableListOf<type>()
        isLoading.value = true

        repo.readList<type>(path).forEach {
            (it as? type)?.let{item ->
                items.add(item)
            }
        }
        isLoading.value = false
        return items
    }

    fun saveExercises(path: String, exercises: List<Exercise>) {

        viewModelScope.launch(ioDispatcher) {
            isLoading.value = true
            repo.saveList(path, exercises)
            isLoading.value = false
        }
    }

    fun getDetails(path: Path, loading: MutableState<Boolean>, details: MutableList<ExerciseDetails>){
        viewModelScope.launch(ioDispatcher){
            loading.value = true
            details.clear()
            details.addAll(getItems<ExerciseDetails>(path.get()))
            loading.value = false
        }
    }
}