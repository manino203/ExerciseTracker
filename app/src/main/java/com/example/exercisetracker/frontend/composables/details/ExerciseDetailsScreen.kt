package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.CenterLoading
import com.example.exercisetracker.frontend.composables.utils.CustomLazyColumn

@Composable
fun ExerciseDetailsScreen(
    loading: MutableState<Boolean>,
    detailsList: SnapshotStateList<ExerciseDetails>,
    onAddClick: () -> Unit,
    ) {
    if (loading.value) {
        CenterLoading()
    } else {
        CustomLazyColumn(
            data = detailsList,
            onAddClick = onAddClick
        )
        { details ->
            ExerciseDetailsItem(
                Modifier
                    .fillMaxWidth(),
                details
            )
        }

    }
}