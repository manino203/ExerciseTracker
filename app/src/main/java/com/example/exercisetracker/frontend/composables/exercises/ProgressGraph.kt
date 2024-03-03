package com.example.exercisetracker.frontend.composables.exercises


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.DateFormatter
import com.example.exercisetracker.frontend.composables.utils.chart.*
import com.example.exercisetracker.frontend.composables.utils.setAlpha

@Suppress("UNUSED_PARAMETER")
@Composable
fun ProgressGraph(
    modifier: Modifier = Modifier,
    details: List<ExerciseDetails>
) {


//    val dataPoints = remember {
//        details.mapIndexed { index, exerciseDetails ->
//            DataPoint(((index + 1) * 3).toFloat(), exerciseDetails.weight)
//        }
//    }
//
//    val ySteps = 5

    val xAxis = remember {
        details.map {
            val date = DateFormatter.toDate(it.timestamp)
            GraphData.String(
                "${date.subSequence(0, 6)}\n ${
                    date.subSequence(
                        6,
                        date.length
                    )
                }"
            )


        }
    }
    val yAxis = remember {
        details.map {
            it.weight
        }
    }

    if (details.isEmpty()) {
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(id = R.string.no_data_yet),
            )
        }
    } else {
        var currentItemIndex by remember {
            mutableStateOf<Int?>(null)
        }
        LineGraph(

            style = LineGraphStyle(
                visibility = LinearGraphVisibility(
                    isHeaderVisible = true,
                    isCrossHairVisible = true,
                    isXAxisLabelVisible = true,
                    isYAxisLabelVisible = true,
                ),
                colors = LinearGraphColors(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    fillGradient = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary.setAlpha(0.5f),
                            Color.Transparent
                        )
                    ),
                    lineColor = MaterialTheme.colorScheme.secondary,
                    pointColor = MaterialTheme.colorScheme.secondary,
                    crossHairColor = Color.Gray,
                    clickHighlightColor = Color.Gray.setAlpha(0.5f)
                )
            ),
            xAxisData = xAxis,
            yAxisData = yAxis,
            isPointValuesVisible = true,
            onPointClicked = { index ->
                currentItemIndex = index
            }
        ) {

            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(
                        text = if (currentItemIndex != null) {
                            "${
                                stringResource(id = R.string.reps)
                            }: ${
                                details[currentItemIndex!!].reps
                            }"
                        } else {
                            ""
                        }
                    )

                    Text(
                        text = if (currentItemIndex != null) {
                            "${
                                stringResource(id = R.string.series)
                            }: ${
                                details[currentItemIndex!!].series
                            } "
                        } else {
                            ""
                        }

                    )
                }
                Divider(
                    Modifier
                        .padding(
                            start = 2.dp,
                            end = 2.dp
                        )
                )

            }


        }
    }
}

