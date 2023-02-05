package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.frontend.composables.utils.*


@Composable
fun ExercisesScreen(
    loading: MutableState<Boolean>,
    exercises: SnapshotStateList<Exercise>,
    bodyPartPath: String,
    onEdit: (String, Exercise) -> Unit,
    addItem: (Exercise) -> Unit,
    onItemClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit
) {


    val nameString = stringResource(id = R.string.name)

    val currentEditData by remember {
        mutableStateOf(
            EditDataList(
                EditDataWrapper(
                    TextFieldFormat.Str,
                    nameString,
                    mutableStateOf("")
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

    val onDismiss = {
        currentEditData.resetToDefaultValues()
        dialogOpen = false
    }

    if (loading.value) {
        CenterLoading()
    } else {

        if (dialogOpen) {
            Dialog(
                onDismissRequest = onDismiss
            ) {
                EditDialogContent(
                    currentValues = currentEditData.items,
                    onCalendarClick = {

                    },
                    onCancelClick = onDismiss,
                    onSaveClick = if (currentExercise == null) { it ->
                        addItem(
                            DataClassFactory.createExercise(
                                it,
                                bodyPartPath
                            )
                        )
                        onDismiss()
                    } else { data ->
                        data.find {
                            it.label == nameString
                        }?.let {
                            onEdit(it.state.value, currentExercise!!)
                        }
                        onDismiss()
                    },
                    onDeleteClick = if (currentExercise == null) {
                        null
                    } else {
                        {
                            onDelete(currentExercise!!)
                            onDismiss()
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
        ) { index, exercise ->
            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                index = index,
                onClick = onItemClick,
                onLongClick = {
                    currentEditData.items[0].state.value = it.name
                    currentExercise = exercise
                    dialogOpen = true
                }
            )
        }

    }
}