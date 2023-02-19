package com.example.exercisetracker.frontend.composables.utils.dialogs

import androidx.compose.runtime.MutableState

data class DialogFormData(
    val format: TextFieldFormat,
    val label: String,
    var state: MutableState<String>
)

