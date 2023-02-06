package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.DateFormatter
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot

@Composable
fun ProgressGraph(
    modifier: Modifier = Modifier,
    _details: List<ExerciseDetails>
) {
    val details = mutableListOf<ExerciseDetails>(
        ExerciseDetails(0f, 0, 0L)
    ).apply {
        addAll(_details)
    }.toList()
    val dataPoints = remember {
        details.mapIndexed { index, exerciseDetails ->
            DataPoint(((index + 1) * 3).toFloat(), exerciseDetails.weight)
        }
    }

    val ySteps = 5


    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    dataPoints,
                    LinePlot.Connection(color = Color.Red),
                    LinePlot.Intersection(color = Color.Transparent),
                    LinePlot.Highlight(color = Color.Yellow){
//                    detaily sem
                    },

                )
            ),
            grid = LinePlot.Grid(Color.Gray, steps = ySteps),
            yAxis = LinePlot.YAxis(
                roundToInt = false,
                steps = ySteps
            ),
            xAxis = LinePlot.XAxis(
                steps = details.size,
                content = { min, offset, max ->

                    details.forEachIndexed { index, it ->

                        val value = DateFormatter.toDate(it.timestamp)

                        Text(
                            text = "${value.subSequence(0, 6)}\n ${value.subSequence(6, value.length)}",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.caption,
                            color = if (index != 0) MaterialTheme.colors.onSurface else Color.Transparent
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