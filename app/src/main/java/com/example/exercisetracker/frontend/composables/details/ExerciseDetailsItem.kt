package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders
import java.text.SimpleDateFormat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseDetailsItem(
    modifier: Modifier = Modifier,
    details: ExerciseDetails,
    index: Int,
    onClick: (Int) -> Unit
) {


    RoundWithBorders(
        modifier
            .combinedClickable(
                onClick = {
                    onClick(index)
                }

            ),
        roundCornerPercentage = 40,
        contentPadding = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.dumbbell),
                        contentDescription = stringResource(id = R.string.weight),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colors.onPrimary
                        )
                    )
                    Text(
                        "${details.weight} kg",
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.repetitions),
                        contentDescription = stringResource(id = R.string.reps),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )
                    Text(
                        details.repetitions.toString(),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Column(
                    Modifier.align(Alignment.CenterVertically)
                )
                {
                    Text(
                        SimpleDateFormat.getDateInstance().format(details.timestamp),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }

}