package com.example.exercisetracker.frontend.composables.utils

data class EditDataWrapper(
    val format: TextFieldFormat,
    val label: String,
    var value: String
) {
}