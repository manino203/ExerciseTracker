package com.example.exercisetracker.frontend.composables.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

sealed class TextFieldFormat {

    open fun correspondsWithFormat(s: String): Boolean = true

    object Float : TextFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return if (s.isNotEmpty()) {
                if ("\n" in s){
                    false
                }
                else{
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


    object Int : TextFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return if (s.isNotEmpty()) {
                if ("\n" in s){
                    false
                }
                else{
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

    object Date : TextFieldFormat(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun correspondsWithFormat(s: String): Boolean {
            return if (!s.contains("\n") && s.isEmpty()){
                true
            }else{
                if (s.length == 10) {
                    (s.slice(0 until 2).toInt()) < 31 && (s.slice(3 until 4).toInt()) < 12 && Regex(
                        "\\d{2}/\\d{2}/\\d{4}"
                    ).containsMatchIn(s)
                }else{
                    true
                }
            }
        }
        }


    object Str : TextFieldFormat() {
        override fun correspondsWithFormat(s: String): Boolean {
            return !s.contains("\n")
        }
    }
}