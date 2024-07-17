package com.example.myapplication.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDateTime(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}

fun Long.toDeltaTime(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return "${hours}h ${minutes % 60}m ${seconds % 60}s"
}