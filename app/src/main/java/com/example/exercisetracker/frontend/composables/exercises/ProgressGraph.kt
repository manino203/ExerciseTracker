package com.example.exercisetracker.frontend.composables.exercises


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.DateFormatter
import com.example.exercisetracker.frontend.composables.utils.graph.DataPoint
import com.example.exercisetracker.frontend.composables.utils.graph.LineGraph
import com.example.exercisetracker.frontend.composables.utils.graph.LinePlot

@Composable
fun ProgressGraph(
    modifier: Modifier = Modifier,
    details: List<ExerciseDetails>
) {


    val dataPoints = remember {
        details.mapIndexed { index, exerciseDetails ->
            DataPoint(((index + 1) * 3).toFloat(), exerciseDetails.weight)
        }
    }

    val ySteps = 5

    if (details.isEmpty()) {
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(id = R.string.no_data_yet),
                color = MaterialTheme.colors.onPrimary
            )
        }
    } else {
        LineGraph(
            bgColor = MaterialTheme.colors.onSurface,
            plot = LinePlot(
                listOf(
                    LinePlot.Line(
                        dataPoints,
                        LinePlot.Connection(color = MaterialTheme.colors.primary),
                        LinePlot.Intersection(color = MaterialTheme.colors.secondary),
                        LinePlot.Highlight(color = Color.Yellow) {
//                    detaily sem
                        },

                        )
                ),
                grid = LinePlot.Grid(Color.Gray, steps = ySteps),
                yAxis = LinePlot.YAxis(
                    roundToInt = false,
                    steps = ySteps,
                    content = { min, offset, max ->
                        for (it in 0 until ySteps) {
                            val value = it * offset + min
                            Text(
                                text = value.toString(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.surface
                            )
                        }
                    }
                ),
                xAxis = LinePlot.XAxis(
                    steps = details.size,
                    content = { min, offset, max ->

                        details.forEachIndexed { index, it ->

                            val value = DateFormatter.toDate(it.timestamp)

                            Text(
                                text = "${value.subSequence(0, 6)}\n ${
                                    value.subSequence(
                                        6,
                                        value.length
                                    )
                                }",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.surface
                            )
                        }
                    }
                )
            ),
            modifier = modifier,
            onSelection = { xLine, points ->
                // Do whatever you want here
            }
        )
    }
}