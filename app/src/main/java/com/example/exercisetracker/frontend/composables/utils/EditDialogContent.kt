package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.backend.data.Exercise


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditDialogContent(
    modifier: Modifier = Modifier,
    currentValues: List<EditDataWrapper>,
    onDismiss: () -> Unit,
    onSaveClick: (List<EditDataWrapper>) -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {


    var newValues by remember {
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

            currentValues.forEachIndexed { index ,data ->
                var text by remember {
                    mutableStateOf(data.value)
                }


                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            label = {
                                Text(text = data.label)
                            },
                            value = text,
                            onValueChange = { fieldVal ->
                                if (data.format.correspondsWithFormat(fieldVal)) {
                                    newValues[index].value = fieldVal

                                    text = fieldVal
                                }

                            }
                        )
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
                                it.value.isNotEmpty()
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