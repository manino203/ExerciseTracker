package com.example.exercisetracker.backend.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val isLoading = mutableStateOf(false)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun exportData(failMessage: String, launchActivity: (Uri) -> Unit,){
        viewModelScope.launch(ioDispatcher){
            isLoading.value = true
            val exportFileUri = repo.getDataUri()
            isLoading.value = false
            exportFileUri?.let{
                launchActivity(it)
                return@launch
            }
            _toastMessage.emit(failMessage)
        }
    }

    fun importData(uri: Uri, failMessage: String){
        viewModelScope.launch(ioDispatcher) {
            if(!repo.importData(uri)){
                _toastMessage.emit(failMessage)
            }
        }
    }
}

