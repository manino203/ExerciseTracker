package com.example.exercisetracker.frontend.composables.dialog_content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.frontend.composables.utils.DialogFormDataList
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogButtons(
    onDeleteClick: (() -> Unit)?,
    onSaveClick: (DialogFormDataList) -> Unit,
    onCancelClick: () -> Unit,
    values: DialogFormDataList
) {

    Column(
        Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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
                borderColor = MaterialTheme.colors.error
            ) {
                Text(
                    stringResource(id = R.string.delete),
                    color = MaterialTheme.colors.error
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
                        enabled = values.items.all {
                            it.state.value.isNotEmpty()
                        },
                        onClick = {
                            onSaveClick(values)
                        }
                    ), roundCornerPercentage = 50
            ) {
                Text(stringResource(id = R.string.save))
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
                    stringResource(id = R.string.cancel)
                )
            }
        }
    }

}