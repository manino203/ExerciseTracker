package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.Item
import com.example.exercisetracker.frontend.composables.utils.dialogs.ExerciseDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState

@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    exercise: Pair<Exercise, ExerciseDetails?>,
    onDelete: (Exercise) -> Unit,
    editItem: (String, Exercise) -> Boolean,
    onClick: (Exercise) -> Unit,
    dragModifier: Modifier? = null,
    elevation: State<Dp>,
    isExpanded: MutableState<Boolean> = mutableStateOf(false),
    canExpand: MutableState<Boolean> = mutableStateOf(true),
    onExpand: (Exercise, MutableState<Boolean>, MutableState<List<ExerciseDetails>>) -> Unit
) {

    val graphLoading = remember {
        mutableStateOf(true)
    }

    val exerciseDetails = remember {
        mutableStateOf<List<ExerciseDetails>>(emptyList())
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val formState = rememberFormState(form = ExerciseDialogForm(exercise.first))

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onDelete = {
                onDelete(exercise.first)
                dialogOpen = false
            },
            onDismiss = {
                dialogOpen = false
            },
            onConfirm = {
                editItem(formState.values.first().value, exercise.first)
            }
        )
    }

    Accordion(
        isExpanded = isExpanded.value && canExpand.value,
        header = {
            ExerciseItem(
                dragModifier = dragModifier,
                elevation = elevation,
                exercise = exercise,
                isExpanded = isExpanded.value,
                modifier = modifier.then(it),
                onClick = { onClick(exercise.first) },
                onLongClick = { dialogOpen = true }
            ) {
                isExpanded.value = !isExpanded.value
                onExpand(exercise.first, graphLoading, exerciseDetails)
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
                details = exerciseDetails.value
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExerciseItem(
    modifier: Modifier = Modifier,
    dragModifier: Modifier?,
    elevation: State<Dp>,
    exercise: Pair<Exercise, ExerciseDetails?>,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onExpand: () -> Unit
){


    Item(
        modifier,
        contentModifier =
        Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
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
                    text = exercise.first.name
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
                        (if (exercise.second != null) "${exercise.second!!.weight} kg" else "").toString(),
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
                        (exercise.second?.reps ?: "").toString(),
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
                        (exercise.second?.series ?: "").toString(),
                    )
                }
                Icon(
                    modifier = Modifier
                        .defaultMinSize(32.dp, 32.dp)
                        .clip(CircleShape)
                        .clickable {
                            onExpand()
                        }
                        .rotate(if (isExpanded) 180f else 0f),

                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "arrow-down",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}