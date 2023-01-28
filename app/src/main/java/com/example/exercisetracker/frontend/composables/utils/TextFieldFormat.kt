package com.example.exercisetracker.frontend.composables.utils

sealed class TextFieldFormat {

    open fun correspondsWithFormat(s: String): Boolean = true

    object Float : TextFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return try {
                s.toFloat()
                true
            } catch (e: java.lang.NumberFormatException) {
                false
            }
        }
    }

    object Int : TextFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return try {
                s.toInt()
                true
            } catch (e: java.lang.NumberFormatException) {
                false
            }
        }
    }

    object Date : TextFieldFormat()

    object Str : TextFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return !s.contains("\n")
        }
    }
}