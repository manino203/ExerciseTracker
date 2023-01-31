package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.frontend.composables.utils.*


@Composable
fun ExercisesScreen(
    loading: MutableState<Boolean>,
    exercises: SnapshotStateList<Exercise>,
    bodyPart: String,
    onEdit: (String, Exercise) -> Unit,
    addItem: (Exercise) -> Unit,
    onItemClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit
) {

    var currentEditData by remember {
        mutableStateOf(
            listOf<EditDataWrapper>(
                EditDataWrapper(
                    TextFieldFormat.Str,
                    "Name",
                    ""
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

    if (loading.value) {
        CenterLoading()
    } else {

        if (dialogOpen) {
            Dialog(
                onDismissRequest = {
                    dialogOpen = false
                }
            ) {
                EditDialogContent(
                    currentValues = currentEditData,
                    onDismiss = {
                        dialogOpen = false
                    },
                    onSaveClick = if (currentExercise == null) { it ->
                        addItem(
                            Exercise(
                                it[0].value,
                                bodyPart
                            )
                        )
                    } else { data ->
                        data.find {
                            it.label == "Name"
                        }?.let {
                            onEdit(it.value, currentExercise!!)
                        }
                    },
                    onDeleteClick = if (currentExercise == null) {
                        null
                    } else {
                        {
                            onDelete(currentExercise!!)
                        }
                    }
                )
            }
        }

        CustomLazyColumn(
            data = exercises,
            onAddClick = {
                dialogOpen = true
            }
        )
        { index, exercise ->
            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                index = index,
                onClick = onItemClick,
                onLongClick = {
                    currentEditData = listOf(
                        EditDataWrapper(
                            TextFieldFormat.Str,
                            "Name",
                            it.name
                        )
                    )
                    currentExercise = exercise
                    dialogOpen = true
                }
            )
        }

    }
}