package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.ui.graphics.Color

fun Color.setAlpha(alpha: Float): Color {
    return Color(red, green, blue, alpha, colorSpace)
}