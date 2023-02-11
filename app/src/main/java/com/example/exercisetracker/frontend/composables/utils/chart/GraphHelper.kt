package com.example.exercisetracker.frontend.composables.utils.chart

import kotlin.math.roundToInt

object GraphHelper {

    fun getAbsoluteMax(list: List<Number>): Number {
        return list.maxByOrNull {
            it.toFloat().roundToInt()
        } ?: 0
    }


}