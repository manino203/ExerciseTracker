package com.example.exercisetracker.frontend.composables.utils.chart


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class LineGraphStyle(

    val paddingValues: PaddingValues = PaddingValues(
        all = 12.dp
    ),
    val height: Dp = 300.dp,
    val xItemSpacing: Float = 150f,
    val crossHairPathEffect: PathEffect? = null,
    val colors: LinearGraphColors = LinearGraphColors(),
    val visibility: LinearGraphVisibility = LinearGraphVisibility(),
    //val yAxisLabelPosition: LabelPosition = LabelPosition.LEFT
)


data class LinearGraphVisibility(
    val isCrossHairVisible: Boolean = false,
    val isYAxisLabelVisible: Boolean = false,
    val isXAxisLabelVisible: Boolean = true,
    val isGridVisible: Boolean = false,

    val isHeaderVisible: Boolean = false,
)
