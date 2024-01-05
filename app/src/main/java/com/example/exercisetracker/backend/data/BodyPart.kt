package com.example.exercisetracker.backend.data

import androidx.annotation.StringRes

data class BodyPart(
    val path: String,
    @StringRes val label: Int
) {
}