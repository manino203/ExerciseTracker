package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.Item
import com.example.exercisetracker.frontend.composables.utils.dialogs.ExerciseDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    onDelete: (Exercise) -> Unit,
    editItem: (String, Exercise) -> Unit,
    onClick: (Exercise) -> Unit,
    dragModifier: Modifier? = null,
    elevation: State<Dp>,
    isExpanded: MutableState<Boolean> = mutableStateOf(false),
    canExpand: MutableState<Boolean> = mutableStateOf(true),
    onExpand: (Exercise, MutableState<Boolean>, SnapshotStateList<ExerciseDetails>) -> Unit
) {


    val graphLoading = remember {
        mutableStateOf(true)
    }

    val exerciseDetails = remember {
        mutableStateListOf<ExerciseDetails>()
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val formState = rememberFormState(form = ExerciseDialogForm(exercise))

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onDelete = {
                onDelete(exercise)
                dialogOpen = false
            },
            onDismiss = {
                dialogOpen = false
            },
            onConfirm = {
                editItem(formState.values.first().value, exercise)
            }
        )
    }

    Accordion(
        isExpanded = isExpanded.value && canExpand.value,
        header = {
            Item(
                modifier
                    .then(it),
                contentModifier =
                Modifier
                    .combinedClickable(
                        onClick = { onClick(exercise) },
                        onLongClick = {
                            dialogOpen = true
                        }
                    ),
                elevation = elevation,
                contentPadding = 16.dp,
                dragModifier = dragModifier
            ) {

                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = exercise.name
                        )
                    }
                    Divider()
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painterResource(id = R.drawable.dumbbell),
                                contentDescription = stringResource(id = R.string.weight),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                            Text(
                                (if (exercise.latestDetails != null) "${exercise.latestDetails.weight} kg" else "").toString(),
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
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                            Text(
                                (exercise.latestDetails?.reps ?: "").toString(),
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
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                            Text(
                                (exercise.latestDetails?.series ?: "").toString(),
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .defaultMinSize(32.dp, 32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    isExpanded.value = !isExpanded.value
                                    onExpand(exercise, graphLoading, exerciseDetails)
                                }
                                .rotate(if (isExpanded.value) 180f else 0f),

                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "arrow-down",
                            tint = MaterialTheme.colorScheme.onBackground
                        )


                    }
                }
            }
        }
    ) {
        if (graphLoading.value) {
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
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