package com.example.exercisetracker.frontend.composables.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {


    companion object {
        const val dateFormat = "dd/MM/yyyy"
        fun toDate(timestamp: Long): String {
            return SimpleDateFormat(dateFormat, Locale.getDefault()).format(timestamp)
        }

        @SuppressLint("SimpleDateFormat")
        fun toTimestamp(date: String): Long {
            return SimpleDateFormat(dateFormat).parse(date)?.time ?: 0L
        }

    }

}