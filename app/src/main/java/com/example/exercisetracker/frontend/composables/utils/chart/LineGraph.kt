package com.example.exercisetracker.frontend.composables.utils.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun LineGraph(
    xAxisData: List<GraphData>,
    yAxisData: List<Number>,
    style: LineGraphStyle = LineGraphStyle(),
    onPointClicked: (Int?) -> Unit = {},
    isPointValuesVisible: Boolean = false,
    header: @Composable () -> Unit = {},
) {

    val paddingLeft: Dp = if (style.visibility.isYAxisLabelVisible) 20.dp else 0.dp
    val paddingBottom: Dp = if (style.visibility.isXAxisLabelVisible) 20.dp else 0.dp
    val paddingTop: Dp = 25.dp

    val offsetList = remember { mutableListOf<Offset>() }
    var isPointClicked by remember { mutableStateOf(false) }
    var clickedPoint by remember { mutableStateOf<Offset?>(null) }


    var scrolledToEnd by remember {
        mutableStateOf(false)
    }

    val lineWidth = remember {
        style.xItemSpacing * (xAxisData.size)
    }

    val minScroll = 50f

    var scrollOffset by remember {
        mutableStateOf(minScroll)
    }

    Column(
        modifier = Modifier
            .background(
                color = style.colors.backgroundColor
            )
            .fillMaxWidth()
            .height(style.height)
            .padding(style.paddingValues)
            .padding(top = 16.dp)
            .onGloballyPositioned {
                if (!scrolledToEnd) {
                    scrollOffset = if (lineWidth - it.size.width > 0) {
                        -(lineWidth - it.size.width)
                    } else {
                        minScroll
                    }
                    scrolledToEnd = true
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->

                    val maxScroll = if (lineWidth - size.width > 0) {
                        -(lineWidth - size.width)
                    } else {
                        minScroll
                    }

                    scrollOffset =
                        (scrollOffset + dragAmount).coerceIn(
                            maxScroll,
                            minScroll
                        )
                }
            }
            .clip(RectangleShape),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        if (style.visibility.isHeaderVisible) {
            header()
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()

                .padding(horizontal = 10.dp, vertical = paddingTop)

                .pointerInput(true) {


                    detectTapGestures { p1: Offset ->

                        val shortest = offsetList.find { p2: Offset ->

                            /** Pythagorean Theorem
                             * Using Pythagorean theorem to calculate distance between two points :
                             * p1 =  p1(x,y) which is the touch point
                             * p2 =  p2(x,y)) which is the point plotted on graph
                             * Formula: c = sqrt(a² + b²), where a = (p1.x - p2.x) & b = (p1.y - p2.y),
                             * c is the distance between p1 & p2
                            Pythagorean Theorem */

                            val distance =
                                abs(p1.x - (p2.x + scrollOffset + paddingLeft.toPx()))
//                                sqrt(
//                                (p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2)
//                            )
                            val pointRadius = 15.dp.toPx()

                            distance <= pointRadius
                        }
                        if (shortest != null) {

                            clickedPoint = shortest
                            isPointClicked = true

                            //
                            val index = offsetList.indexOf(shortest)
                            onPointClicked(index)
                        } else {
                            clickedPoint = null
                            isPointClicked = false
                            onPointClicked(null)
                        }

                    }
                },
        ) {


            //println("Entered scope")
            /**
             * style.xItemSpacing, yItemSpacing => space between each item that lies on the x and y axis
             * (size.width - 16.dp.toPx())
             *               ~~~~~~~~~~~~~ => padding saved for the end of the axis
             */

            val gridHeight = (size.height) - paddingBottom.toPx()
            val gridWidth = size.width

            // the maximum points for x and y axis to plot (maintain uniformity)
            val maxPointsSize: Int = yAxisData.size

            // maximum of the y data list
            val absMaxY = GraphHelper.getAbsoluteMax(yAxisData)

            val verticalStep = absMaxY.toInt() / maxPointsSize.toFloat()

            // generate y axis label
            val yAxisLabelList = mutableListOf<String>()

            for (i in 0..maxPointsSize) {
                val intervalValue = (verticalStep * i).roundToInt()
                println("interval - $intervalValue")
                yAxisLabelList.add(intervalValue.toString())
            }


//                gridWidth / (maxPointsSize - 1)
            val yItemSpacing = gridHeight / (yAxisLabelList.size - 1)


            /**
             * Drawing Grid lines inclined towards x axis
             */
            if (style.visibility.isGridVisible.first) {
                for (i in 0 until maxPointsSize) {

                    // lines inclined towards x axis
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(
                            (style.xItemSpacing * (i) + scrollOffset + paddingLeft.toPx()),
                            0f
                        ),
                        end = Offset(
                            (style.xItemSpacing * (i) + scrollOffset + paddingLeft.toPx()),
                            gridHeight
                        ),
                    )
                }
            }
            if (style.visibility.isGridVisible.second) {
                for (i in 0 until yAxisLabelList.size) {
                    // lines inclined towards y axis
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(paddingLeft.toPx(), gridHeight - yItemSpacing * (i)),
                        end = Offset(
                            (gridWidth + paddingLeft.toPx()),
                            gridHeight - yItemSpacing * (i)
                        ),
                    )
                }
            }

            /**
             * Drawing text labels over the x- axis
             */
            if (style.visibility.isXAxisLabelVisible) {
                val txtSize = 12.sp.toPx()
                for (i in 0 until maxPointsSize) {

//                    drawText(
//                        textMeasurer = textMeasurer,
//                        text = xAxisLabels[i],
//                        topLeft = Offset(
//                            (85 * i).toFloat(),
//                            50.dp.toPx()
//                        )
//                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        xAxisData[i].text.split("\n")[0],
                        (style.xItemSpacing * (i) + scrollOffset + paddingLeft.toPx()), // x
                        size.height, // y
                        Paint().apply {
                            color = android.graphics.Color.GRAY
                            textAlign = Paint.Align.CENTER
                            textSize = txtSize
                        }
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        xAxisData[i].text.split("\n")[1],
                        (style.xItemSpacing * (i) + scrollOffset + paddingLeft.toPx()), // x
                        size.height + txtSize, // y
                        Paint().apply {
                            color = android.graphics.Color.GRAY
                            textAlign = Paint.Align.CENTER
                            textSize = txtSize
                        }
                    )
                }
            }


            // plotting points


            offsetList.clear() // clearing list to avoid data duplication during recomposition

            for (i in 0 until maxPointsSize) {

                val x1 = style.xItemSpacing * i
                val y1 =
                    gridHeight - (yItemSpacing * (yAxisData[i].toFloat() / verticalStep))

                offsetList.add(
                    Offset(
                        x = x1,
                        y = y1
                    )
                )
            }

            /**
             * Drawing Gradient fill for the plotted points
             * Create Path from the offset list with start and end point to complete the path
             * then draw path using brush
             */
            val path = Path().apply {
                // starting point for gradient
                moveTo(
                    x = (0f + scrollOffset + paddingLeft.toPx()),
                    y = gridHeight
                )

                for (i in 0 until maxPointsSize) {
                    lineTo(
                        (offsetList[i].x + scrollOffset + paddingLeft.toPx()),
                        offsetList[i].y
                    )
                }

                // ending point for gradient
                lineTo(
                    x = (style.xItemSpacing * (yAxisData.size - 1) + scrollOffset + paddingLeft.toPx()),
                    y = gridHeight
                )

            }

            drawPath(
                path = path,
                brush = style.colors.fillGradient ?: Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Transparent)
                )
            )


            /**
             * drawing line connecting all circles/points
             */
            drawPoints(
                points = offsetList.map {
                    Offset((it.x + scrollOffset + paddingLeft.toPx()), it.y)
                },
                color = style.colors.lineColor,
                pointMode = PointMode.Polygon,
                strokeWidth = 2.dp.toPx(),
            )

            /**
             * Plotting points on the Graph
             */

            for (i in 0 until maxPointsSize) {

                drawCircle(
                    color = style.colors.pointColor,
                    radius = 5.dp.toPx(),
                    center = Offset(
                        (offsetList[i].x + scrollOffset + paddingLeft.toPx()),
                        offsetList[i].y
                    )
                )


                /**
                 * Draws point value above the point
                 */
                if (isPointValuesVisible) {
                    val pointTextSize = 12.sp.toPx()
                    val formattedValue = yAxisData[i].toString()
                    drawContext.canvas.nativeCanvas.drawText(
                        formattedValue,
                        (offsetList[i].x + scrollOffset + paddingLeft.toPx()), // x
                        offsetList[i].y - pointTextSize, // y
                        Paint().apply {
                            color = android.graphics.Color.GRAY
                            textAlign = Paint.Align.CENTER
                            textSize = pointTextSize
                        }
                    )
                }
            }

            /**
             * highlighting clicks when user clicked on the canvas
             */
            clickedPoint?.let {
                val circleRadius = 11.dp.toPx()
                if (style.visibility.isCrossHairVisible) {
                    drawLine(
                        color = style.colors.crossHairColor,
                        start = Offset(
                            (it.x + scrollOffset + paddingLeft.toPx()),
                            it.y + circleRadius
                        ),
                        end = Offset((it.x + scrollOffset + paddingLeft.toPx()), gridHeight),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = style.crossHairPathEffect
                    )
                }
                drawCircle(
                    color = style.colors.clickHighlightColor,
                    center = Offset((it.x + scrollOffset + paddingLeft.toPx()), it.y),
                    radius = circleRadius
                )

            }

            /**
             * Drawing rect at the start of graph
             */
            drawContext.canvas.nativeCanvas.drawRect(
                -paddingLeft.toPx(),
                -paddingTop.toPx(),
                paddingLeft.toPx(),
                size.height + paddingBottom.toPx(),
                Paint().apply {
                    color = style.colors.backgroundColor.toArgb()
                }
            )
            /**
             * Drawing text labels over the y- axis
             */
            if (style.visibility.isYAxisLabelVisible) {
                for (i in 0 until yAxisLabelList.size) {
                    drawContext.canvas.nativeCanvas.drawText(
                        yAxisLabelList[i],
                        0f, //x
                        gridHeight - yItemSpacing * (i + 0), //y
                        Paint().apply {
                            color = android.graphics.Color.GRAY
                            textAlign = Paint.Align.CENTER
                            textSize = 12.sp.toPx()
                        }
                    )
                }
            }

        }
    }


}


