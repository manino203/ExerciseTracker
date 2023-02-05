package com.example.exercisetracker.frontend.composables.utils

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun EditDialogContent(
    modifier: Modifier = Modifier,
    currentValues: List<EditDataWrapper>,
    onCalendarClick: () -> Unit,
    onSaveClick: (List<EditDataWrapper>) -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onCancelClick: () -> Unit
) {
    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val deleteString = stringResource(id = R.string.delete)
    val saveString = stringResource(id = R.string.save)
    val cancelString = stringResource(id = R.string.cancel)

    val newValues by remember {
        mutableStateOf(
            currentValues
        )
    }

    Surface(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20)

    ) {

        Column(
            Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val lastIndex = remember {
                if (currentValues.last().format == TextFieldFormat.Date) {
                    currentValues.lastIndex - 1
                } else {
                    currentValues.lastIndex
                }
            }

            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            currentValues.forEachIndexed { index, data ->
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


                    val textFieldValue = remember {
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
                            .weight(0.8f),
                        shape = RoundedCornerShape(50),
                        label = {
                            Text(text = data.label)
                        },
                        enabled = data.format != TextFieldFormat.Date,
                        value = textFieldValue.value,
                        onValueChange = { fieldVal ->

                            if (data.format.correspondsWithFormat(fieldVal.text)) {
                                textFieldValue.value = fieldVal
                                newValues[index].state.value = fieldVal.text
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                        )
                    }
                }
            }

            // delete
            if (onDeleteClick != null) {
                RoundWithBorders(
                    Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                onDeleteClick()

                            }
                        ), roundCornerPercentage = 50,
                    borderColor = Color.Red
                ) {
                    Text(
                        deleteString,
                        color = Color.Red
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //save
                RoundWithBorders(
                    Modifier
                        .weight(0.5f, true)
                        .combinedClickable(
                            enabled = currentValues.all {
                                it.state.value.isNotEmpty()
                            },
                            onClick = {
                                onSaveClick(newValues)

                            }
                        ), roundCornerPercentage = 50
                ) {
                    Text(saveString)
                }
                //cancel
                RoundWithBorders(
                    Modifier
                        .weight(0.5f, true)
                        .combinedClickable(
                            onClick = onCancelClick
                        ), roundCornerPercentage = 50
                ) {
                    Text(
                        cancelString
                    )
                }
            }
        }
    }

}