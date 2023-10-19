package com.example.exercisetracker.frontend.composables.utils.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.dialog_content.DialogContent

@Composable
fun FormDialog(
    formState: FormState,
    onCalendarClick: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        DialogContent(
            values = formState.values,
            form = formState.form,
            onCalendarClick = onCalendarClick,
            onCancelClick = onDismiss,
            onSaveClick = {
                onConfirm()
                onDismiss()
            },
            onDeleteClick = onDelete
        )
    }
}