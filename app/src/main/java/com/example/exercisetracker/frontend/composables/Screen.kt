package com.example.exercisetracker.frontend.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Screen(
    onAddClick: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            onAddClick?.let{
                FloatingActionButton(
                    modifier = Modifier.size(100.dp, 50.dp),
                    onClick = it,
                    shape = RoundedCornerShape(100f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Localized description")
                }
            }
        }
    ) { padding ->
        Box(Modifier
            .padding(padding)
            .fillMaxSize()
        ){
            content()
        }
    }
}