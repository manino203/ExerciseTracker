package com.example.exercisetracker.frontend.composables.utils.dialogs

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

    DatePickerDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            Text(
                stringResource(id = R.string.cancel),
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = onCancelClick
                    )
            )

        },
        confirmButton = {
            Text(
                stringResource(id = R.string.ok),
                modifier = Modifier
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = onOkClick
                    )
            )
        }
    ) {
        DatePicker(calendarState)

    }
}