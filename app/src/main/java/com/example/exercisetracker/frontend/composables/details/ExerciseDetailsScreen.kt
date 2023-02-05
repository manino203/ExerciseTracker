package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsScreen(
    loading: MutableState<Boolean>,
    detailsList: SnapshotStateList<ExerciseDetails>,
    addItem: (ExerciseDetails) -> Unit,
    editItem: (ExerciseDetails, Int) -> Unit,
    deleteItem: (Int) -> Unit
) {

    val weightString = stringResource(id = R.string.weight)
    val repsString = stringResource(id = R.string.reps)
    val dateString = stringResource(id = R.string.date)


    val dateFormat = "dd/MM/yyyy"
    val calendarState = rememberDatePickerState(Instant.now().toEpochMilli())
    val currentEditData by remember {
        mutableStateOf(
            EditDataList(
                EditDataWrapper(
                    TextFieldFormat.Float,
                    weightString,
                    mutableStateOf("")
                ),
                EditDataWrapper(
                    TextFieldFormat.Int,
                    repsString,
                    mutableStateOf("")
                ),
                EditDataWrapper(
                    TextFieldFormat.Date,
                    dateString,
                    mutableStateOf(

                        SimpleDateFormat(dateFormat, Locale.getDefault()).format(
                            calendarState.selectedDateMillis ?: 0
                        )
                    )
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

    var calendarOpen by remember {
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
                        calendarOpen = true
                    },
                    onCancelClick = onDismiss,
                    onSaveClick = if (currentDetails == null) { it ->
                        addItem(
                            DataClassFactory.createExerciseDetails(
                                it,
                                calendarState.selectedDateMillis ?: 0
                            )
                        )
                        onDismiss()
                    } else { data ->
                        currentDetails?.let {
                            editItem(
                                DataClassFactory.createExerciseDetails(
                                    data,
                                    calendarState.selectedDateMillis ?: 0
                                ),
                                it.second
                            )
                        }
                        onDismiss()
                    },
                    onDeleteClick = if (currentDetails == null) {
                        null
                    } else {
                        {
                            currentDetails?.let {
                                deleteItem(it.second)
                            }
                            onDismiss()
                        }
                    }
                )
            }
        }
        if (calendarOpen) {
            CalendarDialog(
                calendarState = calendarState,
                onDismiss = {
                    calendarOpen = false
                },
                onOkClick = {
                    currentEditData.items[2].state.value =
                        SimpleDateFormat(dateFormat, Locale.getDefault()).format(
                            calendarState.selectedDateMillis ?: 0
                        )

                    calendarOpen = false
                },
                onCancelClick = {
                    calendarOpen = false
                }
            )
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

                    // reset values

                    dialogOpen = true
                    currentEditData.items[0].state.value = "${details.weight}"
                    currentEditData.items[1].state.value = "${details.repetitions}"
                    currentEditData.items[2].state.value =
                        SimpleDateFormat(dateFormat, Locale.getDefault()).format(details.timestamp)
                    currentDetails = Pair(details, index)
                }
            )
        }

    }
}