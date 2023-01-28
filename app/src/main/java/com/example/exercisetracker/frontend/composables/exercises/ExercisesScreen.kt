package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.frontend.composables.utils.*


@Composable
fun ExercisesScreen(
    loading: MutableState<Boolean>,
    exercises: SnapshotStateList<String>,
    onAddClick: () -> Unit,
    addItem: (String) -> Unit,
    onItemClick: (String) -> Unit
) {

    var currentExercise by remember {
        mutableStateOf(
            listOf(
                EditDataWrapper(
                    TextFieldFormat.Str,
                    "Name",
                    ""
                )
            )
        )
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
                    values = currentExercise,
                    onDismiss = {
                        dialogOpen = false
                    },
                    onSaveClick = addItem

                )
            }
        }

        CustomLazyColumn(
            data = exercises,
            onAddClick = {
                dialogOpen = true
            }
        )
        { exercise ->
            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                onClick = onItemClick,
                onLongClick = {
                    currentExercise = listOf(
                        EditDataWrapper(
                            TextFieldFormat.Str,
                            "Name",
                            it
                        )
                    )
                    dialogOpen = true
                }
            )
        }

    }
}