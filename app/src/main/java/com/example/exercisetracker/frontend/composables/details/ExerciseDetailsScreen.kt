package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.Screen
import com.example.exercisetracker.frontend.composables.utils.*
import com.example.exercisetracker.frontend.composables.utils.DateFormatter.Companion.dateFormat
import com.example.exercisetracker.frontend.composables.utils.dialogs.CalendarDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.DetailsDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsScreen(
    loading: Boolean,
    detailsList: SnapshotStateList<ExerciseDetails>,
    addItem: (ExerciseDetails) -> Unit,
    editItem: (ExerciseDetails, Int) -> Unit,
    deleteItem: (Int) -> Unit
) {

    val calendarState = rememberDatePickerState(Instant.now().toEpochMilli())


    val formState = rememberFormState(form = DetailsDialogForm(null))

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    var calendarOpen by remember {
        mutableStateOf(false)
    }

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onCalendarClick = {
                calendarOpen = true
            },
            onDismiss = { dialogOpen = false }
        ) {
            addItem(
                DataClassFactory.createExerciseDetails(formState.values.map { it.value })
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
                formState.values[3].value =
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
    Screen(
        loading = loading,
        onAddClick = {
            dialogOpen = true
        },
    ){

        ReorderableLazyColumn(
            Modifier.zIndex(0.5f),
            data = detailsList,
        )
        { index, details, _, elevation ->
            ExerciseDetailsItem(
                Modifier
                    .fillMaxWidth(),
                details,
                index,
                elevation,
                deleteItem,
                editItem
            )
        }
    }
}