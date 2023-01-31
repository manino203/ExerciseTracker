package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.exercisetracker.frontend.composables.utils.CustomLazyColumn

@Composable
fun BodyPartsScreen(
    bodyParts: SnapshotStateList<String>,
    onItemClick: (String) -> Unit
) {

    CustomLazyColumn(data = bodyParts) { index, label ->
        BodyPartItem(
            label = label,
            index = index
        ) {
            onItemClick(label)
        }
    }
}