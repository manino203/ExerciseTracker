package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.frontend.composables.utils.Item

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyPartItem(
    modifier: Modifier = Modifier,
    label: String,
    dragHandle: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Item(
        modifier
            .combinedClickable(
                onClick = onClick
            ),
        contentPadding = 28.dp,
        dragHandle = {
            dragHandle?.invoke()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = label,
                )


            }
        }
    }
}