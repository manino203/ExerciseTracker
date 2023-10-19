package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.Screen
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn
import com.example.exercisetracker.frontend.composables.utils.dialogs.ExerciseDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState


@Composable
fun ExercisesScreen(
    loading: Boolean,
    exercises: SnapshotStateList<Exercise>,
    bodyPartPath: String,
    onEdit: (String, Exercise) -> Unit,
    addItem: (Exercise) -> Unit,
    onItemClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    onSwap: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
    onAccordionExpand: (Exercise, MutableState<Boolean>, SnapshotStateList<ExerciseDetails>) -> Unit
) {

    val canExpand = remember {
        mutableStateOf(true)
    }

    val expandedStates = remember {
        mapOf<String, MutableState<Boolean>>()
    }.toMutableMap()

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val formState = rememberFormState(form = ExerciseDialogForm(null))

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onDismiss = {
                dialogOpen = false
            },
            onConfirm = {
                addItem(DataClassFactory.createExercise(formState.values.map { it.value }, bodyPartPath))
            }
        )
    }
    Screen(
        loading = loading,
        onAddClick = {dialogOpen = true},
    ){
        ReorderableLazyColumn(
            Modifier
                .zIndex(0.5f),
            data = exercises,
            onSwap = { from, to ->
                onSwap(from, to)
            },
            onDragStart = {
                expandedStates.forEach {
                    it.value.value = false
                }
                canExpand.value = false
            },
            onDragEnd = { _, _ ->
                canExpand.value = true
                onDragEnd()
            }
        ) { _, exercise, dragModifier, elevation ->

            expandedStates[exercise.id] = remember { mutableStateOf(false) }

            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                onClick = onItemClick,
                dragModifier = dragModifier,
                elevation = elevation,
                isExpanded = expandedStates[exercise.id]!!,
                canExpand = canExpand,
                onExpand = onAccordionExpand,
                editItem = onEdit,
                onDelete = onDelete
            )
        }
    }
}