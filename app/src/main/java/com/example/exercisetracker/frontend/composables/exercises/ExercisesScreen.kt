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
import com.example.exercisetracker.frontend.composables.dialog_content.DialogContent
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
            DialogFormDataList(
                DialogFormData(
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

    val resetDialog = {
        currentEditData.resetToDefaultValues()
        currentExercise = null
        dialogOpen = false
    }

    if (loading.value) {
        CenterLoading()
    } else {

        if (dialogOpen) {
            Dialog(
                onDismissRequest = resetDialog
            ) {
                DialogContent(
                    currentValues = currentEditData,
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

        CustomLazyColumn(
            data = exercises,
            onAddClick = {
                resetDialog()
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
                    dialogOpen = true
                    currentEditData.items[0].state.value = exercise.name
                    currentExercise = exercise

                }
            )
        }

    }
}