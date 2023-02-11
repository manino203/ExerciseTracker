package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.CenterLoading
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    index: Int,
    onClick: (Exercise) -> Unit,
    onLongClick: (Exercise) -> Unit,
    onExpand: (Exercise, MutableState<Boolean>, SnapshotStateList<ExerciseDetails>) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var graphLoading = remember {
        mutableStateOf(true)
    }

    val exerciseDetails = remember {
        mutableStateListOf<ExerciseDetails>()
    }

    Accordion(
        isExpanded = isExpanded,
        header = {
            RoundWithBorders(
                modifier
                    .combinedClickable(
                        onClick = { onClick(exercise) },
                        onLongClick = { onLongClick(exercise) }
                    ),
                roundCornerPercentage = 40,
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = exercise.name,
                        color = MaterialTheme.colors.background
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painterResource(id = R.drawable.dumbbell),
                            contentDescription = stringResource(id = R.string.weight),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.background)
                        )
                        Text(
                            (if (exercise.latestDetails != null) "${exercise.latestDetails.weight} kg" else "").toString(),
                            color = MaterialTheme.colors.background
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.background)
                        )
                        Text(
                            (exercise.latestDetails?.reps ?: "").toString(),
                            color = MaterialTheme.colors.background
                        )
                    }
                    Column(
                        Modifier
                            .padding(8.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(id = R.drawable.series),
                            contentDescription = stringResource(id = R.string.series),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.background)
                        )
                        Text(
                            (exercise.latestDetails?.series ?: "").toString(),
                            color = MaterialTheme.colors.background
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .defaultMinSize(32.dp, 32.dp)
                            .clip(CircleShape)
                            .clickable {
                                isExpanded = !isExpanded
                                onExpand(exercise, graphLoading, exerciseDetails)
                            }
                            .rotate(if (isExpanded) 180f else 0f),

                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = "arrow-down",
                        tint = MaterialTheme.colors.background
                    )

                }


            }
        }
    ) {
        if (graphLoading.value) {
            CenterLoading()
        } else {
            ProgressGraph(
                Modifier.fillMaxSize(),
                details = exerciseDetails.apply {
                    sortBy {
                        it.timestamp
                    }
                }
            )
        }
    }
}