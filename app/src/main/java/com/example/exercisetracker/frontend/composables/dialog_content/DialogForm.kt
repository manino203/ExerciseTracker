package com.example.exercisetracker.frontend.composables.dialog_content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.frontend.composables.utils.dialogs.DialogFormDataList
import com.example.exercisetracker.frontend.composables.utils.dialogs.TextFieldFormat

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DialogForm(
    values: DialogFormDataList,
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
            if (values.items.last().format == TextFieldFormat.Date) {
                values.items.lastIndex - 1
            } else {
                values.items.lastIndex
            }
        }

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        values.items.forEachIndexed { index, data ->
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
                val enabled = data.format != TextFieldFormat.Date
                var textValue by remember {
                    mutableStateOf(
                        TextFieldValue(
                            data.state.value,
                            selection = TextRange(data.state.value.length)
                        )
                    )
                }
                OutlinedTextField(
                    modifier = if (index == 0) {
                        Modifier.focusRequester(focusRequester)
                    } else {
                        Modifier
                    }
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                val text = textValue.text
                                textValue = textValue.copy(
                                    selection = TextRange(0, text.length)
                                )
                            }
                        }
                        .weight(0.8f),
                    shape = RoundedCornerShape(50),
                    label = {
                        Text(text = data.label)
                    },
                    enabled = enabled,
                    singleLine = true,
                    value =
                    if (enabled) textValue
                    else TextFieldValue(
                        data.state.value,
                        selection = TextRange(data.state.value.length)
                    ),
                    onValueChange = { fieldVal ->

                        if (data.format.correspondsWithFormat(fieldVal.text)) {
                            textValue = fieldVal
                            data.state.value = fieldVal.text
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }

    }
}