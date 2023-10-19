package com.example.exercisetracker.frontend.composables.utils.dialogs.dialog_content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogForm


@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    values: List<MutableState<String>>,
    form: DialogForm,
    onCalendarClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onCancelClick: () -> Unit
) {
    Surface(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(10)

    ) {

        Column {
            DialogFormComposable(
                values = values,
                form = form,
                localFocusManager = LocalFocusManager.current,
                keyboardController = LocalSoftwareKeyboardController.current,
                onCalendarClick = onCalendarClick
            )

            DialogButtons(
                onDeleteClick = onDeleteClick,
                onSaveClick = {
                    onSaveClick()
                },
                onCancelClick = onCancelClick,
                saveEnabled = values.all {
                    it.value.isNotEmpty()
                }
            )
        }
    }
}