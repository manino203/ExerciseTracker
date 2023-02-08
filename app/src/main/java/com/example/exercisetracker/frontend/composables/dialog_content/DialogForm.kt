package com.example.exercisetracker.frontend.composables.dialog_content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.frontend.composables.utils.DialogFormDataList
import com.example.exercisetracker.frontend.composables.utils.TextFieldFormat

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DialogForm(
    oldValues: DialogFormDataList,
    newValues: DialogFormDataList,
    localFocusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    onCalendarClick: () -> Unit
) {

    Column(
        Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val lastIndex = remember {
            if (oldValues.items.last().format == TextFieldFormat.Date) {
                oldValues.items.lastIndex - 1
            } else {
                oldValues.items.lastIndex
            }
        }

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        oldValues.items.forEachIndexed { index, data ->
            val imeAction = if (index == lastIndex) {
                ImeAction.Done
            } else ImeAction.Next

            val keyboardActions = KeyboardActions(
                onNext = { localFocusManager.moveFocus(FocusDirection.Down) },
                onDone = {
                    localFocusManager.clearFocus()
                    keyboardController?.hide()
                }
            )

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    modifier = if (index == 0) {
                        Modifier.focusRequester(focusRequester)
                    } else {
                        Modifier
                    }
                        .weight(0.8f),
                    shape = RoundedCornerShape(50),
                    label = {
                        Text(text = data.label)
                    },
                    enabled = data.format != TextFieldFormat.Date,
                    value = TextFieldValue(
                        data.state.value,
                        selection = TextRange(data.state.value.length)
                    ),
                    onValueChange = { fieldVal ->

                        if (data.format.correspondsWithFormat(fieldVal.text)) {
                            data.state.value = fieldVal.text
                            newValues.items[index].state.value = fieldVal.text
                        }

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = data.format.keyboardType,
                        imeAction = imeAction
                    ),
                    keyboardActions = keyboardActions
                )
                if (data.format == TextFieldFormat.Date) {
                    Image(
                        modifier = Modifier
                            .weight(0.2f)
                            .clip(CircleShape)
                            .combinedClickable(
                                onClick = onCalendarClick
                            ),
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = stringResource(id = R.string.calendar),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.background)
                    )
                }
            }
        }

    }
}