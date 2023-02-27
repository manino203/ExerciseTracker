package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.frontend.composables.utils.Item

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyPartItem(
    modifier: Modifier = Modifier,
    label: String,
    dragModifier: Modifier? = null,
    elevation: State<Dp>,
    onClick: () -> Unit
) {
    Item(
        modifier,
        contentModifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = onClick
            ),
        contentPadding = 28.dp,
        dragModifier = dragModifier,
        elevation = elevation
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.secondary
                )


            }
        }
    }
}