package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.dialog_content.DialogContent
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogFormData
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogFormDataList
import com.example.exercisetracker.frontend.composables.utils.dialogs.TextFieldFormat


@Composable
fun ExercisesScreen(
    loading: MutableState<Boolean>,
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


    val nameString = stringResource(id = R.string.name)

    val canExpand = remember {
        mutableStateOf(true)
    }


    val expandedStates = remember {
        mapOf<String, MutableState<Boolean>>()
    }.toMutableMap()


    val currentEditData by remember {
        mutableStateOf(
            DialogFormDataList(
                DialogFormData(
                    TextFieldFormat.Str,
                    nameString,
                    mutableStateOf(
                        ""
                    )
                )
            )
        )
    }

    var currentExercise by remember {
        mutableStateOf<Exercise?>(null)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val resetDialog = {
        currentEditData.resetToDefaultValues()
        currentExercise = null
        dialogOpen = false
    }

    if (dialogOpen) {
        Dialog(
            onDismissRequest = resetDialog
        ) {
            DialogContent(
                values = currentEditData,
                onCalendarClick = {

                },
                onCancelClick = resetDialog,
                onSaveClick = if (currentExercise == null) { it ->
                    addItem(
                        DataClassFactory.createExercise(
                            it,
                            bodyPartPath
                        )
                    )
                    resetDialog()
                } else { data: DialogFormDataList ->
                    onEdit(data.items[0].state.value, currentExercise!!)
                    resetDialog()
                },
                onDeleteClick = if (currentExercise == null) {
                    null
                } else {
                    {
                        onDelete(currentExercise!!)
                        resetDialog()
                    }
                }
            )
        }
    }
    Column(
        Modifier
            .fillMaxSize()
    ) {
        LinearProgressIndicator(
            Modifier
                .fillMaxWidth(),
            color = if (loading.value) ProgressIndicatorDefaults.linearColor else Color.Transparent,
            trackColor = if (loading.value) ProgressIndicatorDefaults.linearTrackColor else Color.Transparent
        )

        ReorderableLazyColumn(
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
            },
            onAddClick = {
                resetDialog()
                dialogOpen = true
            }
        ) { index, exercise, dragModifier, elevation ->

            expandedStates[exercise.id] = remember { mutableStateOf(false) }

            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                onClick = onItemClick,
                onLongClick = {
                    dialogOpen = true
                    currentEditData.items[0].state.value = exercise.name
                    currentExercise = exercise

                },
                dragModifier = dragModifier,
                elevation = elevation,
                isExpanded = expandedStates[exercise.id]!!,
                canExpand = canExpand,
                onExpand = onAccordionExpand
            )
        }

    }

}