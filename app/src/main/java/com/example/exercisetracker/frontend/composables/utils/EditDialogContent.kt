package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditDialogContent(
    modifier: Modifier = Modifier,
    currentValues: List<EditDataWrapper>,
    onCalendarClick: () -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: (List<EditDataWrapper>) -> Unit,
    onDeleteClick: (() -> Unit)? = null
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
        shape = RoundedCornerShape(20)

    ) {

        Column(
            Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            currentValues.forEachIndexed { index, data ->


                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(0.8f),
                        shape = RoundedCornerShape(50),
                        label = {
                            Text(text = data.label)
                        },
                        enabled = data.format != TextFieldFormat.Date,
                        value = data.state.value,
                        onValueChange = { fieldVal ->

                            if (data.format.correspondsWithFormat(fieldVal)) {
                                newValues[index].state.value = fieldVal

                                data.state.value = fieldVal

                            }

                        }
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
                            contentDescription = "calendar"
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
                                onDismiss()
                            }
                        ), roundCornerPercentage = 50,
                    borderColor = Color.Red
                ) {
                    Text(
                        "Delete",
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
                                onDismiss()
                            }
                        ), roundCornerPercentage = 50
                ) {
                    Text("Save")
                }
                //cancel
                RoundWithBorders(
                    Modifier
                        .weight(0.5f, true)
                        .combinedClickable(
                            onClick = onDismiss
                        ), roundCornerPercentage = 50
                ) {
                    Text(
                        "Cancel",

                        )
                }
            }
        }
    }

}