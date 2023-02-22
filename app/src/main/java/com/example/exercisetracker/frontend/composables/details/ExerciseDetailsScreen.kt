package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.dialog_content.DialogContent
import com.example.exercisetracker.frontend.composables.utils.*
import com.example.exercisetracker.frontend.composables.utils.DateFormatter.Companion.dateFormat
import com.example.exercisetracker.frontend.composables.utils.dialogs.CalendarDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogFormData
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogFormDataList
import com.example.exercisetracker.frontend.composables.utils.dialogs.TextFieldFormat
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

    val weightString = stringResource(R.string.weight)
    val repsString = stringResource(R.string.reps)
    val seriesString = stringResource(id = R.string.series)
    val dateString = stringResource(R.string.date)

    val calendarState = rememberDatePickerState(Instant.now().toEpochMilli())
    val currentEditData by remember {
        mutableStateOf(
            DialogFormDataList(
                DialogFormData(
                    TextFieldFormat.Float,
                    weightString,
                    mutableStateOf("")
                ),
                DialogFormData(
                    TextFieldFormat.Int,
                    repsString,
                    mutableStateOf("")
                ),
                DialogFormData(
                    TextFieldFormat.Int,
                    seriesString,
                    mutableStateOf("")
                ),
                DialogFormData(
                    TextFieldFormat.Date,
                    dateString,
                    mutableStateOf(

                        DateFormatter.toDate(
                            Instant.now().toEpochMilli()
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

    val resetDialog = {
        currentDetails = null
        currentEditData.resetToDefaultValues()
        dialogOpen = false
    }


    if (dialogOpen) {
        Dialog(
            onDismissRequest = resetDialog
        ) {
            DialogContent(
                values = currentEditData,
                onCalendarClick = {
                    calendarOpen = true
                },
                onCancelClick = resetDialog,
                onSaveClick = if (currentDetails == null) { it ->
                    addItem(
                        DataClassFactory.createExerciseDetails(
                            it,
                            calendarState.selectedDateMillis ?: 0
                        )
                    )
                    resetDialog()
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
                    resetDialog()
                },
                onDeleteClick = if (currentDetails == null) {
                    null
                } else {
                    {
                        currentDetails?.let {
                            deleteItem(it.second)
                        }
                        resetDialog()
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
                currentEditData.items[3].state.value =
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
    Column(
        Modifier.fillMaxSize()
    ) {

        LinearProgressIndicator(
            Modifier
                .fillMaxWidth(),
            color = if (loading.value) ProgressIndicatorDefaults.linearColor else Color.Transparent,
            trackColor = if (loading.value) ProgressIndicatorDefaults.linearTrackColor else Color.Transparent
        )

        ReorderableLazyColumn(
            data = detailsList,
            onAddClick = {
                dialogOpen = true
            }
        )
        { index, details, _, elevation ->
            ExerciseDetailsItem(
                Modifier
                    .fillMaxWidth(),
                details,
                index,
                elevation,
                onClick = {
                    dialogOpen = true
                    currentEditData.items[0].state.value = "${details.weight}"
                    currentEditData.items[1].state.value = "${details.reps}"
                    currentEditData.items[2].state.value = "${details.series}"
                    currentEditData.items[3].state.value =
                        DateFormatter.toDate(details.timestamp)
                    currentDetails = Pair(details, index)
                }
            )
        }
    }

}