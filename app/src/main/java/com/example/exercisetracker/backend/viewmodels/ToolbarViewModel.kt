package com.example.exercisetracker.backend.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.exercisetracker.frontend.composables.utils.routes.Route


class ToolbarViewModel: ViewModel(){
    val uiState = mutableStateOf(ToolbarUiState())

    fun updateLoading(loading:Boolean){
        uiState.value = uiState.value.copy(loading = loading)
    }

    fun onScreenChange(screenRoute: Route, title: String){
        uiState.value = uiState.value.copy(showBackButton = screenRoute != Route.BodyParts, title = title)
    }
}

data class ToolbarUiState(
    val showBackButton: Boolean = true,
    val title: String = "",
    val loading: Boolean = false
)