package com.example.exercisetracker.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.exercisetracker.frontend.composables.utils.AddButton

@Composable
fun Screen(
    loading: Boolean,
    onAddClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .shadow(20.dp)
                .zIndex(1f)
        ) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                color = if (loading) ProgressIndicatorDefaults.linearColor else Color.Transparent,
                trackColor = if (loading) ProgressIndicatorDefaults.linearTrackColor else Color.Transparent
            )

            AddButton(
                Modifier
                    .zIndex(0.5f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                onClick = {
                    onAddClick()
                }
            )
        }
        content()
    }
}