package com.example.pandu.calisthenics

import android.view.View
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun toDatetoString(str: String, mode: String) : String {
    val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val format = SimpleDateFormat(mode, Locale.ENGLISH)
    val date = format1.parse(str)
    return format.format(date)
}