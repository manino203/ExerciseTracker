package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.frontend.composables.utils.CustomLazyColumn

@Composable
fun BodyPartsScreen(
    bodyParts: SnapshotStateList<BodyPart>,
    onItemClick: (String) -> Unit
) {

    CustomLazyColumn(data = bodyParts) { index, bp ->
        BodyPartItem(
            label = bp.label,
            index = index
        ) {
            onItemClick(bp.path)
        }
    }
}