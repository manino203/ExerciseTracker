package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.runtime.Composable
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn

@Composable
fun BodyPartsScreen(
    bodyParts: List<BodyPart>,
    onItemClick: (String) -> Unit,
    onSwap: (Int, Int) -> Unit
) {


    ReorderableLazyColumn(
        data = bodyParts,
        onSwap = onSwap
    ) { _, bp, dh ->

        BodyPartItem(
            label = bp.label,
            dragHandle = dh
        ) {
            onItemClick(bp.path)
        }
    }

//    DragDropColumn(items = bodyParts, onSwap = onSwap) { bp ->
//        BodyPartItem(
//            label = bp.label
//        ) {
//            onItemClick(bp.path)
//        }
//    }


}