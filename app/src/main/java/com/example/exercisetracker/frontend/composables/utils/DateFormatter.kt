package com.example.exercisetracker.frontend.composables.utils

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {


    companion object {
        const val dateFormat = "dd/MM/yyyy"
        fun toDate(timestamp: Long): String {
            return SimpleDateFormat(dateFormat, Locale.getDefault()).format(timestamp)
        }

        fun toLong(date: String): Long {
            return 0L
        }

    }

}