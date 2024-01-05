package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyPartsViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel(){

    companion object {
        private const val BODY_PARTS_PATH =
            "7466e74f83c475325ee39d4a5aefda0d3878bcca04dedc9fbd81d13429e9c2c2"
    }

    private val DEFAULT_BODY_PARTS = listOf(
        BodyPart("Biceps", R.string.biceps),
        BodyPart("Triceps", R.string.triceps),
        BodyPart("Shoulders", R.string.shoulders),
        BodyPart("Chest", R.string.chest),
        BodyPart("Abs", R.string.abs),
        BodyPart("Back", R.string.back),
        BodyPart("Legs", R.string.legs)
    )

    val bodyParts = mutableStateListOf<BodyPart>()
    var bodyPartsLoading = mutableStateOf(true)

    fun getBodyParts() {
        viewModelScope.launch(ioDispatcher) {
            bodyPartsLoading.value = true
            bodyParts.clear()

            bodyParts.addAll(suspend {
                try{
                    repo.readList<BodyPart>(BODY_PARTS_PATH).toMutableStateList()
                } catch(e: JsonSyntaxException){
                    listOf()
                }
            }()
            )
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

    private fun <T> onItemMove(items: SnapshotStateList<T>, fromIndex: Int, toIndex: Int) {
        if (fromIndex != toIndex && fromIndex in 0 until items.size && toIndex in 0 until items.size) {
            items.apply {
                add(toIndex, removeAt(fromIndex))
            }
        }
    }
    fun onBodyPartMove(
        fromIndex: Int,
        toIndex: Int
    ) {
        onItemMove(bodyParts, fromIndex, toIndex)
        saveBodyParts(false)
    }


    fun getBodyPartByPath(path: String): BodyPart?{
        return try{
            bodyParts.first {
                it.path == path
            }
        }catch (e: NoSuchElementException){
            null
        }
    }

}