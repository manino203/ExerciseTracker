package com.example.exercisetracker.frontend.composables.dialog_content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    currentValues: DialogFormDataList,
    onCalendarClick: () -> Unit,
    onSaveClick: (DialogFormDataList) -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onCancelClick: () -> Unit
) {

    val newValues by remember {
        mutableStateOf(
            currentValues
        )
    }

    Surface(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(10)

    ) {

        Column{
            DialogForm(
                oldValues = currentValues,
                newValues = newValues,
                localFocusManager = LocalFocusManager.current,
                keyboardController = LocalSoftwareKeyboardController.current,
                onCalendarClick = onCalendarClick
            )

            DialogButtons(
                onDeleteClick = onDeleteClick,
                onSaveClick = {
                    onSaveClick(newValues)
                              },
                onCancelClick =  onCancelClick ,
                values = newValues
            )


        }




        }


}