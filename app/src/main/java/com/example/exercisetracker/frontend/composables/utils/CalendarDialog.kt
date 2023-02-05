package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarDialog(
    calendarState: DatePickerState,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit

) {
    val cancelString = stringResource(id = R.string.cancel)
    val okString = stringResource(id = R.string.ok)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            Text(
                cancelString,
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = onCancelClick
                    )
            )

        },
        confirmButton = {
            Text(
                okString,
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = onOkClick
                    )
            )
        }
    ) {
        DatePicker(datePickerState = calendarState)

    }
}