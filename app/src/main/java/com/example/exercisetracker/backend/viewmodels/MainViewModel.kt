package com.example.exercisetracker.backend.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ExerciseDataRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {


    val exportFileUri = mutableStateOf<Uri?>(null)

    fun exportData(){
        viewModelScope.launch(ioDispatcher){
            exportFileUri.value = null
            delay(100L)
            exportFileUri.value = repo.getDataUri()
        }
    }

    fun importData(){
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
    }

}

