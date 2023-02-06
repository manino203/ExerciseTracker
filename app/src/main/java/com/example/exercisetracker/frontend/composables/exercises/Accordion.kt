package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    details: List<ExerciseDetails>,
    header: @Composable () -> Unit

) {


    Column(modifier) {
        header()
        AnimatedVisibility(visible = isExpanded) {
            RoundWithBorders(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                roundCornerPercentage = 10
            ) {
                ProgressGraph(
                    Modifier.fillMaxSize(),
                    _details = details
                )
            }
        }
    }
}