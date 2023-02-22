package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.runtime.Composable
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn

@Composable
fun BodyPartsScreen(
    bodyParts: List<BodyPart>,
    onItemClick: (String) -> Unit,
    onSwap: (Int, Int) -> Unit,
    onDragEnd: () -> Unit
) {


    ReorderableLazyColumn(
        data = bodyParts,
        onSwap = onSwap,
        onDragEnd = { _, _ ->
            onDragEnd()
        }
    ) { _, bp, dh, elevation ->

        BodyPartItem(
            label = bp.label,
            dragModifier = dh,
            elevation = elevation
        ) {
            onItemClick(bp.path)
        }


    }

}