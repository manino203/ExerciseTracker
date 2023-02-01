package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.runtime.MutableState

data class EditDataWrapper(
    val format: TextFieldFormat,
    val label: String,
    var state: MutableState<String>
) {
}