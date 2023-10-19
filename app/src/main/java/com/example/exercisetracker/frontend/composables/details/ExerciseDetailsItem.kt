package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.DateFormatter
import com.example.exercisetracker.frontend.composables.utils.Item
import com.example.exercisetracker.frontend.composables.utils.dialogs.CalendarDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.DetailsDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsItem(
    modifier: Modifier = Modifier,
    details: ExerciseDetails,
    index: Int,
    elevation: State<Dp>,
    onDelete: (Int) -> Unit,
    editItem: (ExerciseDetails, Int) -> Unit,
) {

    var dialogOpen by remember {
        mutableStateOf(false)
    }
    var calendarOpen by remember {
        mutableStateOf(false)
    }

    val calendarState = rememberDatePickerState(Instant.now().toEpochMilli())

    val formState = rememberFormState(form = DetailsDialogForm(details))

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onCalendarClick = {
                calendarOpen = true
            },
            onDelete = {
                onDelete(index)
                dialogOpen = false
            },
            onDismiss = {
                dialogOpen = false
            },
            onConfirm = {
                editItem(DataClassFactory.createExerciseDetails(formState.values.map { it.value }) , index)
            }
        )
    }

    if (calendarOpen) {
        CalendarDialog(
            calendarState = calendarState,
            onDismiss = {
                calendarOpen = false
            },
            onOkClick = {
                formState.values[3].value =
                    SimpleDateFormat(DateFormatter.dateFormat, Locale.getDefault()).format(
                        calendarState.selectedDateMillis ?: 0
                    )
                calendarOpen = false
            },
            onCancelClick = {
                calendarOpen = false
            }
        )
    }

    Item(
        modifier,
        contentModifier = Modifier
            .combinedClickable(
                onClick = {
                    dialogOpen = true
                }
            ),
        elevation = elevation
    ) {
        Row(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.dumbbell),
                    contentDescription = stringResource(id = R.string.weight),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    "${details.weight} kg"
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.repetitions),
                    contentDescription = stringResource(id = R.string.reps),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
                Text(
                    details.reps.toString()
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.series),
                    contentDescription = stringResource(id = R.string.series),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    "${details.series}"
                )
            }
            Column(
                Modifier.align(Alignment.CenterVertically)
            )
            {
                Text(
                    SimpleDateFormat.getDateInstance().format(details.timestamp)
                )
            }
        }

    }

}