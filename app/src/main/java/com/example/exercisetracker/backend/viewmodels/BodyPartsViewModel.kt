package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.db.ExerciseDataRepository
import com.example.exercisetracker.backend.data.db.entities.BodyPart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BodyPartsUiState(
    val loading: Boolean = true,
    val bodyParts: List<BodyPart> = emptyList()
)

@HiltViewModel
class BodyPartsViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {


    private val DEFAULT_BODY_PARTS = listOf(
        BodyPart("Biceps", 0, R.string.biceps),
        BodyPart("Triceps", 1, R.string.triceps),
        BodyPart("Shoulders", 2, R.string.shoulders),
        BodyPart("Chest", 3, R.string.chest),
        BodyPart("Abs", 4, R.string.abs),
        BodyPart("Back", 5, R.string.back),
        BodyPart("Legs", 6, R.string.legs)
    )

    var uiState by mutableStateOf(BodyPartsUiState())

    fun getBodyParts() {
        viewModelScope.launch(ioDispatcher){
            uiState = uiState.copy(loading = true)
            repo.getBodyParts().collect{
                if (it.isEmpty()){
                    repo.updateBodyParts(DEFAULT_BODY_PARTS)
                }else {
                    uiState = uiState.copy(bodyParts = it)
                }
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun onBodyPartMove(
        fromIndex: Int,
        toIndex: Int
    ) {
        viewModelScope.launch(ioDispatcher) {
            val tempPos = -1
            val fromPos = uiState.bodyParts[fromIndex].position
            val toPos = uiState.bodyParts[toIndex].position
            repo.updateBodyPart(uiState.bodyParts[fromIndex].copy(position = tempPos))
            repo.updateBodyPart(uiState.bodyParts[toIndex].copy(position = fromPos))
            repo.updateBodyPart(uiState.bodyParts[fromIndex].copy(position = toPos))

        }
    }
}