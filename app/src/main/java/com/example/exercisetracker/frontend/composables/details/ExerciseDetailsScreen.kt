package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.*

@Composable
fun ExerciseDetailsScreen(
    loading: MutableState<Boolean>,
    detailsList: SnapshotStateList<ExerciseDetails>,
    addItem: (ExerciseDetails) -> Unit,
    editItem: (ExerciseDetails, Int) -> Unit,
    deleteItem: (Int) -> Unit
) {

    var currentEditData by remember {
        mutableStateOf(
            listOf<EditDataWrapper>(
                EditDataWrapper(
                    TextFieldFormat.Float,
                    "Weight",
                    ""
                ),
                EditDataWrapper(
                    TextFieldFormat.Int,
                    "Reps",
                    ""
                ),
                EditDataWrapper(
                    TextFieldFormat.Date,
                    "Timestamp",
                    ""
                )
            )
        )
    }

    var currentDetails by remember {
        mutableStateOf<Pair<ExerciseDetails, Int>?>(null)
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
                    onSaveClick = if (currentDetails == null) { it ->
                        addItem(
                            ExerciseDetails(
                                it[0].value.toFloat(),
                                it[1].value.toInt(),
                                0L
                            )
                        )
                    } else { data ->
                        currentDetails?.let {
                            editItem(
                                ExerciseDetails(
                                    data[0].value.toFloat(),
                                    data[1].value.toInt(),
                                    0L
                                ),
                                it.second
                            )
                        }

                    },
                    onDeleteClick = if (currentDetails == null) {
                        null
                    } else {
                        {
                            currentDetails?.let {
                                deleteItem(it.second)
                            }

                        }
                    }
                )
            }
        }
        CustomLazyColumn(
            data = detailsList,
            onAddClick = {
                dialogOpen = true
            }
        )
        { index, details ->
            ExerciseDetailsItem(
                Modifier
                    .fillMaxWidth(),
                details,
                index,
                onClick = {
                    dialogOpen = true
                    currentEditData = listOf<EditDataWrapper>(
                        EditDataWrapper(
                            TextFieldFormat.Float,
                            "Weight",
                            "${details.weight}"
                        ),
                        EditDataWrapper(
                            TextFieldFormat.Int,
                            "Reps",
                            "${details.repetitions}"
                        ),
                        EditDataWrapper(
                            TextFieldFormat.Date,
                            "Date",
                            "${details.timestamp}"
                        )
                    )
                    currentDetails = Pair(details, index)
                }
            )
        }

    }
}