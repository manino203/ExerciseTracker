package com.example.exercisetracker.frontend.composables.utils.dialogs

import androidx.compose.ui.text.input.KeyboardType

sealed class FormFieldFormat {

    open fun correspondsWithFormat(s: String): Boolean = true
    open val keyboardType = KeyboardType.Text

    object Float : FormFieldFormat() {

        override val keyboardType: KeyboardType = KeyboardType.Number
        override fun correspondsWithFormat(s: String): Boolean {
            return if (s.isNotEmpty()) {
                if ("\n" in s || "d" in s || "D" in s) {
                    false
                } else {
                    try {
                        s.toFloat()
                        true
                    } catch (e: java.lang.NumberFormatException) {
                        false
                    }
                }
            } else {
                true
            }
        }


    }


    object Int : FormFieldFormat() {

        override val keyboardType: KeyboardType = KeyboardType.Number
        override fun correspondsWithFormat(s: String): Boolean {
            return if (s.isNotEmpty()) {
                if ("\n" in s) {
                    false
                } else {
                    try {
                        s.toInt()
                        true
                    } catch (e: java.lang.NumberFormatException) {
                        false
                    }
                }
            } else {
                true
            }
        }
    }


    object Date : FormFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return if (!s.contains("\n") && s.isEmpty()) {
                true
            } else {
                if (s.length == 10) {
                    (s.slice(0 until 2).toInt()) < 31 && (s.slice(3 until 4).toInt()) < 12 && Regex(
                        "\\d{2}/\\d{2}/\\d{4}"
                    ).containsMatchIn(s)
                } else {
                    true
                }
            }
        }
    }


    object Str : FormFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return !s.contains("\n")
        }
    }
}