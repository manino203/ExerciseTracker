package com.example.exercisetracker.frontend.composables.dialog_content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogButtons(
    onDeleteClick: (() -> Unit)?,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    saveEnabled: Boolean = false
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
                borderColor = Color.Red
            ) {
                Text(
                    stringResource(id = R.string.delete),
                    color = Color.Red
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
            //save
            RoundWithBorders(
                Modifier
                    .weight(0.5f, true)
                    .combinedClickable(
                        enabled = saveEnabled,
                        onClick = {
                            onSaveClick.invoke()
                        }
                    ), roundCornerPercentage = 50
            ) {
                Text(stringResource(id = R.string.save))
            }


        }
    }

}