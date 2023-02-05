package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    index: Int,
    onClick: (Exercise) -> Unit,
    onLongClick: (Exercise) -> Unit
) {
    RoundWithBorders(
        modifier
            .combinedClickable(
                onClick = { onClick(exercise) },
                onLongClick = { onLongClick(exercise) }
            ),
        roundCornerPercentage = 40,
        contentPadding = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = exercise.name,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painterResource(id = R.drawable.dumbbell),
                        contentDescription = stringResource(id = R.string.weight),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )
                    Text(
                        "20 kg",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Column(
                    Modifier
                        .padding(8.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(id = R.drawable.repetitions),
                        contentDescription = stringResource(id = R.string.reps),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )
                    Text(
                        "10",
                        color = MaterialTheme.colors.onPrimary
                    )
                }

            }
        }
    }
}