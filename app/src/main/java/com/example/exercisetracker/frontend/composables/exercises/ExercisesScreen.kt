package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier

import com.example.exercisetracker.frontend.composables.utils.CenterLoading
import com.example.exercisetracker.frontend.composables.utils.CustomLazyColumn


@Composable
fun ExercisesScreen(
    loading: MutableState<Boolean>,
    exercises: SnapshotStateList<String>,
    onAddClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    if (loading.value) {
        CenterLoading()
    } else {

        CustomLazyColumn(
            data = exercises,
            onAddClick = onAddClick
        )
        { exercise ->
            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                onClick = onItemClick
            )
        }
    }
}