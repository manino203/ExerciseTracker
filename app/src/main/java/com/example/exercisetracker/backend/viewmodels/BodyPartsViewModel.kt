package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyPartsViewModel @Inject constructor(
    repo: ExerciseDataRepository,
    ioDispatcher: CoroutineDispatcher
) : ScreenViewModel(repo, ioDispatcher){

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

    fun getBodyParts() {
        viewModelScope.launch(ioDispatcher) {
            bodyParts.clear()
            bodyParts.addAll(getItems<BodyPart>(BODY_PARTS_PATH))
            if (bodyParts.isEmpty()) {
                bodyParts.addAll(DEFAULT_BODY_PARTS)
                saveBodyParts()
            }
        }
    }

    private fun saveBodyParts(loading: Boolean = true) {
        viewModelScope.launch(ioDispatcher) {
            isLoading.value = loading
            repo.saveList(BODY_PARTS_PATH, bodyParts)
            isLoading.value = false
        }
    }


    fun onBodyPartMove(
        fromIndex: Int,
        toIndex: Int
    ) {
        onItemMove(bodyParts, fromIndex, toIndex)
        saveBodyParts(false)
    }
}