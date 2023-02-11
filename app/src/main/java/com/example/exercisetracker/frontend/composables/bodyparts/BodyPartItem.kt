package com.example.exercisetracker.frontend.composables.bodyparts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyPartItem(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    RoundWithBorders(
        modifier
            .combinedClickable(
                onClick = onClick
            ),
        roundCornerPercentage = 40,
        contentPadding = PaddingValues(28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = MaterialTheme.colors.background
            )
        }
    }
}