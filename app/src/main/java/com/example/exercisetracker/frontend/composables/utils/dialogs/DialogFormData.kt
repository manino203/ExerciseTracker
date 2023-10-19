package com.example.exercisetracker.frontend.composables.utils.dialogs

import androidx.compose.runtime.MutableState

data class DialogFormData(
    val format: FormFieldFormat,
    val label: ResourceString,
    var state: MutableState<String>
)

