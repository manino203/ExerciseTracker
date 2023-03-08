package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.dialog_content.DialogContent
import com.example.exercisetracker.frontend.composables.utils.AddButton
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
                onSaveClick = {
                    if (currentExercise == null) {
                        addItem(
                            DataClassFactory.createExercise(
                                currentEditData,
                                bodyPartPath
                            )
                        )
                        resetDialog()
                    } else {
                        onEdit(currentEditData.items[0].state.value, currentExercise!!)
                        resetDialog()
                    }
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
        Box(
            Modifier
                .shadow(20.dp)
                .zIndex(1f)
        ) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                color = if (loading.value) ProgressIndicatorDefaults.linearColor else Color.Transparent,
                trackColor = if (loading.value) ProgressIndicatorDefaults.linearTrackColor else Color.Transparent
            )



            AddButton(
                Modifier
                    .zIndex(0.5f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                onClick = {
                    resetDialog()
                    dialogOpen = true
                }
            )
        }

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