package com.example.exercisetracker.frontend.composables.dialog_content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.exercisetracker.frontend.composables.utils.DialogFormDataList


@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    values: DialogFormDataList,
    onCalendarClick: () -> Unit,
    onSaveClick: (DialogFormDataList) -> Unit,
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
            DialogForm(
                values = values,
                localFocusManager = LocalFocusManager.current,
                keyboardController = LocalSoftwareKeyboardController.current,
                onCalendarClick = onCalendarClick
            )

            DialogButtons(
                onDeleteClick = onDeleteClick,
                onSaveClick = {
                    onSaveClick(values)
                },
                onCancelClick = onCancelClick,
                values = values
            )


        }


    }


}