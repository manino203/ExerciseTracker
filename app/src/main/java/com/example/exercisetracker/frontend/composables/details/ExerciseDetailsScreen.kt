package com.example.exercisetracker.frontend.composables.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExerciseDetailsScreen(
    loading: MutableState<Boolean>,
    detailsList: SnapshotStateList<ExerciseDetails>,
    addItem: (ExerciseDetails) -> Unit,
    editItem: (ExerciseDetails, Int) -> Unit,
    deleteItem: (Int) -> Unit
) {
    val dateFormat = "dd/MM/yyyy"
    val calendarState = rememberDatePickerState(Instant.now().toEpochMilli())
    val currentEditData by remember {
        mutableStateOf(
            listOf(
                EditDataWrapper(
                    TextFieldFormat.Float,
                    "Weight",
                    mutableStateOf("")
                ),
                EditDataWrapper(
                    TextFieldFormat.Int,
                    "Reps",
                    mutableStateOf("")
                ),
                EditDataWrapper(
                    TextFieldFormat.Date,
                    "Date",
                    mutableStateOf(
                        SimpleDateFormat(dateFormat).format(
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
                    onCalendarClick = {
                        calendarOpen = true
                    },
                    onDismiss = {
                        dialogOpen = false
                    },
                    onSaveClick = if (currentDetails == null) { it ->
                        addItem(

                            //prerobit

                            ExerciseDetails(
                                it[0].state.value.toFloat(),
                                it[1].state.value.toInt(),
                                calendarState.selectedDateMillis ?: 0
                            )
                        )
                    } else { data ->
                        currentDetails?.let {
                            editItem(

                                //prerobit

                                ExerciseDetails(
                                    data[0].state.value.toFloat(),
                                    data[1].state.value.toInt(),
                                    calendarState.selectedDateMillis ?: 0
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
        if (calendarOpen) {
            DatePickerDialog(
                onDismissRequest = {
                    calendarOpen = false
                },
                dismissButton = {
                    Text(
                        "Cancel",
                        modifier = Modifier
                            .padding(16.dp)
                            .combinedClickable(
                                onClick = {
                                    calendarOpen = false
                                }
                            )
                    )

                },
                confirmButton = {
                    Text(
                        "OK",
                        modifier = Modifier
                            .padding(16.dp)
                            .combinedClickable(
                                onClick = {

                                    currentEditData[2].state.value =
                                        SimpleDateFormat(dateFormat).format(
                                            calendarState.selectedDateMillis ?: 0
                                        )

                                    calendarOpen = false
                                }
                            )
                    )
                }
            ) {
                DatePicker(datePickerState = calendarState)

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

                    // reset values

                    dialogOpen = true
                    currentEditData[0].state.value = "${details.weight}"
                    currentEditData[1].state.value = "${details.repetitions}"
                    currentEditData[2].state.value =
                        SimpleDateFormat(dateFormat).format(details.timestamp)
                    currentDetails = Pair(details, index)
                }
            )
        }

    }
}